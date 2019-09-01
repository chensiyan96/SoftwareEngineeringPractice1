package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagVO extends Tag {
    private String linkNum;
}
