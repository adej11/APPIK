package com.adej.appik.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class mClaim(
    @SerializedName("id_claim")
    @Expose
    open var id: String? = null,
    @SerializedName("policy_number")
    @Expose
    open var policyNumber: String? = null,
    @SerializedName("report_date")
    @Expose
    open var reportDate: String? = null,
    @SerializedName("reporter_name")
    @Expose
    open var reporterName: String? = null,
    @SerializedName("reporter_phone_number")
    @Expose
    open var reporterPhoneNumber: String? = null,
    @SerializedName("reporter_address")
    @Expose
    open var reporterAddress: String? = null,
    @SerializedName("incident_date")
    @Expose
    open var incidentDate: String? = null,
    @SerializedName("incident_place")
    @Expose
    open var incidentPlace: String? = null,
    @SerializedName("postal_code")
    @Expose
    open var postalCode: String? = null,
    @SerializedName("cause_of_incident")
    @Expose
    open var causeOfIncident: String? = null,
    @SerializedName("chronology_of_incident")
    @Expose
    open var chronology: String? = null,
    @SerializedName("insured_value")
    @Expose
    open var insuredValue: String? = null,

    @SerializedName("affected_area")
    @Expose
    open var affectedArea: String? = null,
    @SerializedName("description")
    @Expose
    open var description: String? = null,
    @SerializedName("attachment_url_1")
    @Expose
    open var attachmentUrl1: String? = null,
    @SerializedName("attachment_url_2")
    @Expose
    open var attachmentUrl2: String? = null,
    @SerializedName("latitude")
    @Expose
    open var latitude: String? = null,
    @SerializedName("longitude")
    @Expose
    open var longitude: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(policyNumber)
        parcel.writeString(reportDate)
        parcel.writeString(reporterName)
        parcel.writeString(reporterPhoneNumber)
        parcel.writeString(reporterAddress)
        parcel.writeString(incidentDate)
        parcel.writeString(incidentPlace)
        parcel.writeString(postalCode)
        parcel.writeString(causeOfIncident)
        parcel.writeString(chronology)
        parcel.writeString(insuredValue)
        parcel.writeString(affectedArea)
        parcel.writeString(description)
        parcel.writeString(attachmentUrl1)
        parcel.writeString(attachmentUrl2)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<mClaim> {
        override fun createFromParcel(parcel: Parcel): mClaim {
            return mClaim(parcel)
        }

        override fun newArray(size: Int): Array<mClaim?> {
            return arrayOfNulls(size)
        }
    }
}