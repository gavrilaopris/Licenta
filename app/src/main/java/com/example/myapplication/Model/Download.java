package com.example.myapplication.Model;

public class Download {

    private String link;
    private String name;
    private String extension;

    public Download(String link, String name, String extension) {
        this.link = link;
        this.name = name;
        this.extension = extension;
    }

    public Download() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
