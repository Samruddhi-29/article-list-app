package com.practice.training.core.beans;



public class ArticleListDataBean {


    
    private String path;

    
    private String title;

    private String description;

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }
    
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
