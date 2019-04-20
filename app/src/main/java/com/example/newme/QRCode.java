package com.example.newme;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.util.Log;
import android.content.Intent;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Response;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import static com.example.newme.Bigchain.connectToMongo;


public class QRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    /**
     * TODO: research sending transactions.
     * After scanning QRCode the app crashes.
     *
     * Research found here:
     *  BigchainDB Java Driver - https://github.com/bigchaindb/java-bigchaindb-driver
     *
     * Transaction example - https://gist.github.com/innoprenuer/d4c6798fe5c0581c05a7e676e175e515
     *      Probably need to move away from boiler plate code and try to implement the transaction example more closely.
     *
     * Boiler plate- https://github.com/bigchaindb/android-boilerplate
     *
     * One of the errors being thrown:
     * https://www.slf4j.org/codes.html#StaticLoggerBinder
     *
     *
     */
    static String qResult = null;
    private static final String TAG = "TransactionActivity";
    private static final int REQUEST_SIGNUP = 0;
    public Bigchain bigchainDBApi = new Bigchain(this.handleServerResponse());

    //SharedPreferences saveData = this.getSharedPreferences("com.example.newme.USER_DATA",0);
    User user = MakeAccount.user;
    //TODO use model.transactions
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



//      new Thread new Runnable code found here: https://developer.android.com/guide/components/processes-and-threads
        //FROM: https://gist.github.com/innoprenuer/d4c6798fe5c0581c05a7e676e175e515
        new Thread(new Runnable() {
            public void run() {

                try {
                    //bigchainDBApi.setConfig();
                    //MongoClient mongo = Bigchain.connectToMongo();
                    //MongoDatabase database = mongo.getDatabase("bigchain");

                    //http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
                    //Document doc = new Document("voucher", "BigchainDB")
                    //        .append("voucher", qResult);


                    //MongoCollection<Document> transactionDoc = database.getCollection("transactions");
                    //transactionDoc.insertOne(doc);

                    Log.d("try","Transaction sent?");
                    //TODO: Have a class for available funds...
                    //TODO: send transaction should actually be a transfer

                    bigchainDBApi.sendTransaction(qResult);
                    //Log.d("WIN","Transaction sent?");
                    Intent toProfile = new Intent(QRCode.this,ProfilePage.class);
                    startActivity(toProfile);

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }).start();


        MainActivity.resultTV.setText(result.getText());
        //String[] oneItem = {qResult}; //doInBackground takes a list of strings as input?

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
        moveTaskToBack(true);
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