package com.example.workpermit;

public class PermitModel {
    private String uid, no_permit, area_kerja, status, userUid;

    public PermitModel(String uid, String no_permit, String area_kerja, String status, String userUid) {
        this.uid = uid;
        this.no_permit = no_permit;
        this.area_kerja = area_kerja;
        this.status = status;
        this.userUid = userUid;
    }

    public PermitModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNo_permit() {
        return no_permit;
    }

    public void setNo_permit(String no_permit) {
        this.no_permit = no_permit;
    }

    public String getArea_kerja() {
        return area_kerja;
    }

    public void setArea_kerja(String area_kerja) {
        this.area_kerja = area_kerja;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
