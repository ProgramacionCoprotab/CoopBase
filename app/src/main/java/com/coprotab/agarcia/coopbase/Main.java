package com.coprotab.agarcia.coopbase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class Main extends AppCompatActivity {
    String codrack = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final CoopBaseDBHelper db = new CoopBaseDBHelper(getApplicationContext());

        //sincBase();

        final EditText txtCodRack = (EditText)findViewById(R.id.txtCodRack);
        final Button btSinc = (Button)findViewById(R.id.btSinc);
        ListView listaFardos = (ListView)findViewById(R.id.listFardos);

        btSinc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ConsultaWSDB consulta = new ConsultaWSDB();
                consulta.execute();
            }
        });



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_seccion_1:
                Intent i = new Intent(getApplicationContext(), User.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void Alert(String Titulo, String Mensaje,Context context){
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

    private class ConsultaWSDB extends AsyncTask<String,Integer,Boolean> {
        Context context;
        CoopBaseDBHelper db = new CoopBaseDBHelper(getApplicationContext());

        protected Boolean doInBackground(String... params){
            boolean resul = true;

            Integer[] vector;

            String NAMESPACE = "http://192.168.50.248/";
            String METHOD_NAME = "ListadoRacks";
            String SOAP_ACTION = "http://192.168.50.248/ListadoRacks";
            String URL = "http://192.168.50.248:8819/ServicioTabaco.asmx";

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            /*PropertyInfo pi = new PropertyInfo(); En este metodo no hay argumentos
            pi.setName("");
            pi.setType(null);
            pi.setValue(null);
            Request.addProperty(pi);*/

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // version 1.1 de xml
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);

            //AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
            HttpTransportSE transporte = new HttpTransportSE(URL);

            try
            {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap =(SoapObject)envelope.getResponse();

                int cantidad = resSoap.getPropertyCount();
                vector = new Integer[cantidad];

                for (int i = 0; i < cantidad; i++)
                {
                    int nrack;
                    SoapObject ic = (SoapObject)resSoap.getProperty(i);

                    nrack = Integer.parseInt(ic.getProperty(0).toString());

                    vector[i] = nrack;
                    db.agregarRack(nrack);
                }
            }
            catch (Exception ex)
            {
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Main main = new Main();
                TextView txtFardos = (TextView)findViewById(R.id.txtCodRack);
                txtFardos.setText("CORRECTO");
            }
            else
            {

            }
        }
    }


}



