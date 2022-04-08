package com.appstechio.workyzo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Job implements Serializable {

    private String Employer_ID;
    private String Job_ID;
    private String Title;
    private String Description;
    private String CreatedDate;
    private ArrayList<HashMap> Proposals;
    private ArrayList<String> SkillsRequired;
    private Map<String,Object> Budget;
    private String Hired_Freelancer;
    private Boolean Completed;
    private ArrayList<HashMap> UploadedFiles;



    public Job(){

    }

    public Job(String employer_ID, String job_ID, String title, String description, String createdDate, ArrayList<HashMap> proposals, ArrayList<String> skillsRequired, Map<String, Object> budget, String freelancer_Hired, Boolean completed, ArrayList<HashMap> uploadedFiles) {
        Employer_ID = employer_ID;
        Job_ID = job_ID;
        Title = title;
        Description = description;
        CreatedDate = createdDate;
        Proposals = proposals;
        SkillsRequired = skillsRequired;
        Budget= budget;
        Hired_Freelancer = freelancer_Hired;
        Completed = completed;
        UploadedFiles = uploadedFiles;
    }


    public String getEmployer_ID() {
        return Employer_ID;
    }

    public void setEmployer_ID(String employer_ID) {
        Employer_ID = employer_ID;
    }

    public String getJob_ID() {
        return Job_ID;
    }

    public void setJob_ID(String job_ID) {
        Job_ID = job_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public Map<String, Object> getBudget() {
        return Budget;
    }

    public void setBudget(Map<String, Object> budget) {
        Budget = budget;
    }

    public ArrayList<HashMap> getProposals() {
        return Proposals;
    }

    public void setProposals(ArrayList<HashMap> proposals) {
        Proposals = proposals;
    }

    public ArrayList<String> getSkillsRequired() {
        return SkillsRequired;
    }

    public void setSkillsRequired(ArrayList<String> skillsRequired) {
        SkillsRequired = skillsRequired;
    }



    public String getHired_Freelancer() {
        return Hired_Freelancer;
    }

    public void setHired_Freelancer(String hired_Freelancer) {
        Hired_Freelancer = hired_Freelancer;
    }


    public Boolean getCompleted() {
        return Completed;
    }

    public void setCompleted(Boolean completed) {
        Completed = completed;
    }

    public ArrayList<HashMap> getUploadedFiles() {
        return UploadedFiles;
    }

    public void setUploadedFiles(ArrayList<HashMap> uploadedFiles) {
        UploadedFiles = uploadedFiles;
    }
}





