package com.example.myapplication.Model;

import java.util.List;

public class GanttDataEtape {

    private static GanttDataEtape ganttDataEtape = null;
    private com.example.myapplication.Model.Project project;
    private List<com.example.myapplication.Model.Etapa> etape;
    private List<Milestone> milestones;

    public static GanttDataEtape initGanttDataEtape(com.example.myapplication.Model.Project project, List<com.example.myapplication.Model.Etapa> etape, List<Milestone> milestones) {
        ganttDataEtape = new GanttDataEtape();
        ganttDataEtape.setProject(project);
        ganttDataEtape.setEtapa(etape);
        ganttDataEtape.setMilestones(milestones);

        return ganttDataEtape;
    }

    public static GanttDataEtape getGanttDataEtape() {
        return ganttDataEtape;
    }

    public com.example.myapplication.Model.Project getproject() {
        return project;
    }

    /**
     * Setter for project object
     * @param project
     * Project Class Object
     */
    public void setProject(com.example.myapplication.Model.Project project) {
        this.project = project;
    }

    public List<com.example.myapplication.Model.Etapa> getEtapa() {
        return etape;
    }

    /**
     * Setter for Task List
     * @param etape
     * List<Task>
     */
    public void setEtapa(List<com.example.myapplication.Model.Etapa> etape) {
        this.etape = etape;
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
