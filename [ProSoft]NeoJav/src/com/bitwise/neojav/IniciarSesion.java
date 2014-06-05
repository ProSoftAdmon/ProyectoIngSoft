package com.bitwise.neojav;

import org.json.JSONArray;
import org.json.JSONException;

import DataBase.LocalDB;
import DataBase.RemoteDB;
import Logica.Dialogo;
import Logica.IUtils;
import Logica.Utils;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class IniciarSesion extends Activity {

    private EditText username;
    private EditText password;
    private LocalDB sqlite;
    private IUtils ut = new Utils();    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		sqlite = new LocalDB(IniciarSesion.this, "local");
		sqlite.createTable();
		if(sqlite.consultarUser().equals("si")){
			String[] s = ut.obtenerUser(getFragmentManager(), sqlite.nombreUsuario());
			sqlite.insertarPerfil(s[1], s[3], Integer.parseInt(s[4]), Integer.parseInt(s[2]), s[5], s[6],sqlite.nombreUsuario());
			Intent i = new Intent(IniciarSesion.this,DrawerActivity.class);
			startActivity(i);
		}
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "TELE2.TTF");
	    TextView myTextView = (TextView)findViewById(R.id.Title);
	    myTextView.setTypeface(myTypeface);
	    username = (EditText) findViewById(R.id.username);
	    password = (EditText) findViewById(R.id.password);
	    if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
	}
	
	public void IniciarSesionClick(View v){
		try {
			JSONArray j = null;
			RemoteDB ini = new RemoteDB();
			ini.setmUrl("http://omargonzalez.dx.am/NeoJav/Login/acces.php?usuario="+username.getText()+"&password="+password.getText());
			ini.start();
			boolean flag = false;
			Dialogo d = new Dialogo();
			while(ini.isAlive()){
				if(!flag){
					d.setButtons(1);
					d.setTitle("Conectando");
					d.setMessage("Conectando a la base de datos espere porfavor...");
					d.setPosMes("Ok");
					flag = true;
					d.show(getFragmentManager(), "Conectando");
				}
			}	
			d.dismiss();
			if(ini.getResponse().length()>1){
				j = new JSONArray(ini.getResponse());
				String s = j.getString(0);
				System.out.println(s.equals("si"));
				if(s.equals("si")){
					sqlite.guardarUser(username.getText().toString(), password.getText().toString());
					String[] s1 = ut.obtenerUser(getFragmentManager(), sqlite.nombreUsuario());
					sqlite.insertarPerfil(s1[1], s1[3], Integer.parseInt(s1[4]), Integer.parseInt(s1[2]), s1[5], s1[6],sqlite.nombreUsuario());
					Intent i = new Intent(IniciarSesion.this,DrawerActivity.class);
					startActivity(i);
				}
				else{
					Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				    vibrator.vibrate(200);
					d = new Dialogo();
					d.setButtons(1);
					d.setTitle("Error");
					d.setMessage("Datos Incorrectos");
					d.setPosMes("Ok");
					d.show(getFragmentManager(), "Datos Incorrectos");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void RegistrarseClick(View v){
		Intent i = new Intent(IniciarSesion.this,RegisterUsuario.class);
		startActivity(i);
	}
}
