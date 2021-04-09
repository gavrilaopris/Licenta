package com.example.myapplication.Model;

public class Activitate {
    private String Etapa1;
    private String Etapa2;
    private String Etapa3;

    public Activitate(String etapa1, String etapa2, String etapa3) {
        this.Etapa1 = etapa1;
        this.Etapa2 = etapa2;
        this.Etapa3 = etapa3;
    }

    public Activitate() {
    }

    public String getEtapa1() {
        return Etapa1;
    }

    public void setEtapa1(String etapa1) {
        this.Etapa1 = etapa1;
    }

    public String getEtapa2() {
        return Etapa2;
    }

    public void setEtapa2(String etapa2) {
        this.Etapa2 = etapa2;
    }

    public String getEtapa3() {
        return Etapa3;
    }

    public void setEtapa3(String etapa3) {
        this.Etapa3 = etapa3;
    }
}
