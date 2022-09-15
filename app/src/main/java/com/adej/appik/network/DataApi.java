package com.adej.appik.network;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.adej.appik.util.AppPreference;
import com.adej.appik.util.RetrofitResultCallBack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DataApi {
    Context mContext;
    AppPreference appPreference;
    String URL = "https://addevstudio.com/apik/api/";

    public DataApi(Context context) {
        this.mContext = context;
        appPreference = new AppPreference(mContext);
    }

    public void fnRegister(String idPolicy, String password,String identityNo,String phoneNo, final RetrofitResultCallBack callBack) {
        ApiRest apiRest = getClient(URL).create(ApiRest.class);
        Call<String> authorized = apiRest.fnRegister(idPolicy, password,identityNo, phoneNo,"fnRegister", "");
        authorized.enqueue(new Callback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onError(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                callBack.onError(throwable.toString());
            }
        });
    }

    public void fnUploadImage(String path1, String path2,
                              String id,
                              String report_date,
                              String reporter_name,
                              String phone_number,
                              String event_date,
                              String incident_place,
                              String postal_code,
                              String cause_of_incident,
                              String chronology_incident,
                              String affected_area,
                              String insured_value,
                              String description,
                              String user_id,
                              final RetrofitResultCallBack callBack) {

        //Create a file object using file path
        File file = new File(path1);
        File file1 = new File(path2);
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*//**"), file);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*//**"), file1);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("uploadedfile1", file.getName(), requestBody1);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("uploadedfile2", file1.getName(), requestBody2);

        ApiRest apiRest = getClient(URL).create(ApiRest.class);
        Call<String> authorized = apiRest.upploadFile("fnUpload",id,report_date,reporter_name,
                phone_number,event_date,incident_place,postal_code,cause_of_incident,chronology_incident,
                affected_area,insured_value,description,user_id,fileToUpload1, fileToUpload2);

        authorized.enqueue(new Callback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onError(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                callBack.onError(throwable.toString());
            }
        });

    }


    public void fnLogin(String username, String password, final RetrofitResultCallBack callBack) {
        ApiRest apiRest = getClient(URL).create(ApiRest.class);
        Call<String> authorized = apiRest.fnLogin(username, password, "fnLogin", "");
        authorized.enqueue(new Callback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onError(response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                callBack.onError(throwable.toString());
            }
        });
    }


    public void fnGetData(String policyNumber, String function, final String result, final RetrofitResultCallBack callBack) {
        ApiRest apiRest = getClient(URL).create(ApiRest.class);
        Call<String> authorized = apiRest.getData(policyNumber, function, "");
        authorized.enqueue(new Callback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (!Objects.requireNonNull(response.body()).contains(result)) {
                        callBack.onSuccess(response.body());
                    } else {
                        callBack.onError(response.body());
                    }

                } else {
                    callBack.onError(response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                callBack.onError(throwable.toString());
            }
        });
    }
    public void fnGetDataClaim(String idClaim, String function, final String result, final RetrofitResultCallBack callBack) {
        ApiRest apiRest = getClient(URL).create(ApiRest.class);
        Call<String> authorized = apiRest.getDataApprovalClaim(idClaim, function, "");
        authorized.enqueue(new Callback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (!Objects.requireNonNull(response.body()).contains(result)) {
                        callBack.onSuccess(response.body());
                    } else {
                        callBack.onError(response.body());
                    }

                } else {
                    callBack.onError(response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                callBack.onError(throwable.toString());
            }
        });
    }

    public static Retrofit getClient(String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = null;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}

