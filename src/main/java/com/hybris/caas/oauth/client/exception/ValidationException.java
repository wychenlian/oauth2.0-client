package com.hybris.caas.oauth.client.exception;

import com.google.common.collect.ImmutableList;
import com.hybris.caas.error.YaasErrorConstants;
import com.hybris.caas.error.annotation.YaasException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

@YaasException(status = HttpStatus.BAD_REQUEST, type = YaasErrorConstants.SUB_TYPE_400_INVALID_FIELD)
public class ValidationException extends RuntimeException {
    private static final String DEFAULT_GLOBAL_MSG = "Error validating your data.";
    private final List<FieldError> fieldErrors;

    public ValidationException(final List<FieldError> fieldErrors) {
        super(DEFAULT_GLOBAL_MSG);
        this.fieldErrors = ImmutableList.copyOf(fieldErrors);
    }

    public ValidationException(final List<FieldError> fieldErrors, final String globalMessage) {
        super(globalMessage);
        this.fieldErrors = ImmutableList.copyOf(fieldErrors);
    }

    public ValidationException(final FieldError fieldError) {
        super(DEFAULT_GLOBAL_MSG);
        this.fieldErrors = ImmutableList.copyOf(Collections.singletonList(fieldError));
    }

    public ValidationException(final FieldError fieldError, final String globalMessage) {
        super(globalMessage);
        this.fieldErrors = ImmutableList.copyOf(Collections.singletonList(fieldError));
    }

    public List<FieldError> getFieldErrors() {
        return this.fieldErrors;
    }

    public ValidationException(final String globalMessage) {
        super(globalMessage);
        this.fieldErrors = Collections.emptyList();
    }

}
