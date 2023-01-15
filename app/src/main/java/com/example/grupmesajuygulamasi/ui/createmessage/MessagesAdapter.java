package com.example.grupmesajuygulamasi.ui.createmessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupmesajuygulamasi.MessageClass;
import com.example.grupmesajuygulamasi.R;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    // Mesajlar için bir liste tanımlandı
    private List<MessageClass> messageList;

    // Yapıcı metot ile gelen mesaj listesi sınıftaki listeye atandı
    public MessagesAdapter(List<MessageClass> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Her bir mesaj için oluşturulmuş arayüz elemanı baz alınarak bir ViewHolder tanımlandı
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.createmessage_message, parent, false));
        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        MessageClass messageClass = messageList.get(position);
        holder.setValues(messageClass);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        // Her bir mesaj için oluşturulmuş arayüz elemanları için alanlar oluşturuldu
        TextView messageName;
        TextView message;

        // Her bir arayüz elemanı için gelen değerler yapıcı metotla alındı
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            messageName = itemView.findViewById(R.id.createmessage_message_messageName);
            message = itemView.findViewById(R.id.createmessage_message_message);
        }

        public void setValues(MessageClass messageClass) {
            // Mesaj ismi ve mesaj alanı ayarlandı
            messageName.setText(messageClass.getMessageName());
            message.setText(messageClass.getMessage());
        }
    }
}
