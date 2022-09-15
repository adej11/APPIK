package com.adej.appik.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adej.appik.R
import com.adej.appik.network.DataApi
import com.adej.appik.util.AppPreference
import com.adej.appik.util.GeneralFunction
import com.adej.appik.util.RetrofitResultCallBack
import kotlinx.android.synthetic.main.form_register.*
import org.jetbrains.anko.toast

class Register : AppCompatActivity() {
    private lateinit var dataApi: DataApi
    private lateinit var appPreference: AppPreference
    private lateinit var generalFunction: GeneralFunction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_register)
        btn_submit.setOnClickListener {

            generalFunction = GeneralFunction()
            dataApi = DataApi(this)
            appPreference = AppPreference(this)

            var noPolis = no_polis.text.toString()
            var phoneNo = phone_number.text.toString()
            var noKtp = identity_no.text.toString()
            var password = generalFunction.md5(password.text.toString())
            dataApi.fnRegister(noPolis, password,noKtp, phoneNo,object : RetrofitResultCallBack {
                override fun onSuccess(value: String) {
                    if (value.contains("success")) {
                        val separated =
                            value!!.split(":".toRegex()).dropLastWhile({ it.isEmpty() })
                                .toTypedArray()
                        try {
                            appPreference.user = separated[1].trim { it <= ' ' }
                            startActivity(Intent(applicationContext, Authentication::class.java))
                            finish()
                            toast("Register Berhasil")

                        } catch (e: Exception) {
                            runOnUiThread {
                                toast(value)
                            }

                        }
                    }else{
                        toast(value)
                    }

                }

                override fun onError(value: String) {
                    toast(value)
                }
            })
        }

    }
}