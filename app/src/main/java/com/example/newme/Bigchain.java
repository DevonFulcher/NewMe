package com.example.newme;

import android.os.AsyncTask;
import android.util.Log;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.FulFill;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;
import com.google.zxing.oned.rss.RSS14Reader;


import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Response;

/**
 * simple usage of BigchainDB Java driver (https://github.com/bigchaindb/java-bigchaindb-driver)
 * to create TXs on BigchainDB network
 * @author innoprenuer
 *
 * AsyncTask - https://developer.android.com/reference/android/os/AsyncTask.html
 * https://medium.com/@ankit.sinhal/understanding-of-asynctask-in-android-8fe61a96a238
 *
 */
public class Bigchain{

    private static KeyPairGenerator edDsaKpg = new KeyPairGenerator();
    private static final String TAG = "BigchainDB";
    private static String userId = "";
    private static final KeyPair KEYS = edDsaKpg.generateKeyPair();
    private static final String bigchainDBNodeURL = "https://35.212.69.121/";///"https://test.bigchaindb.com/";//"http://10.0.2.2:9984" ;
    private GenericCallback callback = null;

    public Bigchain(GenericCallback callback){

        this.callback = callback;
    }

    /**
     * configures connection url and credentials
     */
    public static void setConfig() {
        BigchainDbConfigBuilder
                .baseUrl(bigchainDBNodeURL)
                .addToken("app_id","")
                .addToken("app_key","").setup();


    }

    public Transaction sendTransaction(String data) throws Exception {

        Log.d(TAG, "Setting configuration..");
        setConfig();
        Transaction transaction = null;

        //create asset data
        Map<String, String> assetData = new TreeMap<String, String>();
        assetData.put("data", data);

        //create asset metadata
        Map<String, String> metadata = new TreeMap<String, String>();
        metadata.put("lastModifiedOn", new Date().toString());


        //build and send CREATE transaction
        transaction = BigchainDbTransactionBuilder
                .init()
                .addAssets(assetData, TreeMap.class)
                .addMetaData(metadata)
                .operation(Operations.CREATE)
                .buildAndSign((EdDSAPublicKey) KEYS.getPublic(), (EdDSAPrivateKey) KEYS.getPrivate())
                .sendTransaction(this.callback);

        Log.d(TAG, "(*) Transaction registered.. - " + transaction.getId());


        return transaction;

    }



    public String doCreate(Map<String, String> assetData, MetaData metaData, KeyPair keys) throws Exception {

        try {
            //build and send CREATE transaction
            Transaction transaction = null;

            transaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .addMetaData(metaData)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) CREATE Transaction sent.. - " + transaction.getId());
            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static KeyPair getKeys() {
        //  prepare your keys
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        System.out.println("(*) Keys Generated..");
        return keyPair;

    }



    public void doTransfer(String txId, MetaData metaData, KeyPair keys) throws Exception {

        Map<String, String> assetData = new TreeMap<String, String>();
        assetData.put("id", txId);

        try {


            //which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(txId);


            //build and send TRANSFER transaction
            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addInput(null, fulfill, (EdDSAPublicKey) keys.getPublic())
                    .addOutput("1", (EdDSAPublicKey) keys.getPublic())
                    .addAssets(txId, String.class)
                    .addMetaData(metaData)
                    .operation(Operations.TRANSFER)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) TRANSFER Transaction sent.. - " + transaction.getId());


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static Map<String, String> createUser(final String firstName, final String lastName, final String email){
        Map<String, String> assetData = new TreeMap<String, String>() {{
            put("User First Name", firstName);
            put("Last Name", lastName);
            put("email", email);
            put("purpose", "Creating User");
        }};
        return assetData;
    }



    private void onSuccess(Response response) {
        //TODO : Add your logic here with response from server
        System.out.println("Transaction posted successfully");
    }

    private void onFailure() {
        //TODO : Add your logic here
        System.out.println("Transaction failed");
    }

    private GenericCallback handleServerResponse() {
        //define callback methods to verify response from BigchainDBServer
        GenericCallback callback = new GenericCallback() {

            @Override
            public void transactionMalformed(Response response) {
                System.out.println("malformed " + response.message());
                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
                System.out.println("pushedSuccessfully");
                onSuccess(response);
            }

            @Override
            public void otherError(Response response) {
                System.out.println("otherError" + response.message());
                onFailure();
            }
        };

        return callback;
    }


}