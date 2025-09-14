package tech.zeta.util;

import tech.zeta.model.Project;
import tech.zeta.model.Task;

public class ModelFiller {

    public static Project fillProject(Project currentProject, Project updatedProject) {
        if (updatedProject.getName() != null) {
            currentProject.setName(updatedProject.getName());
        }
        if (updatedProject.getDescription() != null ) {
            currentProject.setDescription(updatedProject.getDescription());
        }
        if (updatedProject.getStatus() != null) {
            currentProject.setStatus(updatedProject.getStatus());
        }
        if (updatedProject.getBudget() != 0) {
            currentProject.setBudget(updatedProject.getBudget());
        }
        if (updatedProject.getActualSpend() != 0) {
            currentProject.setActualSpend(updatedProject.getActualSpend());
        }
        if (updatedProject.getStartDate() != null) {
            currentProject.setStartDate(updatedProject.getStartDate());
        }
        if (updatedProject.getEndDate() != null) {
            currentProject.setEndDate(updatedProject.getEndDate());
        }
        if (updatedProject.getBuilderId() != 0) {
            currentProject.setBuilderId(updatedProject.getBuilderId());
        }
        return currentProject;
    }


    public static Task fillTask(Task currentTask, Task updatedTask) {
        if (updatedTask.getName() != null) {
            currentTask.setName(updatedTask.getName());
        }
        if (updatedTask.getDescription() != null ) {
            currentTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getProgress() != 0) {
            currentTask.setProgress(updatedTask.getProgress());
        }
        if (updatedTask.getStartDate() != null) {
            currentTask.setStartDate(updatedTask.getStartDate());
        }
        if (updatedTask.getEndDate() != null) {
            currentTask.setEndDate(updatedTask.getEndDate());
        }

        return currentTask;
    }
}
