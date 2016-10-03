package com.coprotab.agarcia.coopbase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class Main extends ActionBarActivity {
    String codrack = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //sincBase();

        final EditText txtCodRack = (EditText)findViewById(R.id.txtCodRack);
        ListView listaFardos = (ListView)findViewById(R.id.listFardos);

        txtCodRack.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {;
                    codrack = txtCodRack.getText().toString();
                    Alert("Presionó ENTER","Recibido",Main.this);
                    return true;
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
                      // Whatever...
                  }
              }).create().show();
    }

    protected void sincBase() {
    }

    protected boolean validarRackOnLine(){
        return true;
    }

    protected boolean validarFardoOnLine(){
        return true;
    }
}
