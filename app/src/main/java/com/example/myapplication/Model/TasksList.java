package com.example.myapplication.Model;

public class TasksList {

    public String taskid;

    public TasksList(String taskid) {
        this.taskid = taskid;
    }

    public TasksList() {
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}
