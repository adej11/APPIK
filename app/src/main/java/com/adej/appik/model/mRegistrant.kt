package com.adej.appik.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class mRegistrant (
    @SerializedName("id_registrant")
    @Expose
    open var id:  String? = null,
    @SerializedName("registrant_name")
    @Expose
    open var registrantName:  String? = null,
    @SerializedName("id_policy")
    @Expose
    open var idPolicy:  String? = null,
    @SerializedName("identity_number")
    @Expose
    open var identityNumber:  String? = null,
    @SerializedName("phone_number")
    @Expose
    open var phoneNumber:  String? = null,
    @SerializedName("province")
    @Expose
    open var province:  String? = null,
    @SerializedName("city")
    @Expose
    open var city:  String? = null,
    @SerializedName("district")
    @Expose
    open var district:  String? = null,
    @SerializedName("subdistrict")
    @Expose
    open var subdistrict:  String? = null,
    @SerializedName("group_name")
    @Expose
    open var groupName:  String? = null
)