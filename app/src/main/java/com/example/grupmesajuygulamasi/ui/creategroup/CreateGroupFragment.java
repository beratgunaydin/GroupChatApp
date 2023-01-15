package com.example.grupmesajuygulamasi.ui.creategroup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grupmesajuygulamasi.GroupClass;
import com.example.grupmesajuygulamasi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class CreateGroupFragment extends Fragment {
    // Sayfa tasarımında oluşturulan kısımları alabilmek için alanlar oluşturuldu
    EditText groupName;
    EditText groupDescription;
    ImageView groupSymbolImage;
    Button createGroupButton;
    RecyclerView groupsListRecyclerView;

    // Firebase'de kullanacağımız kısımlar için alanlar oluşturuldu
    FirebaseAuth firebaseAuth;
    // Kullanıcının grup bilgilerini kaydetmek için
    FirebaseFirestore firebaseFirestore;
    // Kullanıcının belirlediği grup resimlerini kaydetmek için
    FirebaseStorage firebaseStorage;

    // Resimlerin dosya yolu
    Uri filePath;

    // Grupların tutulacağı grup listesi
    List<GroupClass> groups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        // Sayfadaki girilen verileri almak için id'leri ile oluşturan alanlara atama yapıldı
        groupName = view.findViewById(R.id.createGroupPage_groupName);
        groupDescription = view.findViewById(R.id.createGroupPage_groupDescription);
        groupSymbolImage = view.findViewById(R.id.createGroupPage_groupImage);
        createGroupButton = view.findViewById(R.id.createGroupPage_createGroupButton);
        groupsListRecyclerView = view.findViewById(R.id.createGroupPage_groupsRecyclerView);

        // Tek bir nesne oluşturulması ve tekrar çağrılırsa aynı nesnenin döndürülmesi sağlandı
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // Oluşturulan gruplar listesinin örneği alındı
        groups = new ArrayList<>();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        filePath = data.getData();
                        groupSymbolImage.setImageURI(filePath);
                    }
                }
        );

        // Fotoğraf seçme kısmına tıklandığında
        groupSymbolImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);
            }
        });

        // Grup oluşturma butonuna tıklandığında
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sayfada girilen grup ismi ve grup açıklaması alanları String değişkenlere atandı
                String group_name = String.valueOf(groupName.getText());
                String group_description = String.valueOf(groupDescription.getText());

                //Grup ismi girilmemişse
                if (group_name.isEmpty()) {
                    // Grup adı alanı uyarısı
                    Toast.makeText(getContext(), "Grup adı alanı boş bırakılamaz", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Grup açıklaması girilmemişse
                if (group_description.isEmpty()) {
                    //Grup açıklaması uyarısı
                    Toast.makeText(getContext(), "Açıklama alanı boş bırakılamaz", Toast.LENGTH_SHORT).show();
                }

                // Fotoğraf seçilmemişse
                if(filePath != null) {
                    StorageReference storageReference = firebaseStorage.getReference().child("images/" + UUID.randomUUID().toString());

                    // Fotoğraf seçilmemişse ayarlanmış bir fotoğraf ekle
                    storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadURL = uri.toString();
                                    CreateGroup(group_name, group_description, downloadURL);
                                }
                            });
                        }
                    });
                    return;
                }
                // Fotoğraf seçilmişse
                else {
                    CreateGroup(group_name, group_description, null);
                }
            }
        });
        AddGroupsToRecylerView();
        return view;
    }

    // Grup oluşturan ve firestore'a kaydeden metot
    private void CreateGroup(String group_name, String group_description, String group_image) {
        // Mevcut kullanıcının id'si
        String userID = firebaseAuth.getCurrentUser().getUid();

        // Firestore'da oluşturulmuş koleksiyonda kullanıcıya oluşturduğu grup bilgilerini ekler
        firebaseFirestore.collection("/veri/" + userID + "/" + "usersGroups").add(new HashMap<String, Object>() {
            {
                put("groupName", group_name);
                put("groupDescription", group_description);
                put("groupImage", group_image);
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Grup Oluşturuldu", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Grup Oluşturma İşlemi Başarısız", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Grupları arayüzdeki gruplar listesine ekleme
    private void AddGroupsToRecylerView() {
        // Mevcut kullanıcının id'si
        String userID = firebaseAuth.getCurrentUser().getUid();

        // Firestore'da oluşturulmuş koleksiyonda kullanıcının oluşturduğu grup bilgilerini getirir
        firebaseFirestore.collection("/veri/" + userID + "/" + "usersGroups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Mevcut listeyi temizler
                groups.clear();
                // Tüm gruplar listesi içerisinde for döngüsü ile gezildi
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    // Bir grup nesnesi oluşturuldu ve firestore'dan çekilen verilerle bu nesnenin özellikleri atandı
                    GroupClass groupClass = new GroupClass(documentSnapshot.getString("groupImage"), documentSnapshot.getString("groupName"), documentSnapshot.getString("groupDescription"), documentSnapshot.getId());
                    // Listeye grup eklendi
                    groups.add(groupClass);
                }

                // Bir adapter kullanarak RecyclerView ayarlandı
                groupsListRecyclerView.setAdapter(new GroupsAdapter(groups));
                // Gruplar için oluşturulan arayüz elemanları için bir yönetici ayarlandı
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                // RecyclerView'ın yöneticisi atandı
                groupsListRecyclerView.setLayoutManager(linearLayoutManager);
            }
        });
    }
}