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
    String id;
    long time;
    long responseTime;
    MessageType type;
    @JsonSerialize(using= ToStringSerializer.class)
    ObjectId ancestorId;
    @JsonSerialize(using= ToStringSerializer.class)
    ObjectId parentPostId;
    List<Like> markList;
    MetaDataSummary summary;
    String userId;
    String userName;
    String userPhoto;
    String clientId;
    String header;
    List<Content> content;
    List<Content> shortContent;
    List<Message> comments;
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
