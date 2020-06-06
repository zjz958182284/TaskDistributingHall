package com.example.taskdistributinghall.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Fragment.Home.HomeFragment;
import com.example.taskdistributinghall.Fragment.Home.RecyclerViewAdapter;
import com.example.taskdistributinghall.Fragment.PersonalCenter.ModifyPersonalInfo;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;

public class AddMissionPage extends AppCompatActivity {

    private final  static int REQUEST_IMAGE_CAPTURE=1;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mission_page);
        Toolbar toolbar = findViewById(R.id.add_mission_toolbar);
        setSupportActionBar(toolbar);

         imageView = findViewById(R.id.select_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button button = findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String taskType;
                        RadioGroup radioGroup = findViewById(R.id.radio_group);
                        int checkedId=radioGroup.getCheckedRadioButtonId();
                        if (checkedId == R.id.errand)
                            taskType = "errand";
                        else if (checkedId == R.id.study)
                            taskType = "study";
                        else if(checkedId==R.id.collaboration)
                            taskType = "collaboration";
                        else taskType="";
                        EditText editTextTitle = findViewById(R.id.mission_title_edit);
                        EditText editTextDescription = findViewById((R.id.mission_description_edit));
                        EditText editTextBounty = findViewById(R.id.bounty_edit);
                        String title = editTextTitle.getText().toString();
                        String detail = editTextDescription.getText().toString();
                        int bounty=0;
                        try {
                             bounty = Integer.parseInt(editTextBounty.getText().toString());
                        }catch (NumberFormatException e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddMissionPage.this,"请填写整数金额",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }

                        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                        String phone=sp.getString("phone","");
                        try {
                            boolean isComplete= !taskType.equals("")&&!detail.equals("") && !title.equals("") && bounty > 0;
                            if(!isComplete)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddMissionPage.this,"请填写完整信息",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            else {
                                HomeFragment homeFragment=HomeFragment.getInstance();
                              //  RecyclerViewAdapter adapter=homeFragment.getAdapter();
                                DBControl.addTask(bitmap,phone, detail, title, taskType, bounty);
                               // List<Task> tasks=DBControl.searchAllTask();
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Toast.makeText(AddMissionPage.this, "发布任务成功",
                                             Toast.LENGTH_SHORT).show();
                                   // homeFragment.refresh();
                                    button.setVisibility(View.GONE);

                                   //  //更新任务大厅界面
                                   //  homeFragment.setTasks(tasks);
                                   //  adapter.setTasks(tasks);
                                   //  adapter.notifyDataSetChanged();
                                 }
                             });
                            }
                        } catch (SQLException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddMissionPage.this,"发布任务失败",
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

            //    // Get the dimensions of the View
            //    int targetW = imageView.getWidth();
            //    int targetH = imageView.getHeight();
//
            //    if (imageBitmap != null) {
            //        imageBitmap = zoomBitMap(imageBitmap, targetW, targetH);
            //    }
                imageView.setImageBitmap(imageBitmap);
            }
        }

        public Bitmap zoomBitMap(Bitmap bitmap,int w,int h){


            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bitmap.getWidth();
            int photoH = bitmap.getHeight();

            // Determine how much to scale down the image
            int scaleFactor = Math.min(w,h);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] bytes=bos.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,bmOptions);
            return bitmap;
        }

        public  Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidth = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
        }




}
