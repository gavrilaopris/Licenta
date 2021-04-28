package com.example.myapplication.Model;

public class Etapa {

    private String id;
    private String titlu;
    private String descriere;
    private String endDate;
    private String startDate;
    private String status;
    private String projectID;
    private String visibility;

    public Etapa(String id, String titlu, String descriere, String endDate, String startDate, String status, String projectID, String visibility) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
        this.projectID = projectID;
        this.visibility = visibility;
    }

    public Etapa() {
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

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
