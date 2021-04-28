package com.example.myapplication.Model;

import java.util.List;

public class Project {

    private String id;
    private String name;
    private String descriere;
    private String endDate;
    private String startDate;
    private String status;
    private String visibility;





    public Project(String id, String name, String descriere, String endDate, String startDate, String status, String visibility) {

        this.id = id;
        this.name = name;
        this.descriere = descriere;
        this.descriere = descriere;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
        this.visibility = visibility;

    }

    public Project() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
