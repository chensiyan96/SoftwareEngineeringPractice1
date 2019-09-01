package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class TimelineMonth {
    private Integer month;
    private Integer count;
    private List<TimelinePost> posts;
}
