package com.arongeorgel.flinggallery.network.event;

/**
 * Generic API error event
 *
 * Created by arongeorgel on 30/01/2015.
 */
public class ApiErrorEvent {
    private String errorMessage;

    public ApiErrorEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
