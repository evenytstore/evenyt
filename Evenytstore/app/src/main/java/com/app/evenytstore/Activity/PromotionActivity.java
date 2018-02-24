package com.app.evenytstore.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.apigateway.ApiClientException;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;

import EvenytServer.model.Promotions;

/**
 * Created by Enrique on 24/02/2018.
 */

public class PromotionActivity extends AppCompatActivity {


    public class ServerPromotionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String code = params[0];
            try {
                Promotions p = ServerAccess.getClient().promotionsCodeGet(code);
                if(p.getStatus() == 0)
                    return "Expired";
                p.setStatus(0);
                AppSettings.CURRENT_PROMOTION = p;
            }catch(ApiClientException e){
                if(e.getStatusCode() == 400)
                    return e.getErrorMessage();
                else
                    return "Failed";
            }catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }
            return "Correct";
        }

        protected void onPostExecute(String result){
            if(result.equals("Correct")){
                Dialog dialog = new AlertDialog.Builder(PromotionActivity.this)
                        .setTitle("Exito")
                        .setMessage("Promoción de " + String.valueOf(AppSettings.CURRENT_PROMOTION.getPercentage().doubleValue()*100) +  "% ingresada exitosamente.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        setResult(RESULT_OK, new Intent());
                        finish();
                    }
                });
                dialog.show();
            }else if (result.equals("Failed")){
                Dialog dialog = new AlertDialog.Builder(PromotionActivity.this)
                        .setTitle("Error")
                        .setMessage("Se ha encontrado un problema, por favor intente luego nuevamente.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }else if (result.equals("Expired")){
                Dialog dialog = new AlertDialog.Builder(PromotionActivity.this)
                        .setTitle("Error")
                        .setMessage("El código promocional ingresado ya ha sido utilizado.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }else{ //Invalid promotional code
                Dialog dialog = new AlertDialog.Builder(PromotionActivity.this)
                        .setTitle("Error")
                        .setMessage("El código promocional ingresado no es válido")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        Button confirmButton = (Button)findViewById(R.id.confirmButton);
        TextView currentPromotion = (TextView)findViewById(R.id.txtCurrentPromotion);
        if(AppSettings.CURRENT_PROMOTION != null)
            currentPromotion.setText("Promoción activa con descuento de " + String.valueOf(AppSettings.CURRENT_PROMOTION.getPercentage().doubleValue()*100) + "%");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView codeText = (TextView)findViewById(R.id.codeText);
                String code = codeText.getText().toString();
                ServerPromotionTask task = new ServerPromotionTask();
                task.execute(code);
            }
        });
    }
}