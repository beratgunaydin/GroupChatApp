package com.example.grupmesajuygulamasi.ui.creategroup;

import static java.lang.System.load;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupmesajuygulamasi.GroupClass;
import com.example.grupmesajuygulamasi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {
    // Gruplar için bir liste tanımlandı
    private List<GroupClass> groupsList;

    // Yapıcı metot ile gelen grup listesi sınıftaki listeye atandı
    public GroupsAdapter(List<GroupClass> groupsList) {
        this.groupsList = groupsList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Her bir grup için oluşturulmuş arayüz elemanı baz alınarak bir ViewHolder tanımlandı
        GroupViewHolder groupViewHolder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.creategroup_group, parent, false));
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.GroupViewHolder holder, int position) {
        GroupClass groupClass = groupsList.get(position);
        holder.setValues(groupClass);
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        // Her bir grup için oluşturulmuş arayüz elemanları için alanlar oluşturuldu
        private ImageView groupImage;
        private TextView groupName;
        private TextView groupDescription;

        // Her bir arayüz elemanı için gelen değerler yapıcı metotla alındı
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            // Arayüzdeki elemanlar oluşturulan alanlara atandı
            groupImage = itemView.findViewById(R.id.creategroup_group_groupImage);
            groupName = itemView.findViewById(R.id.creategroup_group_groupName);
            groupDescription = itemView.findViewById(R.id.creategroup_group_groupDescription);
        }

        public void setValues(GroupClass groupClass) {
            // Eğer bi grup simgesi seçilmemişse önceden ayarlanmış resmi ekle
            if(groupClass.getGroupImage() != null) {
                Picasso.get().load(groupClass.getGroupImage()).into(groupImage);
            }

            // Grup ismi ve Grup açıklaması alanı ayarlandı
            groupName.setText(groupClass.getGroupName());
            groupDescription.setText(groupClass.getGroupDescription());
        }
    }
}
