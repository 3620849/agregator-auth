package com.weiss.weiss.services;

import com.weiss.weiss.dao.MessageDao;
import com.weiss.weiss.model.forum.Message;
import com.weiss.weiss.model.forum.MessageListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    MessageDao messageDao;
    public void saveMessage(Message messageMsg) {
        messageDao.save(messageMsg);
    }
    public MessageListDto getMessageListNew(String type, long skip){
        return messageDao.getMessageListNew(type,skip);
    }

    public boolean likeOrDislike(String messageId, String userId, String clientId, byte val) {
       return messageDao.likeOrDislike(messageId,userId,clientId,val);
    }
}
