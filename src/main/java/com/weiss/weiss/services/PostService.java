package com.weiss.weiss.services;

import com.weiss.weiss.dao.PostDao;
import com.weiss.weiss.model.forum.Post;
import com.weiss.weiss.model.forum.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    PostDao postDao;
    public void savePost(Post postMsg) {
        postDao.save(postMsg);
    }
    public PostListDto getPostListNew(){
        return postDao.getListNewPost();
    }
}
