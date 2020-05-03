package com.weiss.weiss.model.ggl;

import lombok.Data;

@Data
public class GglUser {
    String id;
    String email;
    String sub;
    boolean verified_email;
    String name;
    String given_name;
    String family_name;
    String picture;
    String locale;
}