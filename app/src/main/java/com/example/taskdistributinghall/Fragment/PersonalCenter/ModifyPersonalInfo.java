package com.example.taskdistributinghall.Fragment.PersonalCenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;
import com.shehuan.niv.NiceImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

public class ModifyPersonalInfo extends AppCompatActivity {

    NiceImageView niceImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1; //请求拍照
    static final int REQUEST_ALBUM=2; //请求相册

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_personal_info);

        /**
         * 开一个线程去加载数据
         */

         new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info",Context.MODE_PRIVATE);
                String phone=sp.getString("phone","");
                User user=DBControl.searchUserByPhone(phone);
                if(user!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 更新显示布局
                             */
                            ((NiceImageView)findViewById(R.id.profile_photo)).setImageBitmap(user.headPortrait==null?null:user.headPortrait);
                            ((EditText)findViewById(R.id.name_edit)).setText(user.name);
                            ((EditText)findViewById(R.id.address_edit)).setText(user.address);
                            ((EditText)findViewById(R.id.grade_edit)).setText(user.grade);
                            ((EditText)findViewById(R.id.dept_edit)).setText(user.dept);
                            /**
                             * 等待页面加载完成，然后关闭loading布局
                             */
                            findViewById(R.id.loading_constraint).setVisibility(View.GONE);
                            findViewById(R.id.manifest_constraint).setVisibility(View.VISIBLE);
                        }
                    });

                }
            }
        }).start();




        Button button=findViewById(R.id.ok_btn);
        niceImageView=findViewById(R.id.profile_photo);

        //打开相册
        niceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //camera=Camera.
               showBottomPopupWindow();
                //dispatchTakePictureIntent();
        }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                             ,phone,name,dept,grade,address)){
                               // PersonalCenterFragment.getInstance().refresh();
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


    /*
    官方文档代码
     */

    /**
     * 打开相册选取照骗
     */
    private  void dispatchGetAlbumIntent(){
        Intent intent=new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_ALBUM);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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
        else if(requestCode==REQUEST_ALBUM&&resultCode == RESULT_OK && null != data){

            Uri selectedImage = data.getData();

            niceImageView.setImageBitmap( getBitmapFromUri(this,selectedImage));

            /**
             * 以下被注释内容用来完成获得图片路径
             */
            //  String[] filePathColumn = { MediaStore.Images.Media.DATA };
          //  //查询我们需要的数据
          //  Cursor cursor = getContentResolver().query(selectedImage,
          //          filePathColumn, null, null, null);
          //  cursor.moveToFirst();
//
          //  int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
          //  String picturePath = cursor.getString(columnIndex);
          //  cursor.close();
          //  displayImage(picturePath);
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

    /**
     * 根据路径展示图片的方法
     * Android 10以后因为分区存储的关系无法再通过这种通过图片路径加载图片的方法获取图片了
     */

  private void displayImage(String imagePath){
      if (imagePath != null){
              Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
              niceImageView.setImageBitmap(bitmap);
      }else{
          Toast.makeText(this,"fail to set image",Toast.LENGTH_SHORT).show();
      }
  }


    /**
     * 可以通过uri来加载图片
     */

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {

        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showBottomPopupWindow(){

        View popView= LayoutInflater.from(this).inflate(R.layout.capture_select,null,false);
        PopupWindow popupWindow=new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        popView.findViewById(R.id.capture_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                popupWindow.dismiss();
            }
        });
        popView.findViewById(R.id.select_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchGetAlbumIntent();
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }

}
