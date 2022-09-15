package com.adej.appik.network

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiRest {

    @FormUrlEncoded
    @POST("web_service.php")
    fun fnLogin(
        @Field("USER_NAME") username: String?,
        @Field("PASSWORD") password: String?,
        @Field("F_ACTION") action: String?,
        @Field("CODE") code: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("web_service.php")
    fun fnRegister(
        @Field("ID") id: String?,
        @Field("PASSWORD") password: String?,
        @Field("IDENTITY_NUMBER") identityNumber: String?,
        @Field("PHONE_NUMBER") phoneNumber: String?,
        @Field("F_ACTION") action: String?,
        @Field("CODE") code: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("web_service.php")
    fun getData(
        @Field("ID_POLICY") username: String?,
        @Field("F_ACTION") action: String?,
        @Field("CODE") code: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("web_service.php")
    fun getDataApprovalClaim(
        @Field("ID_CLAIM") username: String?,
        @Field("F_ACTION") action: String?,
        @Field("CODE") code: String?
    ): Call<String?>?
    @Multipart
    @POST("web_service.php")
    fun upploadFile(
        @Part("F_ACTION") action: String?,
        @Part("id") id: String?,
        @Part("report_date") report_date: String?,
        @Part("reporter_name") reporter_name: String?,
        @Part("phone_number") phone_number: String?,
        @Part("event_date") event_date: String?,
        @Part("incident_place") incident_place: String?,
        @Part("postal_code") postal_code: String?,
        @Part("cause_of_incident") cause_of_incident: String?,
        @Part("chronology_incident") chronology_incident: String?,
        @Part("affected_area") affected_area: String?,
        @Part("insured_value") insured_value: String?,
        @Part("description") description: String?,
        @Part("user_id") user_id: String?,
        @Part file: MultipartBody.Part,
        @Part file1: MultipartBody.Part
    ): Call<String>

}