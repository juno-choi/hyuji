package com.juno.normalapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class EmptyDto implements Serializable {
    private String message;

    public static EmptyDto of(String message){
        return new EmptyDto(message);
    }
}
