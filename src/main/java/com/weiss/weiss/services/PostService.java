package com.weiss.weiss.services;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.dao.MessageDao;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.model.forum.Content;
import com.weiss.weiss.model.forum.ListMessages;
import com.weiss.weiss.model.forum.Message;
import com.weiss.weiss.model.forum.MessageListDto;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Value("${messages.maxEditTime}")
    long maxEditTime;
    @Value("${messages.maxEditTimeUnauthorized}")
    long maxEditTimeUnauth;
    @Autowired
    MessageDao messageDao;

    public void saveMessage(Message messageMsg) {
        messageDao.save(messageMsg);
    }

    public MessageListDto getMessageListNew(String type, long skip) {
        return messageDao.getMessageListNew(type, skip);
    }

    public boolean likeOrDislike(String messageId, String userId, String clientId, byte val) {
        return messageDao.likeOrDislike(messageId, userId, clientId, val);
    }

    public List<Content> truncateContent(Message messageMsg) {
        List<Content> result = new LinkedList<>();
        List<Content> content = messageMsg.getContent();
        if (content == null || content.size() == 0) {
            return result;
        }
        String state = getState(content);
        switch (state) {
            case "text":
            case "text_text":
            case "media":
            case "media_text":
            case "media_media":
            case "text_media":
                result = content.stream().limit(2).collect(Collectors.toList());
                break;
            case "text_over_text":
                result = content.stream().limit(1).map(o -> Content
                        .builder()
                        .index(o.getIndex()).type("text")
                        .text(o.getText().substring(0, Math.min(o.getText().length(), 1000)) + "...")
                        .build())
                        .collect(Collectors.toList());
                break;//limit 1 prefix ...
            case "text_text_over":
                result = EntryStream.of(content).limit(2).map(o -> {
                    String text = o.getValue().getText();
                    if (o.getKey() == 1) {
                        text = o.getValue().getText().substring(0, Math.min(o.getValue().getText().length(), 1000 - content.get(0).getText().length())) + "...";
                    }
                    return Content.builder().index(o.getKey()).type("text").text(text).build();
                }).collect(Collectors.toList());
                break;
            case "media_text_over":
            case "text_over_media":
                result = EntryStream.of(content).limit(2).map(o -> {
                    Content build = Content.builder().index(o.getKey()).type(o.getValue().getType()).url(o.getValue().getUrl()).build();
                    if (o.getValue().getType().equals("text")) {
                        build.setText(o.getValue().getText().substring(0, Math.min(o.getValue().getText().length(), 1000)) + "...");
                    }
                    return build;

                }).collect(Collectors.toList());
                break;
            case "text_over":
                result = content.stream()
                        .map(o -> Content.builder()
                                .index(o.getIndex())
                                .text(o.getText().substring(0, Math.min(o.getText().length(), 1000)) + "...")
                                .type(o.getType()).url(o.getUrl()).build())
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }

    ;

    private String getState(List<Content> content) {
        String state = new String();
        if (content.size() >= 2) {
            long c = content.stream().filter(o -> o.getType().equals("text")).count();
            if (c == 2) {
                state = "text_text";
                if (content.get(0).getText().length() >= 1000) {
                    state = "text_over_text";
                } else {
                    if (content.get(0).getText().length() + content.get(1).getText().length() > 1000) {
                        state = "text_text_over";
                    }
                }
            } else if (c == 1) {
                state = "text_media";
                if (content.get(0).getType().equals("text")) {
                    if (content.get(0).getText().length() > 1000) {
                        state = "text_over_media";
                    }
                } else {
                    state = "media_text";
                    if (content.get(1).getText().length() > 1000) {
                        state = "media_text_over";
                    }
                }
            } else if (c == 0) {
                state = "media_media";
            }
        }
        if (content.size() == 1) {
            if (content.get(0).getType().equals("text")) {
                state = "text";
                if (content.get(0).getText().length() > 1000) {
                    state = "text_over";
                }
            } else {
                state = "media";
            }
        }
        return state;
    }

    public MessageListDto getMessageListById(ListMessages idList) {
        return messageDao.getMessageListById(idList);
    }

    public MessageListDto getMessageListByUserId(String userId, long skip) {
        return messageDao.getMessageListByUserId(userId, skip);
    }

    public MessageListDto getCommentsById(String parentId) {
        return messageDao.getCommentsById(parentId);
    }

    public boolean removeMessageById(String messageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            UserAuthentication autUser = (UserAuthentication) authentication;
            UserInfo userInfo = autUser.getUserInfo();
            Set<String> roles = authentication.getAuthorities().stream()
                    .map(r -> r.getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_ADMIN")) {
                return messageDao.removeMessageById(messageId);
            }
            ListMessages listMessages = new ListMessages();
            listMessages.setIdsList(new String[]{messageId});
            MessageListDto messageListById = getMessageListById(listMessages);
            long editTime = new Date().getTime()-maxEditTime;
            if (messageListById != null && messageListById.getMessageList() != null) {
                Message message = messageListById.getMessageList().get(0);

                if (message !=null) {
                    String mUid = message.getUserId();
                    if(mUid==null){
                        mUid=message.getClientId();
                        editTime = new Date().getTime()-maxEditTimeUnauth;
                        if(mUid==null){
                            return false;
                        }
                    }
                    String uid= userInfo.getId();
                    if(uid==null){
                        uid=autUser.getClientId();
                    }
                    if (mUid.equals(uid)) {
                        if(message.getTime()>editTime){
                            return messageDao.removeMessageById(messageId);
                        }
                    }
                }
            }
        }
        return false;
    }
}
