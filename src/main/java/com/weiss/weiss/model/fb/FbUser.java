package com.weiss.weiss.model.fb;

import lombok.Data;

@Data
public class FbUser {
    String id;
    String last_name;
    String first_name;
    String email;
    Picture picture;
}
