package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class TimelineYear {
    private Integer year;
    private Integer count;
    private List<TimelineMonth> months;
}
