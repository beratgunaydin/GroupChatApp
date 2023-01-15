package com.example.grupmesajuygulamasi;

public class MessageClass {
    // Mesaj bilgilerinin tutulacağı alanlar
    private String messageName;
    private String message;
    private String userID;

    // Mesaj nesnesinin özelliklerinin tutulduğu ve atandığı constructor metot oluşturuldu
    public MessageClass(String messageName, String message, String userID) {
        this.messageName = messageName;
        this.message = message;
        this.userID = userID;
    }

    // Alanlara ulaşmak için get ve set metotları oluşturuldu
    public String getMessageName() {
        return messageName;
    }

    public String getMessage() {
        return message;
    }

    public String getUserID() {
        return userID;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
