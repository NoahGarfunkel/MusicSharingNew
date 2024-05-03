package com.example.musicsharing.retrofit.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountsApi {
    @GET("authorize")
    fun requestAuth(@Query(("client_id")) clientID: String,
                    @Query(("response_type")) responseType: String,
                    @Query(("redirect_uri")) redirectURI: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/token")
    fun getToken(@Header("Authorization") authorization: String,
                 @Header("Content-Type") contentType: String,
                 @Field(("grant_type")) grantType:String,
                 @Field(("code")) code:String,
                 @Field(("redirect_uri")) redirect:String
                 ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/token")
    fun getNewToken(@Header("Authorization") authorization: String,
                    @Header("Content-Type") contentType: String,
                    @Field(("grant_type")) grantType:String,
                    @Field(("refresh_token")) refreshToken:String
    ): Call<ResponseBody>
}