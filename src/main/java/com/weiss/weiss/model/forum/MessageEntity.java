package com.weiss.weiss.model.forum;

import lombok.Data;

@Data
public class MessageEntity {
    long time;
    long responseTime;
    MetaData metaData;
    MetaDataSummary summary;
    String userId;
    String userName;
    String userPhoto;
    String clientId;
}
