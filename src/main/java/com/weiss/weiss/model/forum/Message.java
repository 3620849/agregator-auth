package com.weiss.weiss.model.forum;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.weiss.weiss.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message  {
    private String id;
    private long time;
    private long responseTime;
    private MessageType type;
    @JsonSerialize(using= ToStringSerializer.class)
    private   ObjectId ancestorId;
    @JsonSerialize(using= ToStringSerializer.class)
    private ObjectId parentPostId;
    private List<Like> markList;
    private  boolean isEditable;
    private  MetaDataSummary summary;
    private String userId;
    private String userName;
    private String userPhoto;
    private String clientId;
    private String header;
    private List<Content> content;
    private List<Content> shortContent;
    private List<Message> comments;
    public void setUserData(UserInfo userInfo){
        userId = userInfo.getId();
        userName = userInfo.getUsername();
        if(userName==null || userName.isBlank()){
            userName="Anonymous";
            if(getUserId()!=null){
                userName="No name";
            }
        }
        userPhoto = userInfo.getPhoto();
        if(userPhoto==null || userPhoto.isBlank()){
            userPhoto="assets/logo.svg";
        }
    }
}
