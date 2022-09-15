package com.adej.appik.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adej.appik.R
import com.adej.appik.network.DataApi
import com.adej.appik.util.AppPreference
import com.adej.appik.util.GeneralFunction
import com.adej.appik.util.RetrofitResultCallBack
import kotlinx.android.synthetic.main.form_login.*
import org.jetbrains.anko.toast

class Authentication  : AppCompatActivity() {
    private lateinit var dataApi: DataApi
    private lateinit var appPreference: AppPreference
    private lateinit var generalFunction: GeneralFunction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_login)

        btn_register.setOnClickListener{
            startActivity(Intent(applicationContext, Register::class.java))
        }
        btn_submit.setOnClickListener {
            generalFunction = GeneralFunction()
            dataApi = DataApi(this)
            appPreference = AppPreference(this)

            var username = username.text.toString()
            var password = generalFunction.md5(password.text.toString())
            dataApi.fnLogin(username, password, object : RetrofitResultCallBack {
                override fun onSuccess(value: String) {
                    if (value.contains("success")) {
                        val separated =
                            value!!.split(":".toRegex()).dropLastWhile({ it.isEmpty() })
                                .toTypedArray()
                        try {
                            appPreference.user = separated[1].trim { it <= ' ' }
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        } catch (e: Exception) {
                            runOnUiThread {
                                toast(value)
                            }

                        }
                    }

                }

                override fun onError(value: String) {
                    toast(value)
                }
            })
        }

    }
}