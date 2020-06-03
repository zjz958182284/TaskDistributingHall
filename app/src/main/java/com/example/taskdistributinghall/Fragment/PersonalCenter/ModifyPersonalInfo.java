package com.example.taskdistributinghall.Fragment.PersonalCenter;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taskdistributinghall.Activity.Login;
import com.example.taskdistributinghall.Activity.MainActivity;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;
import com.shehuan.niv.NiceImageView;

import java.io.File;
import java.sql.SQLException;
import java.util.zip.Inflater;

public class ModifyPersonalInfo extends AppCompatActivity {
    private Uri imageUri;
    NiceImageView niceImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_personal_info);
        loadData();
        Button button=findViewById(R.id.ok_btn);
        niceImageView=findViewById(R.id.profile_photo);
        RadioGroup radioGroup=findViewById(R.id.gender_radio_group);
        //打开相册
        niceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
        }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender=((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                String name=((EditText)findViewById(R.id.name_edit)).getText().toString();
                String address=((EditText)findViewById(R.id.address_edit)).getText().toString();
                String grade=((EditText)findViewById(R.id.grade_edit)).getText().toString();
                String dept=((EditText)findViewById(R.id.dept_edit)).getText().toString();
                SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                String phone=sp.getString("phone","");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if( DBControl.updateUser(((BitmapDrawable)niceImageView.getDrawable()).getBitmap()
                             ,phone,name,gender,dept,grade,address)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ModifyPersonalInfo.this,"更新成功",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (SQLException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ModifyPersonalInfo.this,"更新失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();

            }
        });

    }


    private void loadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info",Context.MODE_PRIVATE);
                String phone=sp.getString("phone","");
                User user=DBControl.searchUserByPhone(phone);
                if(user!=null){
                    ((EditText)findViewById(R.id.name_edit)).setText(user.name);
                    ((EditText)findViewById(R.id.address_edit)).setText(user.address);
                    ((EditText)findViewById(R.id.grade_edit)).setText(user.grade);
                    ((EditText)findViewById(R.id.dept_edit)).setText(user.dept);
                }
            }
        }).start();
    }

    /*
    官方文档代码
     */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

      //  Intent intent = new Intent("android.intent.action.GET_CONTENT");
      //       intent.setType("image/*");
      //       startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = null;
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
            }

            // Get the dimensions of the View
            int targetW = niceImageView.getWidth();
            int targetH = niceImageView.getHeight();

            if (imageBitmap != null) {
                imageBitmap = zoomBitmap(imageBitmap, targetW, targetH);
            }
            niceImageView.setImageBitmap(imageBitmap);
        }
    }

    public  Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }


    ///获取权限的结果
 // @Override
 // public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 //     if (requestCode == 1){
 //         if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) openAlbum();
 //         else Toast.makeText(this,"你拒绝了",Toast.LENGTH_SHORT).show();
 //     }
 // }

 // //启动相册的方法
 // private void openAlbum(){
 //     Intent intent = new Intent("android.intent.action.GET_CONTENT");
 //     intent.setType("image/*");
 //     startActivityForResult(intent,2);
 // }

 // @Override
 // protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
 //     super.onActivityResult(requestCode, resultCode, data);
 //     if (requestCode == 2) {
 //         //判断安卓版本
 //         if (resultCode == RESULT_OK && data != null) {
 //             handImage(data);
 //         }
 //     }
 // }

 // //安卓版本大于4.4的处理方法
 // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
 // private void handImage(Intent data){
 //     String path =null;
 //     Uri uri = data.getData();
 //     //根据不同的uri进行不同的解析
 //     if (DocumentsContract.isDocumentUri(this,uri)){
 //         String docId = DocumentsContract.getDocumentId(uri);
 //         if ("com.android.providers.media.documents".equals(uri.getAuthority())){
 //             String id = docId.split(":")[1];
 //             String selection = MediaStore.Images.Media._ID+"="+id;
 //             path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
 //         }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
 //             Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
 //             path = getImagePath(contentUri,null);
 //         }
 //     }else if ("content".equalsIgnoreCase(uri.getScheme())){
 //         path = getImagePath(uri,null);
 //     }else if ("file".equalsIgnoreCase(uri.getScheme())){
 //         path = uri.getPath();
 //     }
 //     //展示图片
 //     displayImage(path);
 // }




 // //content类型的uri获取图片路径的方法
 // private String getImagePath(Uri uri,String selection) {
 //     String path = null;
 //     Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
 //     if (cursor!=null){
 //         if (cursor.moveToFirst()){
 //             path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
 //         }
 //         cursor.close();
 //     }
 //     return path;
 // }

 // //根据路径展示图片的方法
 // private void displayImage(String imagePath){
 //     if (imagePath != null){
 //         Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
 //         niceImageView.setImageBitmap(bitmap);
 //     }else{
 //         Toast.makeText(this,"fail to set image",Toast.LENGTH_SHORT).show();
 //     }
 // }


}
