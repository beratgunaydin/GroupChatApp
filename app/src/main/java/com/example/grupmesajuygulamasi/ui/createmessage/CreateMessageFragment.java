package com.example.grupmesajuygulamasi.ui.createmessage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grupmesajuygulamasi.MessageClass;
import com.example.grupmesajuygulamasi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateMessageFragment extends Fragment {
    // Sayfa tasarımında oluşturulan kısımları alabilmek için alanlar oluşturuldu
    EditText messageName;
    EditText message;
    Button createMessageButton;
    RecyclerView messagesListRecyclerView;

    // Firebase'de kullanacağımız kısımlar için alanlar oluşturuldu
    FirebaseAuth firebaseAuth;
    // Kullanıcının grup bilgilerini kaydetmek için
    FirebaseFirestore firebaseFirestore;

    // Mesajların tutulacağı grup listesi
    List<MessageClass> messageClassList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        // Sayfadaki girilen verileri almak için id'leri ile oluşturan alanlara atama yapıldı
        messageName = view.findViewById(R.id.createMessagePage_messageName);
        message = view.findViewById(R.id.createMessagePage_message);
        createMessageButton = view.findViewById(R.id.createMessagePage_createMessageButton);
        messagesListRecyclerView = view.findViewById(R.id.createMessagePage_messagesRecyclerView);

        // Tek bir nesne oluşturulması ve tekrar çağrılırsa aynı nesnenin döndürülmesi sağlandı
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Oluşturulan gruplar listesinin örneği alındı
        messageClassList = new ArrayList<>();

        // Mesaj oluşturma butonuna tıklandığında
        createMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sayfada girilen mesaj ismi ve mesaj alanları String değişkenlere atandı
                String message_name = String.valueOf(messageName.getText());
                String message_description = String.valueOf(message.getText());

                //Mesaj ismi girilmemişse
                if(message_name.isEmpty()) {
                    // Mesaj adı alanı uyarısı
                    Toast.makeText(getContext(), "Mesaj Adı Alanı Boş Bırakılamaz", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mesaj girilmemişse
                if(message_description.isEmpty()) {
                    //Mesaj uyarısı
                    Toast.makeText(getContext(), "Mesaj Alanı Boş Bırakılamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                CreateMessage(message_name, message_description);
            }
        });
        AddMessagesToRecyclerView();
        return view;
    }

    // Mesaj oluşturan ve firestore'a kaydeden metot
    public void CreateMessage(String message_name, String message_description) {
        // Mevcut kullanıcının id'si
        String userID = firebaseAuth.getCurrentUser().getUid();

        // Firestore'da oluşturulmuş koleksiyonda kullanıcıya oluşturduğu mesaj bilgilerini ekler
        firebaseFirestore.collection("/veri/" + userID + "/" + "usersMessages").add(new HashMap<String, String >() {
            {
                put("messageName", message_name);
                put("messageDescription", message_description);
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Mesaj Oluşturuldu", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Mesaj Oluşturma İşlemi Başarısız ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Mesajları arayüzdeki mesajlar listesine ekleme
    private void AddMessagesToRecyclerView() {
        // Mevcut kullanıcının id'si
        String userID = firebaseAuth.getCurrentUser().getUid();

        // Firestore'da oluşturulmuş koleksiyonda kullanıcının oluşturduğu mesaj bilgilerini getirir
        firebaseFirestore.collection("/veri/" + userID + "/" + "usersMessages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Mevcut listeyi temizler
                messageClassList.clear();
                // Tüm mesajlar listesi içerisinde for döngüsü ile gezildi
                for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                    // Bir mesaj nesnesi oluşturuldu ve firestore'dan çekilen verilerle bu nesnenin özellikleri atandı
                    MessageClass messageClass = new MessageClass(documentSnapshot.getString("messageName"), documentSnapshot.getString("messageDescription"), documentSnapshot.getId());
                    // Listeye mesaj eklendi
                    messageClassList.add(messageClass);
                }

                // Bir adapter kullanarak RecyclerView ayarlandı
                messagesListRecyclerView.setAdapter(new MessagesAdapter(messageClassList));
                // Mesajlar için oluşturulan arayüz elemanları için bir yönetici ayarlandı
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                // RecyclerView'ın yöneticisi atandı
                messagesListRecyclerView.setLayoutManager(linearLayoutManager);
            }
        });
    }
}