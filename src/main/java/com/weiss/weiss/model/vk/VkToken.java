package com.weiss.weiss.model.vk;

import com.weiss.weiss.model.BaseToken;
import lombok.Data;

@Data
public class VkToken extends BaseToken {
    private int expires_in;
    private String user_id;
}
