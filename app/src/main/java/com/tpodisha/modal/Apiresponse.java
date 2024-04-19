package com.tpodisha.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Apiresponse implements Serializable
{

    @SerializedName("SOFTWARE_VERSION_SAP")
    @Expose
    private String softwareVersionSap;
    @SerializedName("URLNAME")
    @Expose
    private String urlname;
    private final static long serialVersionUID = 1162041824008590470L;

    public String getSoftwareVersionSap() {
        return softwareVersionSap;
    }

    public void setSoftwareVersionSap(String softwareVersionSap) {
        this.softwareVersionSap = softwareVersionSap;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

}
