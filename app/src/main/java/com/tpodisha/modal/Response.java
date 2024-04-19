package com.tpodisha.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("apiResponse")
    @Expose
    private Apiresponse apiresponse;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    private final static long serialVersionUID = -1680107675999511218L;

    public Apiresponse getApiresponse() {
        return apiresponse;
    }

    public void setApiresponse(Apiresponse apiresponse) {
        this.apiresponse = apiresponse;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
