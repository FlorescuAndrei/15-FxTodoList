package com.andrei.todolist.datamodel;

import java.time.LocalDate;

public class TodoItem {

    private String description;
    private String details;
    private LocalDate deadLine;

    public TodoItem(String description, String details, LocalDate deadLine) {
        this.description = description;
        this.details = details;
        this.deadLine = deadLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "description='" + description + '\'' +
                '}';
    }


}
