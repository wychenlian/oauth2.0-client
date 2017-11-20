package com.hybris.caas.oauth.client.model;

import com.hybris.caas.oauth.client.model.enums.GrantTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by I341534 on 11/10/2017.
 */
@Data
public class AccessTokenGetRequest {
    @NotNull
    private GrantTypeEnum grantTypeEnum;

    private String refreshCode;
    private String clientId;
    private String code;
}
