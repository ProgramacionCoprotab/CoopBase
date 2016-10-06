package com.coprotab.agarcia.coopbase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.coprotab.agarcia.coopbase.CoopBaseDBHelper;

import java.util.List;
import java.util.Scanner;

public class Main extends ActionBarActivity {
    String codrack = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final CoopBaseDBHelper db = new CoopBaseDBHelper(getApplicationContext());

        //sincBase();

        final EditText txtCodRack = (EditText)findViewById(R.id.txtCodRack);
        ListView listaFardos = (ListView)findViewById(R.id.listFardos);

        txtCodRack.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    try {
                        codrack = txtCodRack.getText().toString();
                        db.agregarRack(Integer.parseInt(codrack));
                        Alert("confirmado", "ingresado", Main.this);
                        txtCodRack.setText("");
                        //validar si es rack
                        return true;
                    } catch (Exception ex) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    protected void Alert(String Titulo, String Mensaje,Context context){
        new AlertDialog.Builder(context)
              .setTitle(Titulo)
              .setMessage(Mensaje)
              .setCancelable(false)
              .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      // código al dar click en OK

                  }
              }).create().show();
    }


    protected boolean validarRackOnLine(){
        return true;
    }

    protected boolean validarFormatoRack(String rack)
    {
        try {
            String inicial = rack.substring(0, 1);
            Integer longitud = rack.length();
            if (inicial.equals( "0" )) {

                return rack.length() == 8;
            }
            else

                return false;

        }
        catch (Exception ex){
            ex.getMessage();
            return false;
        }

    }

    protected boolean validarFormatoFardo(String fardo){
        try
        {
            Integer longitud = fardo.length();
            return longitud == 9;

        }
        catch (Exception ex){
            return false;
        }
    }


    protected boolean validarFardoOnLine(){
        return true;
    }


    //procedimiento para conocer la red a la que esta conectado el dispositivo
    protected boolean OnLine(){
        try {
            boolean connected = false;
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                wifiInfo = wifi.getConnectionInfo();
                String ssidWifi = wifiInfo.getSSID().substring(1,11);
                if (ssidWifi.equals("APCoprotab")) {
                    return connected = true;
                } else {
                    return connected = false;
                }
            } else
                return connected = false;
        }
        catch(Exception ex){
            ex.getMessage();
            return false;
        }
    }
}
