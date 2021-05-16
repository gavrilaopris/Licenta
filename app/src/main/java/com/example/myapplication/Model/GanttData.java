package com.example.myapplication.Model;

import java.util.List;

/**
 * Created by Jayesh Bhadja on 5/10/17.
 */

public class GanttData {
    private static GanttData ganttData = null;
    private com.example.myapplication.Model.Etapa etapa;
    private List<com.example.myapplication.Model.Task> tasks;
    private List<Milestone> milestones;

    /**
     * Initializing Gantt Data
     *
     * @param etapa    Etapa
     * @param tasks      list of Task
     * @param milestones list of Milestones
     * @return GanttData
     */
    public static GanttData initGanttData(com.example.myapplication.Model.Etapa etapa, List<com.example.myapplication.Model.Task> tasks, List<Milestone> milestones) {
        ganttData = new GanttData();
        ganttData.setEtapa(etapa);
        ganttData.setTasks(tasks);
        ganttData.setMilestones(milestones);

        return ganttData;
    }

    public static GanttData getGanttData() {
        return ganttData;
    }

    public com.example.myapplication.Model.Etapa getEtapa() {
        return etapa;
    }

    /**
     * Setter for project object
     * @param etapa
     * Project Class Object
     */
    public void setEtapa(com.example.myapplication.Model.Etapa etapa) {
        this.etapa = etapa;
    }

    public List<com.example.myapplication.Model.Task> getTasks() {
        return tasks;
    }

    /**
     * Setter for Task List
     * @param tasks
     * List<Task>
     */
    public void setTasks(List<com.example.myapplication.Model.Task> tasks) {
        this.tasks = tasks;
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * Setter for Milestone List
     * @param milestones
     * List<Milestone>
     */
    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }

}
