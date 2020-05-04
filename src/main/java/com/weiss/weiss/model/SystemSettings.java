package com.weiss.weiss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemSettings {
   @Value("${github.userAuthorizationUrl}")
   private String git_userAuthorizationUrl;
   @Value("${vk.userAuthorizationUrl}")
   private String vk_userAuthorizationUrl;
   @Value("${fb.userAuthorizationUrl}")
   private String fb_userAuthorizationUrl;
   @Value("${ggl.userAuthorizationUrl}")
   private String ggl_userAuthorizationUrl;
   @Value("${dis.userAuthorizationUrl}")
   private String dis_userAuthorizationUrl;
}
