package com.adej.appik.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.adej.appik.R
import com.adej.appik.model.mClaim
import com.adej.appik.network.DataApi
import com.adej.appik.util.AppPreference
import com.adej.appik.util.Constant
import com.adej.appik.util.GeneralFunction
import com.adej.appik.util.GlobalFunction
import kotlinx.android.synthetic.main.form_input.*
import kotlinx.android.synthetic.main.form_input.btn_submit
import kotlinx.android.synthetic.main.form_login.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ClaimActivity : AppCompatActivity() {
    private val causes = arrayOf("Bencana Alam", "Wabah Penyakit", "Lainnya")
    val policyNumber: String = ""
    private lateinit var dataApi: DataApi
    private lateinit var appPreference: AppPreference
    private lateinit var generalFunction: GeneralFunction
    private lateinit var globalFunction: GlobalFunction
    var filePath1 = "NONE"
    var filePath2 = "NONE"
    var fileName1 = "NONE"
    var fileName2 = "NONE"
    val PICK_IMAGE1 = 1
    val PICK_IMAGE2 = 2
    private lateinit var imageUri: Uri
    private var cameraIntent: Intent? = null
    var directoryPath = Environment.getExternalStorageDirectory()
        .toString() + File.separator + "appik"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_input)
        report_date.inputType = InputType.TYPE_NULL
        generalFunction = GeneralFunction()
        globalFunction = GlobalFunction()
        dataApi = DataApi(this)
        appPreference = AppPreference(this)
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        tv_data.text = appPreference.user
        report_date.setOnClickListener {

            val picker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    report_date.setText(
                        year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                    )
                }, year, month, day
            )
            picker.show()

        }
        incident_date.setOnClickListener {

            val picker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    incident_date.setText(
                        year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                    )
                }, year, month, day
            )
            picker.show()
        }

        val adapterStatus = ArrayAdapter(
            this,
            R.layout.spinner_item, causes
        )
        spinner_causes.adapter = adapterStatus
        spinner_causes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>, arg1: View,
                arg2: Int, arg3: Long
            ) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub
            }
        }


        btn_photo.setOnClickListener {
            fileName1 = (System.currentTimeMillis().toString() + ".jpg")
            openGallery(PICK_IMAGE1)
        }
        btn_attach.setOnClickListener {
            fileName2 = (System.currentTimeMillis().toString() + ".jpg")
            openGallery(PICK_IMAGE2)
        }

        btn_submit.setOnClickListener {
               if (description_loses.text.toString().isEmpty() || incident_date.text.toString()
            .isEmpty() || report_date.text.toString().isEmpty()||
             chronology.text.toString().isEmpty() || reporter_name.text.toString().isEmpty()
            ) {
            toast("Silahkan Lengkapi data Anda!")
            } else {
            val appPreference = AppPreference(this)
            val generalFunction = GeneralFunction()


            val data = mClaim(
                "",
                appPreference.user, report_date.text.toString(),
                reporter_name.text.toString(),
                reporter_phone.text.toString(),
                reporter_address.text.toString(),
                incident_date.text.toString(),
                incident_location.text.toString(),
                postal_code.text.toString(),
                spinner_causes.selectedItem.toString(),
                chronology.text.toString(),
                insured_amount.text.toString(),
                affected_area.text.toString(),
                description_loses.text.toString(),
                filePath1,
                filePath2,
                "0", "0"
            )
            val intent = Intent(this, PreviewClaim::class.java)
            intent.putExtra("data", data)
            intent.putExtra("type", "input")
            startActivity(intent)

          }
        }
    }

    private fun openGallery(pick: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            pick
        )
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectImageUri1 = data?.data
        if (resultCode == Activity.RESULT_OK) {
            if ( requestCode == PICK_IMAGE1) {
                try {
                    filePath1 = if (requestCode == PICK_IMAGE1) globalFunction.copyPhoto(applicationContext,selectImageUri1, fileName1)
                    else
                        directoryPath + File.separator + fileName1
                    globalFunction.decodeUri(applicationContext,filePath1,"")

                } catch (ignored: Exception) {
                }
                tv_file1.text = fileName1
            } else if ( requestCode == PICK_IMAGE2) {
                try {
                    filePath2 = if (requestCode == PICK_IMAGE2) {
                        globalFunction.copyPhoto(applicationContext,selectImageUri1, fileName2)
                    } else {
                        directoryPath + File.separator + fileName2
                    }
                    globalFunction.decodeUri(applicationContext,filePath2, fileName2)
                } catch (ignored: Exception) {
                }
                tv_file2.text = fileName2
            }
        }
    }

}