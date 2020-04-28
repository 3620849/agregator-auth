package com.weiss.weiss.model.git;

import lombok.Data;

@Data
public class Plan {
    private String name;
    private float space;
    private float collaborators;
    private float private_repos;
}
