package com.example.newme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bigchaindb.constants.BigchainDbApi;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Response;



public class QRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    static String qResult = null;
    private static final String TAG = "TransactionActivity";
    private static final int REQUEST_SIGNUP = 0;
    Bigchain bigchainDBApi = new Bigchain(handleServerResponse());
    int SUCCESS_CODE = 1;
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
        //Need to return the result to pass it to TransactionActivity.
        qResult = result.getText();
        //MainActivity.resultTV.setText(result.getText());
        try {
            //Bigchain.sendTransaction(qResult);
            this.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void send() throws Exception {
        //Bigchain thisBigChain = new com.example.newme.Bigchain();
//        Transaction newTransaction = null;
        Log.d(TAG, "Sending Transaction");

        if (!validate()) {
            onSendFailed();
            return;
        }

        //get string from Daniel's QR code
        Transaction sentTx = null;
        if(QRCode.qResult.equals(null)){
            Log.d("oof", "NUll QRCODE");
        }else{
            //sentTx = thisBigChain.sendTransaction(QRCode.qResult);
            //newTransaction = thisBigChain.sendTransaction(QRCode.qResult);
            bigchainDBApi.sendTransaction(QRCode.qResult);
            Log.d(TAG, sentTx.toString());
        }
        bigchainDBApi.sendTransaction(QRCode.qResult);

//        Transaction sentText = thisBigChain.sendTransaction(QRCode.qResult);
//        bigchainDBApi.sendTransaction(sentText);

//        Transaction sentTx = null;
//        try{
//            sentTx = bigchainDBApi.sendTransaction(QRCode.this.qResult);
//        } catch (ConnectException ex){
//            //set error code
//            SUCCESS_CODE = -2;
//        } catch (Exception e){
//            //set error code
//            SUCCESS_CODE = -3;
//        }

//        Log.d(TAG, sentTx.toString()); logging sentText
//        final Transaction tx = sentTx;
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        Log.d(TAG, "Success code - " + SUCCESS_CODE);
//                        while(SUCCESS_CODE == 1){
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            Log.d(TAG, "Still waiting with code - " + SUCCESS_CODE);
//                        }
//                        if(SUCCESS_CODE == 0){
//                            //onSendSuccess(tx);
//                        }
//                        else {
//                            onSendFailed();
//                        }
//
//                        //progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        //moveTaskToBack(true);
        scannerView.stopCamera();
        this.finish();
    }

    public void onSendSuccess(Transaction successfulTx) {
        //_txButton.setEnabled(true);

        Toast.makeText(getBaseContext(), "Transaction successful", Toast.LENGTH_LONG).show();
        JsonObject jsonObject = new JsonParser().parse(successfulTx.toString()).getAsJsonObject();
        //_txRespText.setText(toPrettyFormat(jsonObject.toString()));
        //set success code to intial state
        SUCCESS_CODE = 1;
    }

    public void onSendFailed() {
        Log.d(TAG, "Transaction failed. Success code - " + SUCCESS_CODE);
        String msg = "";
        if(SUCCESS_CODE == -2){
            msg = "Couldn't connect to BigchainDB";
            SUCCESS_CODE = 1;
        }
        else if(SUCCESS_CODE == -3){
            msg = "Some error occurred. Exiting";
        }
        else {
            msg ="Transaction Failed";
            SUCCESS_CODE = 1;
        }


    }

    public boolean validate() {
        boolean valid = true;

        String tx = "Code from scanning QR label";
        if(tx.equals(null)){
            Log.d("Error", "set an error here");
            valid = false;
        }
//            if (tx.isEmpty()) {
//            _txText.setError("type a message to send");
//            valid = false;
//        } else {
//            _txText.setError(null);
//        }

        return valid;

    }

    private GenericCallback handleServerResponse() {
        //define callback methods to verify response from BigchainDBServer
        GenericCallback callback = new GenericCallback() {

            @Override
            public void transactionMalformed(Response response) {
                Log.d(TAG, "malformed " + response.message());
                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
                Log.d(TAG, "pushedSuccessfully");
                onSuccess(response);
            }

            @Override
            public void otherError(Response response) {
                Log.d(TAG, "otherError" + response.message());
                onFailure();
            }
        };

        return callback;
    }

    private void onSuccess(Response response) {
        SUCCESS_CODE = 0;
        Log.d(TAG, "(*) Transaction successfully committed..");
        Log.d(TAG, response.toString());
    }

    private void onFailure() {
        SUCCESS_CODE = -1;
        Log.d(TAG, "Transaction failed");
        Log.d(TAG, "Transaction failed");
    }

    /**
     * Convert a JSON string to pretty print version
     * @param jsonString
     * @return
     */
    public static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

}