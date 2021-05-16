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
    private String statusDate;
    private String percentageComplete;

    public Etapa(String id, String titlu, String descriere, String endDate, String startDate, String status, String projectID, String visibility, String statusDate, String percentageComplete) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
        this.projectID = projectID;
        this.visibility = visibility;
        this.statusDate = statusDate;
        this.percentageComplete = percentageComplete;
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

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(String percentageComplete) {
        this.percentageComplete = percentageComplete;
    }
}
