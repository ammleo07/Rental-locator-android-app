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
import android.widget.Button;
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

public class UploadDocumentsHouseOwner extends AppCompatActivity {

    String ownerId;
    HashMap<String, String> documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents_house_owner);
        ownerId = (getIntent().getStringExtra("id"));
        documents = new HashMap<String, String>();
    }

    public void uploadValidIdDocument(final View v)
    {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 999);
    }

    public void uploadBusinessPermit(final View v)
    {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 998);
    }

    public void uploadLandTitle(final View v)
    {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 997);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "Request code:" + requestCode, Toast.LENGTH_LONG).show();
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
                case 997:
                    ImageView landTitle = (ImageView) findViewById(R.id.register_house_owner_upload_land_title);
                    //landTitle.setText(picturePath);
                    //Toast.makeText(getApplicationContext(), "Picture Path:" + picturePath, Toast.LENGTH_LONG).show();
                    //uploadImage(picturePath,ownerId,"landTitle");
                    landTitle.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    landTitle.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    documents.put("landTitle",picturePath);
                    break;
                case 998:
                    ImageView businessPermit = (ImageView) findViewById(R.id.register_house_owner_upload_business_permit);
                    //businessPermit.setText(picturePath);
                    //Toast.makeText(getApplicationContext(), "Picture Path:" + picturePath, Toast.LENGTH_LONG).show();
                    //uploadImage(picturePath,ownerId,"businessPermit");
                    businessPermit.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    businessPermit.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    documents.put("businessPermit",picturePath);
                    break;
                case 999:
                    ImageView validId = (ImageView) findViewById(R.id.register_house_owner_upload_valid_id);
                    //validId.setText(picturePath);
                    //Toast.makeText(getApplicationContext(), "Picture Path:" + picturePath, Toast.LENGTH_LONG).show();
                    //uploadImage(picturePath,ownerId,"validID");
                    validId.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    validId.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    documents.put("validID",picturePath);
                    break;
            }

        }

    }

    public void uploadImage(View v)
    {

        if(documents.size() >= 2) {
            Toast.makeText(getApplicationContext(), "Uploading .....", Toast.LENGTH_LONG).show();

            for (String i : documents.keySet()) {
                System.out.println("key: " + i + " value: " + documents.get(i));

                File file = new File(documents.get(i));
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                RequestBody userId =
                        RequestBody.create(MediaType.parse("multipart/form-data"), ownerId + "");
                RequestBody type =
                        RequestBody.create(MediaType.parse("multipart/form-data"), i + "");
                RetrofitServiceHouseOwner mService;
                SharedPreferences sharedpreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                ApiUtils.BASE_URL = "http://" + sharedpreferences.getString("SERVER", null);
                mService = ApiUtils.getHomeOwnerService();

                Call<ResponseBody> call = mService.upload(userId, body, type);
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
