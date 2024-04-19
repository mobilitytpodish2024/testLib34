package com.tpodisha.gcmaqa.webservice;

import com.google.gson.JsonObject;
import com.tpodisha.modal.ModalResponseVersion;


import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;

public interface ApiInterface {

    @POST("getUrlResp")
    Call<ModalResponseVersion> checkVersion(@Body JsonObject object);



}
