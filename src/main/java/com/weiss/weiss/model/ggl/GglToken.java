package com.weiss.weiss.model.ggl;

import com.weiss.weiss.model.BaseToken;
import lombok.Data;

@Data
public class GglToken extends BaseToken {
    int expires_in;
    String scope;
    String token_type;
}
