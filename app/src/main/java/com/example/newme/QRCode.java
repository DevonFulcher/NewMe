package com.example.newme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    static int PReqCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAndRequestPermission();
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {
        MainActivity.resultTV.setText(result.getText());
        onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume(){
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    protected void checkAndRequestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                Toast.makeText(this,"Please accept to use QR Scanner",Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PReqCode);
            }
        }
        else{
            return;
        }
    }
}
