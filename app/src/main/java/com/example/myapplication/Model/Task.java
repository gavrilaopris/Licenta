package com.example.myapplication.Model;

public class Task {

    private String id;
    private String titlu;
    private String descriere;
    private String endDate;
    private String startDate;
    private String status;

    public Task(String id, String titlu, String descriere, String endDate, String startDate, String status) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
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
}
