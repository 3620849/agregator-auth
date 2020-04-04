package com.weiss.weiss.model.git;

import com.weiss.weiss.model.BaseToken;
import lombok.Data;

@Data
public class GitHubToken extends BaseToken {
    private String token_type;
    private String scope;
}
