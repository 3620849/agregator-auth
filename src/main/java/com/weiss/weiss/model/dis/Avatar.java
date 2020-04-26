package com.weiss.weiss.model.dis;

import lombok.Data;

@Data
public class Avatar {
    Small small;
    private boolean isCustom;
    private String permalink;
    private String cache;
    Large large;
}
