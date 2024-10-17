package com.example.hugbo_team_13.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class EventDTO {

    private Long id;
    private String name;
    private Date startTime;
    private Date endTime;


    // Constructor for creating DTO from entity (used when sending data to client)
    public EventDTO(Long id, String name, Date startTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startTime;
    }

    public Date getEndDate() {
        return endTime;
    }


    // Setters - no setter for id !!
    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
