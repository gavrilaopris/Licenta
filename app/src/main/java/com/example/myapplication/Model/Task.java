package com.example.myapplication.Model;

public class Task {

    private String id;
    private String titlu;
    private String imageUrl;
    private String descriere;
    private String endDate;
    private String startDate;
    private String status;
    private String etapaID;
    private String visibility;


    public Task(String id, String titlu, String descriere, String endDate, String startDate, String status, String imageUrl, String etapaID, String visibility) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
        this.imageUrl = imageUrl;
        this.etapaID = etapaID;
        this.visibility = visibility;
    }

    public Task() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEtapaID() {
        return etapaID;
    }

    public void setEtapaID(String etapaID) {
        this.etapaID = etapaID;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
