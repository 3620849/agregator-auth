package com.weiss.weiss.model.vk;

import lombok.Data;

@Data
public class VkUser {
    int id;
    String first_name;
    String last_name;
    boolean is_closed;
    boolean can_access_closed;
    short sex;
    String bdate;
    String photo_100;
    String mobile_phone;
    String home_phone;
    String home_town;
}
