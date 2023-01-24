package com.juno.normalapi.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponseError<T> {
    private ResponseCode code;
    private String message;
    private List<T> errors;
}
