package com.bizzmark.seller.sellerwithoutlogin.db.Retrofit;

/**
 * Created by Tharun on 06-04-2017.
 */

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface InsertData {

    @FormUrlEncoded
    @POST("/add_store_points.php/")
    public void saveData(@Field("deviceid") String deviceid,
                         @Field("storename") String storename,
                         @Field("billamount") String billamount,
                         @Field("points") String points,
                         @Field("type") String type,
                         @Field("time") String time,
                         Callback<Response> callback);
}
