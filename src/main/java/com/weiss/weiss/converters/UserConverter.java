package com.weiss.weiss.converters;

import com.weiss.weiss.model.dis.DisqusResponse;
import com.weiss.weiss.model.fb.FbUser;
import com.weiss.weiss.model.ggl.GglUser;
import com.weiss.weiss.model.git.GitHubUser;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.model.vk.VkUser;
import org.springframework.stereotype.Service;

@Service
public class UserConverter {
    public UserInfo convert(GitHubUser user){
        return UserInfo.builder().login(user.getLogin()).mail(user.getEmail()).build();
    }
    public UserInfo convert(VkUser user){
        return UserInfo.builder().username(user.getFirst_name()).photo(user.getPhoto_100()).build();
    }
    public UserInfo convert(FbUser user){
        return UserInfo.builder().mail(user.getEmail()).photo(user.getPicture().getData().getUrl()).username(user.getFirst_name()).build();
    }
    public UserInfo convert(GglUser gglUserData) {
        return UserInfo.builder().mail(gglUserData.getEmail()).photo(gglUserData.getPicture()).username(gglUserData.getName()).build();
    }
    public UserInfo convert(DisqusResponse disUserData) {
        return UserInfo.builder().username(disUserData.getName()).photo(disUserData.getAvatar().getSmall().getPermalink()).build();
    }
}
