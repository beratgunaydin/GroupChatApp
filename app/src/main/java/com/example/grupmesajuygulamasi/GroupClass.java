package com.example.grupmesajuygulamasi;

public class GroupClass {
    // Grup bilgilerinin tutulacağı alanlar
    private String groupImage;
    private String groupName;
    private String groupDescription;
    private String userID;

    // Grup nesnesinin özelliklerinin tutulduğu ve atandığı constructor metot oluşturuldu
    public GroupClass(String groupImage, String groupName, String groupDescription, String userID) {
        this.groupImage = groupImage;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.userID = userID;
    }

    // Alanlara ulaşmak için get ve set metotları oluşturuldu
    public String getGroupImage() {
        return groupImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public String getUserID() {
        return userID;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
