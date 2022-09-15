package com.adej.appik.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.core.app.ActivityCompat;

import static androidx.core.content.ContextCompat.checkSelfPermission;

//import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by adej on 11/2/2017.
 */

public class GlobalFunction {

    public GlobalFunction() {
    }

    public static String getSerial() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }


    public String str_pad(String input, int length, String pad, String sense) {
        int resto_pad = length - input.length();
        String padded = "";

        if (resto_pad <= 0) {
            return input;
        }

        if (sense.equals("STR_PAD_RIGHT")) {
            padded = input;
            padded += _fill_string(pad, resto_pad);
        } else if (sense.equals("STR_PAD_LEFT")) {
            padded = _fill_string(pad, resto_pad);
            padded += input;
        } else // STR_PAD_BOTH
        {
            int pad_left = (int) Math.ceil(resto_pad / 2);
            int pad_right = resto_pad - pad_left;

            padded = _fill_string(pad, pad_left);
            padded += input;
            padded += _fill_string(pad, pad_right);
        }
        return padded;
    }

    protected String _fill_string(String pad, int resto) {
        boolean first = true;
        String padded = "";

        if (resto >= pad.length()) {
            for (int i = resto; i >= 0; i = i - pad.length()) {
                if (i >= pad.length()) {
                    if (first) {
                        padded = pad;
                    } else {
                        padded += pad;
                    }
                } else {
                    if (first) {
                        padded = pad.substring(0, i);
                    } else {
                        padded += pad.substring(0, i);
                    }
                }
                first = false;
            }
        } else {
            padded = pad.substring(0, resto);
        }
        return padded;
    }

    public String getModel() {
        String model = "unknown";
        model = Build.MANUFACTURER + Build.MODEL;

        if (model == " ") {
            model = "unknown";
        } else {
            if (model.length() > 30) {
                model = Build.MANUFACTURER;
            }
        }

        return model;
    }

    public String getNumberKey() {
        String vrNum = "";
        Random r = new Random();
        int i1 = r.nextInt(999999999 - 199999999) + 199999999;
        vrNum = String.valueOf(i1);
        return vrNum;
    }

    public String stringEncryption(String id, String key) {
        String vrResultKey = "";
        int i = 0, j = 0;
        String[] vrArrPairID = {"A", "D", "E", "L", "I", "S", "U", "W", "N", "T"};
        String[] vrArrPairKey = {"W", "U", "N", "T", "D", "E", "A", "L", "I", "S"};
        String vrResultID = "";
        for (i = 0; i < id.length(); i++) {
            vrResultID += vrArrPairID[Integer.parseInt(id.substring(i, i + 1))];
        }
        for (j = 0; j < key.length(); j++) {
            vrResultKey += vrArrPairKey[Integer.parseInt(key.substring(j, j + 1))];
        }

        vrResultID = md5(vrResultID + vrResultKey + key + "ALADSU");

        return vrResultID;
    }


    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        String result = "";
        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        result = elapsedDays + "d " + elapsedHours + " j";
        return result;
    }


    public String md5(final String password) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void createDirectory() {
        File mFile = null;
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // handle case of no SDCARD present
        } else {
            mFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "appik");
            if (!mFile.exists()) {
                mFile.mkdir();
            }
        }
        if (!mFile.exists()) {
            mFile.mkdir();
        }
    }

    public void decodeUri(Context mContext, String filePath, String nama) throws FileNotFoundException {
        // Decode image size
        Bitmap bitmap = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        Uri uriFromPath = Uri.fromFile(new File(filePath));
        BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(
                uriFromPath));
        // The new size we want to scale to
        // final int REQUIRED_SIZE = 70;
        // Find the correct scale value. It should be the power of 2.
        ///   int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        //  while (true) {
        //    if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
        //      break;
        //   width_tmp /= 2;
        // height_tmp /= 2;
        scale *= 3;
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int second = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String Date = mDay + "/" + mMonth + "/" + mYear + " " + hour + ":" + minute + ":" + second;
        bitmap = BitmapFactory.decodeStream(mContext.getContentResolver()
                .openInputStream(uriFromPath), null, o2);
        String nm = " - " + nama + "-" + Date;
        String watermark = nama + "_" + Date;
        // bitmap=waterMark(bitmap, dept, p, Color.RED, 90, 30, true);

        Bitmap drawableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(drawableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(15);
        // paint.setTextSize((int) (14 * scale));
        Rect bounds = new Rect();
        paint.getTextBounds(nm, 0, nm.length(), bounds);
        int x = 20;
        int y = bitmap.getHeight() - 60;
        System.out.println(x + "-" + y);
        // canvas.drawText(watermark, x, y, paint);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        drawableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File f = new File(filePath);
        try {
            f.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // write the bytes in file
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fo.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String copyPhoto(Context mContext, Uri imageUri, String fileName) {
        createDirectory();
        String filePath = "NONE";
        Uri selectImageUri1 = imageUri, selectedImageUri2 = null;
        try {
            String directoryPath1 = "sdcard/" + "appik";
            File file1 = new File(directoryPath1, fileName);
            Uri imageUri1 = Uri.fromFile(file1);
            selectedImageUri2 = imageUri1;
            // InputStream in = new FileInputStream(src);
            InputStream in = mContext.getContentResolver().openInputStream(
                    Objects.requireNonNull(selectImageUri1));
            // OutputStream out = new FileOutputStream(dest);
            OutputStream out = mContext.getContentResolver().openOutputStream(
                    selectedImageUri2);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = Objects.requireNonNull(in).read(buf)) > 0) {
                Objects.requireNonNull(out).write(buf, 0, len);
            }
            in.close();
            Objects.requireNonNull(out).close();

        } catch (Exception ignored) {
        }
        if (selectedImageUri2 != null) {
            try {
                // OI FILE Manager
                String filemanagerstring1 = selectedImageUri2.getPath();
                // MEDIA GALLERY
                String selectedImagePath1 = getPath(mContext, selectedImageUri2);

                if (selectedImagePath1 != null) {
                    filePath = selectedImagePath1;
                } else if (filemanagerstring1 != null) {
                    filePath = filemanagerstring1;
                } else {

                    Log.e("Bitmap", "Unknown path");
                }
            } catch (Exception e) {
            }

        }
        return filePath;
    }

    private String getPath(Context mContext, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File("/sdcard/Foto Meter");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            gpxfile.createNewFile();
            FileInputStream fIn = new FileInputStream(
                    gpxfile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {

                aBuffer += aDataRow + " \n";
            }
            // txtData.setText(aBuffer);

            final FileOutputStream fOut = new FileOutputStream(
                    gpxfile);
            final OutputStreamWriter myOutWriter = new OutputStreamWriter(
                    fOut);

            myOutWriter.append(aBuffer);
            myOutWriter.append(sBody);

            myOutWriter.close();
            fOut.close();

            //  Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPermisionAndroidM(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 10);
            }

        }

    }
}