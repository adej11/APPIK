package com.adej.appik.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class mMember(
    @SerializedName("id_policy")
    @Expose
    open var id: String? = null,
    @SerializedName("registrant_name")
    @Expose
    open var registrant_name: String? = null,
    @SerializedName("identity_number")
    @Expose
    open var identity_number: String? = null,
    @SerializedName("address")
    @Expose
    open var address: String? = null,
    @SerializedName("phone_number")
    @Expose
    open var phone_number: String? = null,
    @SerializedName("dis_name")
    @Expose
    open var dis_name: String? = null,
    @SerializedName("subdis_name")
    @Expose
    open var subdis_name: String? = null,
    @SerializedName("comodity_type")
    @Expose
    open var comodity_type: String? = null,
    @SerializedName("insured_price")
    @Expose
    open var insured_price: String? = null,
    @SerializedName("land_boundary")
    @Expose
    open var land_boundary: String? = null,
    @SerializedName("premi")
    @Expose
    open var premi: String? = null,
    @SerializedName("create_datetime")
    @Expose
    open var create_datetime: String? = null

)