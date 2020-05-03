package com.weiss.weiss.model.forum;

import lombok.Data;

@Data
public class Like {
    long time;
    String userId;
    String clientId;
    LikeType likeType;
    byte value;
}
