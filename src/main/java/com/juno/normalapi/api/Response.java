package com.juno.normalapi.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {
    private ResponseCode code;
    private String message;
    private T data;
}
