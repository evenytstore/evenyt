package com.app.evenytstore.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;
import com.app.evenytstore.Utility.DecimalHandler;
import com.app.evenytstore.Utility.TimeBasedOneTimePasswordGenerator;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;

/**
 * Created by Enrique on 12/07/2017.
 */

public class InputSmsCodeActivity extends AppCompatActivity {

    private Calendar lastSend;

    public class SMSSendTask extends AsyncTask<PublishRequest, Void, Void> {
        @Override
        protected Void doInBackground(PublishRequest... params) {
            PublishRequest request = params[0];
            try{
                //Descomentar para usar el sms
                PublishResult result = snsClient.publish(request);
                System.out.println(result); // Prints the message ID.
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }


    public class ServerCustomerTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ServerAccess.getClient().customersPost(AppSettings.CURRENT_CUSTOMER);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            try {
                DatabaseAccess instance = DatabaseAccess.getInstance(InputSmsCodeActivity.this);
                instance.open();
                instance.insertCustomer(AppSettings.CURRENT_CUSTOMER);
                instance.close();
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }

            return true;
        }

        protected void onPostExecute(Boolean result){
            if(!result){
                Dialog dialog = new AlertDialog.Builder(InputSmsCodeActivity.this)
                        .setTitle("Error")
                        .setMessage("No se pudo establecer conexión al servidor.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                if(!InputSmsCodeActivity.this.isFinishing() && ! InputSmsCodeActivity.this.isDestroyed())
                    dialog.show();
            }else{
                Intent intent = new Intent(InputSmsCodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }


    private AmazonSNSClient snsClient;
    private String accessKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sms_code);

        Button confirmButton = (Button)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView codeText = (TextView)findViewById(R.id.codeText);
                String code = codeText.getText().toString();
                if(code.equals(accessKey)){
                    ServerCustomerTask serverCustomerTask = new ServerCustomerTask();
                    serverCustomerTask.execute();
                }else{
                    Dialog dialog = new AlertDialog.Builder(InputSmsCodeActivity.this)
                            .setTitle("Error")
                            .setMessage("El código ingresado es incorrecto.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        });

        Button resendButton = findViewById(R.id.resendButton);
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastSend != null){
                    Calendar currTime = Calendar.getInstance();
                    long difference = currTime.getTimeInMillis() - lastSend.getTimeInMillis();
                    if(difference < 1000*5*60){
                        final Dialog dialog = new Dialog(InputSmsCodeActivity.this, R.style.Theme_Dialog);
                        dialog.setContentView(R.layout.dialog_information);
                        dialog.setCanceledOnTouchOutside(true);
                /*LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_address, null);*/
                        Button okButton = dialog.findViewById(R.id.okButton);
                        TextView informationText = dialog.findViewById(R.id.txtInformation);
                        informationText.setText("Han pasado menos de 5 minutos desde su último reenvio. Por favor intente nuevamente en un momento.");
                        informationText.setGravity(Gravity.CENTER_HORIZONTAL);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        return;
                    }
                }
                sendSms();
                lastSend = Calendar.getInstance();

                final Dialog dialog = new Dialog(InputSmsCodeActivity.this, R.style.Theme_Dialog);
                dialog.setContentView(R.layout.dialog_information);
                dialog.setCanceledOnTouchOutside(true);
                /*LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_address, null);*/
                Button okButton = dialog.findViewById(R.id.okButton);
                TextView informationText = dialog.findViewById(R.id.txtInformation);
                informationText.setText("El código de verificación ha sido reenviado correctamente.");
                informationText.setGravity(Gravity.CENTER_HORIZONTAL);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        accessKey = "";
        try {
            final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
            final Key secretKey;
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());

            // SHA-1 and SHA-256 prefer 64-byte (512-bit) keys; SHA512 prefers 128-byte keys
            keyGenerator.init(512);

            secretKey = keyGenerator.generateKey();
            final Date now = new Date();
            accessKey = String.valueOf(totp.generateOneTimePassword(secretKey, now));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeyException e){
            e.printStackTrace();
        }

        snsClient = new AmazonSNSClient(new BasicAWSCredentials(getString(R.string.access_key_aws),
                getString(R.string.secret_key_aws)));
        sendSms();
        ServerCustomerTask serverCustomerTask = new ServerCustomerTask();
        serverCustomerTask.execute();
    }

    private void sendSms(){
        String message = "Su clave de acceso es "+ accessKey;
        String phoneNumber = AppSettings.CURRENT_CUSTOMER.getPhoneNumber();
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue("EvenytStore") //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Transactional") //Sets the type to transactional.
                .withDataType("String"));
        //<set SMS attributes>
        PublishRequest request = new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes);
        SMSSendTask smsTask = new SMSSendTask();
        smsTask.execute(request);
    }
}


