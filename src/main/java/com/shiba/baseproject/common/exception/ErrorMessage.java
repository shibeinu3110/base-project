package com.shiba.baseproject.common.exception;

import java.io.Serializable;

public interface ErrorMessage extends Serializable {
    int getCode();
    String getMessage();
}
