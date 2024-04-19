package com.tpodisha.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class ModalResponseVersion implements Serializable
{

    @SerializedName("response")
    @Expose
    private Response response;
    private final static long serialVersionUID = -2852661756625704527L;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
