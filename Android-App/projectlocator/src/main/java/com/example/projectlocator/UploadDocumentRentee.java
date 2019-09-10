package com.example.projectlocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitServiceHouseOwner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocumentRentee extends AppCompatActivity {

    String userId;
    HashMap<String, String> documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document_rentee);
        userId = (getIntent().getStringExtra("id"));
        documents = new HashMap<String, String>();
    }

    public void uploadValidIdDocument(final View v)
    {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(getApplicationContext(), "Request code:" + requestCode, Toast.LENGTH_LONG).show();
        if (requestCode >= 997 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            switch (requestCode)
            {
                case 999:
                    ImageView validId = (ImageView) findViewById(R.id.register_rentee_upload_valid_id);
                    validId.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    validId.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    documents.put("validID",picturePath);
                    break;
            }

        }

    }

    public void uploadImage(View v)
    {

        if(documents.size() == 1) {
            Toast.makeText(getApplicationContext(), "Uploading .....", Toast.LENGTH_LONG).show();

            for (String i : documents.keySet()) {
                System.out.println("key: " + i + " value: " + documents.get(i));

                File file = new File(documents.get(i));
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                RequestBody user =
                        RequestBody.create(MediaType.parse("multipart/form-data"), userId + "");
                RequestBody type =
                        RequestBody.create(MediaType.parse("multipart/form-data"), i + "");
                RetrofitServiceHouseOwner mService;
                SharedPreferences sharedpreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                ApiUtils.BASE_URL = "http://" + sharedpreferences.getString("SERVER", null);
                mService = ApiUtils.getHomeOwnerService();

                Call<ResponseBody> call = mService.upload(user, body, type);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        Log.v("Upload", "success");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                        Toast.makeText(getApplicationContext(), "Uploading error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            Toast.makeText(getApplicationContext(), "Uploading Image:Success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),SuccessRegistrationActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Incomplete documents attached", Toast.LENGTH_LONG).show();
        }
    }


}
