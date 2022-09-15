package com.adej.appik.util

import android.os.Environment
import java.io.File

public class Constant {
    var URL="https://addevstudio.com/apik/api"
    var directoryPath = Environment.getExternalStorageDirectory()
        .toString() + File.separator + "appik"

}