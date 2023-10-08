package com.social.socialserviceapp.model.enums;

import lombok.Getter;

@Getter
public enum ReactStatus {

    LIKE("liked"),
    UNLIKE("unliked");

    private final String react;

    ReactStatus(String react){
        this.react = react;
    }
}
