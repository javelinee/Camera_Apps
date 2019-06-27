package com.mine.camera_apps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.Policy;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    Button saklar;
    private final int CAMERA_REQUEST_CODE = 2;
    boolean hasCameraFlash = false;
    private boolean flashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        saklar = findViewById( R.id.onOff );

        hasCameraFlash = getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA_FLASH );
        saklar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                askPermission(Manifest.permission.CAMERA,CAMERA_REQUEST_CODE);
            }
        } );
    }

    private void flashlight(){
        if (hasCameraFlash) {
            if(flashOn){
                saklar.setText( "On" );
                flashlightOff();
                flashOn = false;
            }else{
                saklar.setText( "Off" );
                flashlightOn();
                flashOn = true;
            }
        }else{
            Toast.makeText( MainActivity.this, "This apps can't run on your device", Toast.LENGTH_SHORT ).show();
        }
    }

    private void flashlightOn(){
        CameraManager cm = (CameraManager) getSystemService( Context.CAMERA_SERVICE );

        try{
            String cameraID = cm.getCameraIdList()[0];
            cm.setTorchMode( cameraID, true );
        }catch(CameraAccessException e){

        }
    }
    private void flashlightOff(){
        CameraManager cm = (CameraManager) getSystemService( Context.CAMERA_SERVICE );

        try{
            String cameraID = cm.getCameraIdList()[0];
            cm.setTorchMode( cameraID, false );
        }catch(CameraAccessException e){

        }
    }


    private void askPermission(String permission, int code){
        if (ContextCompat.checkSelfPermission( this, permission )!= PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[]{permission}, code );
        }else{
            flashlight();
        }
    }

    private void onRequestPermissionResult(int code, @NonNull String[] permission, @NonNull int[] grantResult){
        switch (code){
            case CAMERA_REQUEST_CODE :
                if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    hasCameraFlash = getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA_FLASH );
                    Toast.makeText( this, "Camera Permission Granted", Toast.LENGTH_SHORT ).show();
                    flashlight();
                }else{
                    Toast.makeText( this, "Camera Permission Denied", Toast.LENGTH_SHORT ).show();
                }
                break;
        }
    }
}
