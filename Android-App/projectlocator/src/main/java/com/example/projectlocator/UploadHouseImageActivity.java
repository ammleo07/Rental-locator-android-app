package com.example.projectlocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;

import Model.HouseOwnerForm;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitServiceHouseOwner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadHouseImageActivity extends AppCompatActivity {

    String fileSelected;
    HouseOwnerForm ownerForm;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_house_image);
        ownerForm = (HouseOwnerForm) (getIntent().getSerializableExtra("User"));
        Button chooseImage = (Button) findViewById(R.id.choose_image);
        chooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 999);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 999 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            fileSelected=picturePath;
            ImageView imageView = (ImageView) findViewById(R.id.image_Preview);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }

    }

    public void upload(View view)
    {
        uploadImage(fileSelected);
    }

    public void uploadImage(String filepath)
    {
        String transactionID = new SimpleDateFormat("MMddyyHHmmSSss").format(new java.util.Date());
        progressBar = findViewById(R.id.upload_progress);
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), "Uploading .....", Toast.LENGTH_LONG).show();
        final Button chooseImage = (Button) findViewById(R.id.choose_image);
        final Button uploadbtn = (Button) findViewById(R.id.uploadBtn);

        uploadbtn.setEnabled(false);
        chooseImage.setEnabled(false);
        File file = new File(filepath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody userId =
                RequestBody.create(MediaType.parse("multipart/form-data"), ownerForm.getHouse().getId() + "");
        RequestBody fileType =
                RequestBody.create(MediaType.parse("multipart/form-data"), transactionID + "");
        RetrofitServiceHouseOwner mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getHomeOwnerService();


        Call<ResponseBody> call = mService.upload(userId,body,fileType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                Toast.makeText(getApplicationContext(), "Uploading Image:Success", Toast.LENGTH_LONG).show();
                uploadbtn.setEnabled(true);
                chooseImage.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                Toast.makeText(getApplicationContext(), "Uploading error", Toast.LENGTH_LONG).show();
                uploadbtn.setEnabled(true);
                chooseImage.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
