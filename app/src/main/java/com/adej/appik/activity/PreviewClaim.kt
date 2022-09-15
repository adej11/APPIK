package com.adej.appik.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.adej.appik.BuildConfig
import com.adej.appik.R
import com.adej.appik.model.mClaim
import com.adej.appik.network.ApiRest
import com.adej.appik.network.DataApi
import com.adej.appik.network.DataList
import com.adej.appik.util.AppPreference
import com.adej.appik.util.RetrofitResultCallBack
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.form_register.*
import kotlinx.android.synthetic.main.prev_claim.*
import kotlinx.android.synthetic.main.prev_claim.btn_submit
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*




class PreviewClaim  : AppCompatActivity() {
    private lateinit var data: mClaim
    lateinit var type:String
  var extension:String? = null
  var mimetype:String? = null

    private var uri_img: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prev_claim)
        data = intent.getParcelableExtra("data")!!
        type = intent.getStringExtra("type")!!
        val df1 = SimpleDateFormat("yyyy-MM-dd")
        val date1 = df1.parse(  data.reportDate )
        val reportDate = SimpleDateFormat("dd-MM-yyyy").format(date1)
        var customerData = "<b>Tanggal Lapor :</b>" +  reportDate +
                "<br> <b> Nama Pelapor : </b>" + data.reporterName +
                "<br> <b>No.Handphone :</b>" + data.reporterPhoneNumber +
                "<br> <b>Alamat :</b>" + data.reporterAddress

        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = df.parse( data.incidentDate)
        val time = SimpleDateFormat("dd-MM-yyyy").format(date)


        val followUpData =
                "<b>Tanggal Kejadian : </b>" + time +
            "<br> <b>Lokasi Kejadian :</b>" + data.incidentPlace+
            "<br> <b>Penyebab  :</b>" + data.causeOfIncident+
            "<br> <b>Deskripsi :</b>" + data.description+
            "<br> <b>Kronologi :</b>" + data.chronology+
            "<br> <b>Area Terkena :</b>" + data.affectedArea+
            "<br> <b>Jumlah Pertanggungan :</b>" + data.insuredValue


        reporter_data.text = customerData.fromHtml()
        follow_up_data.text = followUpData.fromHtml()
        if(type=="view"){
            imgView.visibility=View.GONE
            imgView1.visibility = View.GONE
            btn_submit.visibility= View.GONE
            layout_data_approval.visibility = View.VISIBLE
            layout_data_payment.visibility = View.VISIBLE
            showDataApproval(data.id!!)
            showDataPayment(data.id!!)
        }

        setImage(data.attachmentUrl1,data.attachmentUrl2)

        btn_submit.setOnClickListener{
            val dataApi = DataApi(this)
            val  appPreference = AppPreference(this)

            dataApi.fnUploadImage(data.attachmentUrl1,data.attachmentUrl2,
                data.policyNumber,data.reportDate,data.reporterName,data.reporterPhoneNumber,
                data.incidentDate,data.incidentPlace,data.postalCode,data.causeOfIncident,
                data.chronology,data.affectedArea,data.insuredValue,data.description,appPreference.user
                ,object : RetrofitResultCallBack {
                override fun onSuccess(value: String) {
                    if (value.contains("success")) {
                        toast("Data Berhasil Terkirim")
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()

                    }

                }

                override fun onError(value: String) {
                    toast(value)
                }
            })
        }
        imgView.setOnClickListener {
            setZoom(data.attachmentUrl1)
            Toast.makeText(
                this, "Please wait..",
                Toast.LENGTH_SHORT
            ).show()
            val i = Intent(Intent.ACTION_VIEW)
            i.setDataAndType(uri_img, mimetype)
            startActivity(i)
        }
        imgView1.setOnClickListener {
            setZoom(data.attachmentUrl2)
            Toast.makeText(
                this, "Please wait..",
                Toast.LENGTH_SHORT
            ).show()
            val i = Intent(Intent.ACTION_VIEW)
            i.setDataAndType(uri_img, mimetype)
            startActivity(i)
        }
    }
    fun String.fromHtml(): Spanned {
        val htmlStr = replace("\n", "<br />")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlStr, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlStr)
        }
    }
    fun setZoom(filePath: String?) {
        val filePaths = File(filePath)
        uri_img = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            filePaths
        )
        extension = MimeTypeMap
            .getFileExtensionFromUrl(uri_img.toString())
        mimetype = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension)
    }
    fun setImage(  filePath: String?,filePath1: String?) {

        try {
            if (!filePath.contentEquals("NONE")) {
                decodeUris(filePath, 1)
                    }
            if (!filePath1.contentEquals("NONE")) {
                decodeUris(filePath1, 2)
                     }
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
    @Throws(FileNotFoundException::class)
    fun decodeUris(filePath: String?, a: Int) {
        var bitmap: Bitmap? =null;
        // Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        val uriFromPath = Uri.fromFile(File(filePath))
        BitmapFactory.decodeStream(
            contentResolver.openInputStream(
                uriFromPath
            )
        )
        // The new size we want to scale to
        val REQUIRED_SIZE = 75

        // Find the correct scale value. It should be the power of 2.
        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        bitmap = BitmapFactory.decodeStream(
            contentResolver
                .openInputStream(uriFromPath), null, o2
        )
        bitmap = bitmap?.let { Bitmap.createScaledBitmap(it, 240, 240, true) }
        if (a == 1) {
            imgView.setImageBitmap(bitmap)
        } else if (a == 2) {
            imgView1.setImageBitmap(bitmap)
        }
    }
    private fun showDataApproval(idClaim:String) {
        val dataApi = DataApi(this)
        dataApi.fnGetDataClaim(idClaim, "fnGetDataApprovalClaim", "data:null", object :
            RetrofitResultCallBack {

            override fun onSuccess(value: String) {
               // toast(value)
                data_approval.text = value.fromHtml()
            }

            override fun onError(value: String) {
                runOnUiThread {
                    toast(value)
                }
            }

        })
    }
    private fun showDataPayment(idClaim:String) {
        val dataApi = DataApi(this)
        dataApi.fnGetDataClaim(idClaim, "fnGetDataPaymentClaim", "data:null", object :
            RetrofitResultCallBack {

            override fun onSuccess(value: String) {
                // toast(value)
                data_payment.text = value.fromHtml()
            }

            override fun onError(value: String) {
                runOnUiThread {
                    toast(value)
                }
            }

        })
    }
}

