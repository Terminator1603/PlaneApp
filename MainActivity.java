package com.example.toshan.planeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Camera camera;
    ShowCamera showCamera;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.startCapture);
        final Button endCap = findViewById(R.id.stopCapture);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);


        //open camera
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);

        frameLayout.addView(showCamera);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    camera.takePicture(null, null, nPictureCallback);

            }
        });
    }


    Camera.PictureCallback nPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if(picture_file == null){

            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();

                }  catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
    };
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type){
        Environment.getExternalStorageState();

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"PlaneAppPhotos");
        //create directory if it doesn't exist
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.d("PlaneApp","failed to create directory");
                return null;
            }
        }

        //Create File name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if(type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        }else {
            return null;
        }
        return mediaFile;

    }

    }

