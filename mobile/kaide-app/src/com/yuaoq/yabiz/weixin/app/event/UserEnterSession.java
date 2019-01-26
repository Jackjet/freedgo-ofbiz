package com.yuaoq.yabiz.weixin.app.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.yuaoq.yabiz.weixin.common.event.EventRequest;

/**
 * @borball on 12/29/2016.
 */
public class UserEnterSession extends EventRequest {

    @JsonProperty("SessionFrom")
    @JacksonXmlCData
    private String sessionFrom;

    public String getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        this.sessionFrom = sessionFrom;
    }
}
