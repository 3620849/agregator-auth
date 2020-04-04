package com.weiss.weiss.model.fb;

import com.weiss.weiss.model.BaseToken;
import lombok.Data;

@Data
public class FbToken extends BaseToken {
    String token_type;
    String expires_in;
}
