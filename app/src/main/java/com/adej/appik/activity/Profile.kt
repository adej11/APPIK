package com.adej.appik.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adej.appik.R
import com.adej.appik.model.ClaimAdapter
import com.adej.appik.network.DataApi
import com.adej.appik.network.DataList
import com.adej.appik.util.AppPreference
import com.adej.appik.util.RetrofitResultCallBack
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Profile : AppCompatActivity() {

    private lateinit var claimAdapter: ClaimAdapter
    private lateinit var appPreference : AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        appPreference= AppPreference(this)

        showData()

    }

    private fun showData() {
        val dataApi = DataApi(this)
        dataApi.fnGetData(appPreference.user,"fnGetDataMemberWeb","data:null", object :
            RetrofitResultCallBack {

            override fun onSuccess(value: String) {
                if(value.isNotEmpty()) {
                    val separated =
                        value!!.split("::::".toRegex()).dropLastWhile({ it.isEmpty() })
                            .toTypedArray()
                    profile_data.text = separated[0].trim { it <= ' ' }.fromHtml()
                    insurance_data.text = separated[1].trim { it <= ' ' }.fromHtml()
                }
            /**   val g = Gson()
                val p = g.fromJson<DataList>(value.toString(), DataList::class.java)
                val data  = p.getDataMember()!!.get(0)
                var profileData = "<b>Nama :</b>" + data.registrant_name+
                        "<br> <b> NIK : </b>" + data.identity_number +
                        "<br> <b>No.Handphone :</b>" + data.phone_number +
                        "<br> <b>Kecamatan :</b>" + data.dis_name +
                        "<br> <b>Kelurahan :</b>" + data.subdis_name +
                        "<br> <b>Alamat :</b>" + data.address
                val formatter = SimpleDateFormat("yyyy-MM-dd")

                val date = formatter.parse(data.create_datetime)
        // val jwp = dateFormat.format(data.create_datetime)+" sampai "
                var insuranceData = "<b>Komoditas :</b>" + data.comodity_type+
                        "<br> <b> Luas Lahan : </b>" + data.land_boundary +
                        "<br> <b>Harga Pertanggungan :</b>" + data.insured_price +
                        "<br> <b>Premi :</b>" + data.premi+
                        "<br> <b>JWP :</b>"   + date

                profile_data.text=profileData.fromHtml()
                insurance_data.text=insuranceData.fromHtml()

            **/
            }

            override fun onError(value: String) {
                runOnUiThread {
                //    progress_bar.visibility= View.GONE
                    toast(value)
                }
            }

        })

    }
    fun String.fromHtml(): Spanned {
        val htmlStr = replace("\n", "<br />")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlStr, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlStr)
        }
    }
}