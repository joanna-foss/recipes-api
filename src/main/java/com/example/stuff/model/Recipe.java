package com.example.stuff.model;

import lombok.Data;

@Data
public class Recipe {
    private String name;
    private String[] ingredients;
    private String[] instructions;
}