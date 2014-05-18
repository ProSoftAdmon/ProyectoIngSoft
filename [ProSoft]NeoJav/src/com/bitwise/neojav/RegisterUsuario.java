package com.bitwise.neojav;

import DataBase.LocalDB;
import DataBase.RemoteDB;
import Logica.Dialogo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterUsuario extends Activity {

	private EditText user;
	private EditText pass;
	private EditText name;
	private EditText lastname;
	
	private LocalDB ldb = new LocalDB(this,"local");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		user = (EditText) findViewById(R.id.newusername);
		pass = (EditText) findViewById(R.id.newpassword);
		name = (EditText) findViewById(R.id.name);
		lastname = (EditText) findViewById(R.id.lastname);
		Button re = (Button) findViewById(R.id.registrar);
		re.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialogo d = new Dialogo();
				if(!user.getText().toString().isEmpty() && !pass.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !lastname.getText().toString().isEmpty()){
					RemoteDB ini = new RemoteDB();
					ini.setmUrl("http://omargonzalez.dx.am/NeoJav/Login/adduser.php?usuario="+user.getText()+"&password="+pass.getText()+"&nombre="+name.getText()+"&apellido="+lastname.getText());
					ini.start();
					boolean flag = false;
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
						String s = ini.getResponse();
						System.out.println(s);
						if(!s.trim().equals("YaExiste")){
							if(!s.equals("Error")){
								ldb.guardarUserName(user.getText().toString(), pass.getText().toString(),name.getText().toString(),lastname.getText().toString());
								Intent i = new Intent(RegisterUsuario.this,DrawerActivity.class);
								startActivity(i);
							}
							else{
								Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
							    vibrator.vibrate(200);
								d = new Dialogo();
								d.setButtons(1);
								d.setTitle("Error");
								d.setMessage("Error con la base de datos, intente de nuevo mas tarde");
								d.setPosMes("Ok");
								d.show(getFragmentManager(), "Error DB");
							}
						}
						else{
							Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						    vibrator.vibrate(200);
							d = new Dialogo();
							d.setButtons(1);
							d.setTitle("Error");
							d.setMessage("Usuario ya existente");
							d.setPosMes("Ok");
							d.show(getFragmentManager(), "Error Usuario Existente");
						}
					}
				}
				else{
					Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				    vibrator.vibrate(200);
					d = new Dialogo();
					d.setButtons(1);
					d.setTitle("Error");
					d.setMessage("Problema");
					d.setPosMes("Ok");
					d.show(getFragmentManager(), "Base de Datos Ocupada");
				}
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
