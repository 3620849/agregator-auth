package com.weiss.weiss.model.dis;

import com.weiss.weiss.model.BaseToken;
import lombok.Data;

@Data
public class DisToken extends BaseToken {
    String username;
    int user_id;
    int expires_in;
    String token_type;
    String state;
    String scope;
    String refresh_token;
}
