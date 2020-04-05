package com.weiss.weiss.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SystemSettings {
   @Value("${github.userAuthorizationUrl}")
   private String git_userAuthorizationUrl;
   @Value("${vk.userAuthorizationUrl}")
   private String vk_userAuthorizationUrl;
   @Value("${fb.userAuthorizationUrl}")
   private String fb_userAuthorizationUrl;
   @Value("${twt.userAuthorizationUrl}")
   private String twt_userAuthorizationUrl;
   @Value("${ggl.userAuthorizationUrl}")
   private String ggl_userAuthorizationUrl;
}
