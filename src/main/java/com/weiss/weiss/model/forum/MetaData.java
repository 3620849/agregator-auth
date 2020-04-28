package com.weiss.weiss.model.forum;

import lombok.Data;

import java.util.List;

@Data
public class MetaData {
    List<Like> likeList;
    List<Dislike> dislikeList;
    List<Comment> commentsList;
    int views;
    int loads;
}
