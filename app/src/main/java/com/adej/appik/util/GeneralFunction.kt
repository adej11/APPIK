package com.adej.appik.util

import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.content.FileProvider
import java.io.*
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

class GeneralFunction() {
    var mContext: Context? = null
    var mFile: File? = null
    fun GeneralFunction(context: Context) {
        this.mContext = context

    }

    fun md5(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(password.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuffer()
            for (i in messageDigest.indices) {
                var h: String
                h = Integer.toHexString(255 and messageDigest[i].toInt())
                while (h.length < 2) {
                    h = "0$h"
                }
                hexString.append(h)
            }
            hexString.toString()
        } catch (var7: NoSuchAlgorithmException) {
            var7.printStackTrace()
            ""
        }
    }

    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = mContext?.getContentResolver()?.query(uri, projection, null, null, null)!!
        return if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }
    fun createDirectory() {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            // handle case of no SDCARD present
        } else {
            mFile = File(
                Environment.DIRECTORY_DOCUMENTS
                    .toString() + File.separator + "appik"
            )
            if (!mFile!!.exists()) {
                mFile!!.mkdir()
            }
        }
        if (!mFile!!.exists()) {
            mFile!!.mkdir()
        }
    }

    fun copyPhoto(imageUri: Uri?, fileName: String?): String? {
        createDirectory()
       var filePath : String = "NONE"
        var selectedImageUri2: Uri? = null
        try {
            //val directoryPath1 = "sdcard/" + "appik"
            val directoryPath1 = Environment.DIRECTORY_DOCUMENTS
                .toString() + File.separator + "appik"
            val file1 = File(directoryPath1, fileName)
            val imageUri1 = Uri.fromFile(file1)
            selectedImageUri2 = imageUri1
            // InputStream in = new FileInputStream(src);
            val `in`: InputStream? = mContext?.getContentResolver()?.openInputStream(
                Objects.requireNonNull(imageUri)!!
            )
            // OutputStream out = new FileOutputStream(dest);
            val out: OutputStream = mContext?.getContentResolver()?.openOutputStream(
                selectedImageUri2
            )!!
            // Transfer bytes from in to out
            val buf = ByteArray(1024)
            var len: Int
            while (Objects.requireNonNull(`in`)!!.read(buf).also {
                    len = it
                } > 0) {
                Objects.requireNonNull(out).write(buf, 0, len)
            }
            `in`!!.close()
            Objects.requireNonNull(out).close()
        } catch (ignored: Exception) {
        }
        if (selectedImageUri2 != null) {
            try {
                // OI FILE Manager
                val filemanagerstring1 = selectedImageUri2.path
                // MEDIA GALLERY
                val selectedImagePath1: String = getPath(selectedImageUri2)!!
                if (selectedImagePath1 != null) {
                    filePath = selectedImagePath1
                } else if (filemanagerstring1 != null) {
                    filePath = filemanagerstring1
                } else {
                    Log.e("Bitmap", "Unknown path")
                }
            } catch (e: Exception) {
            }
        }
        return filePath
    }
    val formattedDate: String
        get() {
            val c = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd")
            return df.format(c.time)
        }
    fun String.fromHtml(): Spanned {
        val htmlStr = replace("\n", "<br />")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlStr, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlStr)
        }
    }

    @Throws(FileNotFoundException::class)
    fun decodeUri(filePath: String?, nama: String) {
        var bitmap: Bitmap? = null
        // Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        val uriFromPath = Uri.fromFile(File(filePath))
        BitmapFactory.decodeStream(
            mContext!!.contentResolver.openInputStream(
                uriFromPath
            )
        )
        // The new size we want to scale to
        // final int REQUIRED_SIZE = 70;
        // Find the correct scale value. It should be the power of 2.
        ///   int width_tmp = o.outWidth, height_tmp = o.outHeight;
        var scale = 1
        //  while (true) {
        //    if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
        //      break;
        //   width_tmp /= 2;
        // height_tmp /= 2;
        scale *= 3
        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH] + 1
        val mDay = c[Calendar.DAY_OF_MONTH]
        val second = c[Calendar.SECOND]
        val minute = c[Calendar.MINUTE]
        val hour = c[Calendar.HOUR_OF_DAY]
        val Date = "$mDay/$mMonth/$mYear $hour:$minute:$second"
        bitmap = BitmapFactory.decodeStream(
            mContext!!.contentResolver
                .openInputStream(uriFromPath), null, o2
        )
        val nm = " - $nama-$Date"
        val watermark = nama + "_" + Date
        // bitmap=waterMark(bitmap, dept, p, Color.RED, 90, 30, true);
        val drawableBitmap: Bitmap = bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(drawableBitmap)
        val paint = Paint()
        paint.color = Color.RED
        paint.textSize = 15f
        // paint.setTextSize((int) (14 * scale));
        val bounds = Rect()
        paint.getTextBounds(nm, 0, nm.length, bounds)
        val x = 20
        val y: Int = bitmap!!.getHeight() - 60
        println("$x-$y")
        canvas.drawText(watermark, x.toFloat(), y.toFloat(), paint)
        val bytes = ByteArrayOutputStream()
        drawableBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes)
        val f = File(filePath)
        try {
            f.createNewFile()
        } catch (e1: IOException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        // write the bytes in file
        var fo: FileOutputStream? = null
        try {
            fo = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        try {
            fo!!.write(bytes.toByteArray())
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        try {
            fo!!.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

}
