package com.hybris.caas.oauth.client.model.enums;

/**
 * Created by I341534 on 11/10/2017.
 */
public enum GrantTypeEnum {
    CODE("code"),REFRESh_TOKEN("refresh_token");

    private String code;

    GrantTypeEnum(String code) {
        this.code = code;
    }
}
