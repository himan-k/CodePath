package com.codepath.simpletodo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HIMANSHU on 1/20/2015.
 */
public class Task implements Serializable{
    private int id;
    private String title;
    private String dueDate;

    public Task(String title, String dueDate) {
        this.title = title;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
