package com.example.newme;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.MetaData;
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
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyPair;
import java.util.Map;
import java.util.TreeMap;


public class QRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    static Result qResult = null;
    private static final String TAG = "TransactionActivity";
    private static final int REQUEST_SIGNUP = 0;
    Bigchain bigchainDBApi = new Bigchain(handleServerResponse());
    int SUCCESS_CODE = 1;
    ZXingScannerView scannerView;
    static int PReqCode = 1;
    //SharedPreferences saveData = this.getSharedPreferences("com.example.newme.USER_DATA", 0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAndRequestPermission();
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    /**
     * Handle result is called to handle the result of starting the camera.
     */
    public void handleResult(Result result) {
        qResult = result;
        //Need to return the result to pass it to TransactionActivity.
        final JSONObject jo = new JSONObject();
        try {
            jo.put("{Transaction",qResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * In order to access the internet a new thread needs to be spun off the main thread.
         * Async task can also be used, but it has to extend the class.
         * new android.os.Handler().postDelayed() also works, used in the android boilerplate code example found here: https://github.com/bigchaindb/android-boilerplate
         *
         * MongoDB could be used to access the DB directly and register transactions that way. Connection issues so trying bigcahin Transactions as well.
         */

        //Thread thread = new Thread(new Runnable(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Transaction sentTx = null;
                        try {
                            //create a transaction
                            bigchainDBApi.setConfig();
                            KeyPair keys = bigchainDBApi.getKeys();

                            Map<String, String> assetData = new TreeMap<String, String>() {{
                                put("firstname", "Cooper");
                                put("lastname", "Opal");
                                put("purpose", "saving the world");
                            }};

                            MetaData metaData = new MetaData();
                            metaData.setMetaData("where is he now?", "Thailand");
                            String transID = bigchainDBApi.doCreate(assetData,metaData,keys);

                            //transfer data

                            MetaData transferMetadata = new MetaData();
                            transferMetadata.setMetaData("where is he now?", "Japan");

                            Thread.sleep(5000);

                            bigchainDBApi.doTransfer(transID,transferMetadata,keys);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },3000);


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
