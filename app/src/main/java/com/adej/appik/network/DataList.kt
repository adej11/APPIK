package com.adej.appik.network

import com.adej.appik.model.mClaim
import com.adej.appik.model.mMember
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class DataList {
    @SerializedName("data")
    @Expose
    private var datas: ArrayList<mClaim>? = null
    fun getResults(): ArrayList<mClaim>? {
        return datas
    }
    @SerializedName("data_member")
    @Expose
    private var data_member: ArrayList<mMember>? = null
    fun getDataMember(): ArrayList<mMember>? {
        return data_member
    }
}