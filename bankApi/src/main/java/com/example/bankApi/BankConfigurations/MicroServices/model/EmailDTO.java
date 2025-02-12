package com.example.bankApi.BankConfigurations.MicroServices.model;

import java.util.UUID;

public class EmailDTO {

    /**
     * A class used as an Interface DTO ( Data To Object ),
     * used to create manipulate data from it's original format to
     * EmailModel, as seen in others Models in this API
     */

    private UUID userId;
    private String emailTo;
    private String subject;
    private String text;

    /**
     * GETTERS / SETTERS :
     * Important to better mobility from passing data,
     * sending and setting new data.
     */

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

