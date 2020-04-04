package com.weiss.weiss.exceptions;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class ErrorObj {
    private String message;
}
