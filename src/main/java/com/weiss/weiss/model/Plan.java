package com.weiss.weiss.model;

import lombok.Data;

@Data
public class Plan {
    private String name;
    private float space;
    private float collaborators;
    private float private_repos;
}
