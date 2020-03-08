package com.shivamgaba.managerapp;

public class manager {
private String managerName,managerPicUrl,managerEmailId,managerPhoneNumber;

    public manager(String managerName, String managerPicUrl, String managerEmailId, String managerPhoneNumber) {
        this.managerName = managerName;
        this.managerPicUrl = managerPicUrl;
        this.managerEmailId = managerEmailId;
        this.managerPhoneNumber = managerPhoneNumber;
    }

    public manager() {
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPicUrl() {
        return managerPicUrl;
    }

    public void setManagerPicUrl(String managerPicUrl) {
        this.managerPicUrl = managerPicUrl;
    }

    public String getManagerEmailId() {
        return managerEmailId;
    }

    public void setManagerEmailId(String managerEmailId) {
        this.managerEmailId = managerEmailId;
    }

    public String getManagerPhoneNumber() {
        return managerPhoneNumber;
    }

    public void setManagerPhoneNumber(String managerPhoneNumber) {
        this.managerPhoneNumber = managerPhoneNumber;
    }
}
