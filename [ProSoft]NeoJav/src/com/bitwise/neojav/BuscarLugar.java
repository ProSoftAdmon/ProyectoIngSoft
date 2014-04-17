package com.bitwise.neojav;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DataBase.LocalDB;
import DataBase.ConectarDB;
import Logica.Dialogo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BuscarLugar extends Activity {

	private static Object lat;
	private static Object lng;
	private static Object place;
	private EditText search;
	private Button enter;
	private GoogleMap mMap;
	private LocalDB admin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		cargarMarkersPHP();
		search = (EditText) findViewById(R.id.search);
		enter = (Button) findViewById(R.id.Buscar);
		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = search.getText().toString();
				if (!text.isEmpty()) {
					getLatLongFromAddress(trimer(text.trim()));
					mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
					mMap.clear();
					cargarMarkersPHP();
					LatLng position = new LatLng(Double.parseDouble(lat
							.toString()), Double.parseDouble(lng.toString()));
					mMap.addMarker(new MarkerOptions().position(position)
							.title(place.toString().split(",")[0])
							.snippet(place.toString()));
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(position).zoom(16)
							.bearing(90)
							.tilt(0).build();
					mMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}
		});
		search.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					String text = search.getText().toString();
					if (!text.isEmpty()) {
						getLatLongFromAddress(trimer(text.trim()));
						mMap = ((MapFragment) getFragmentManager()
								.findFragmentById(R.id.map)).getMap();
						mMap.clear();
						cargarMarkersPHP();
						LatLng position = new LatLng(Double.parseDouble(lat
								.toString()),
								Double.parseDouble(lng.toString()));
						mMap.addMarker(new MarkerOptions().position(position)
								.title(place.toString().split(",")[0])
								.snippet(place.toString()));
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(position).zoom(16) // Sets the zoom
								.bearing(90) // Sets the orientation of the
												// camera to east
								.tilt(0).build();
						mMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
					}
					return true;
				}
				return false;
			}
		});
	}

	private void cargarMarkersPHP() {
		try {
			JSONArray j = null;
			ConectarDB ini = new ConectarDB();
			ini.setmUrl("http://omargonzalez.dx.am/NeoJav/Markers/consultarMarker.php");
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
				StringTokenizer st = new StringTokenizer(ini.getResponse(),"]");
				while(st.hasMoreTokens()){
					j = new JSONArray(st.nextToken()+"]");
					mMap.addMarker(new MarkerOptions()
							.position(new LatLng(Double.parseDouble(j.getString(0)), Double.parseDouble(j.getString(1))))
							.title(j.getString(2))
							.snippet(j.getString(3))
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(BuscarLugar.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
		finish();
	}

	public String trimer(String s) {
		String ret = new String();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				ret += s.charAt(i);
			}
		}
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void getLatLongFromAddress(String youraddress) {
		String uri = "http://maps.google.com/maps/api/geocode/json?address="
				+ youraddress + ",Bogota&sensor=false";
		HttpGet httpGet = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

			place = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getString("formatted_address");

			lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public LocalDB getAdmin() {
		return admin;
	}

	public void setAdmin(LocalDB admin) {
		this.admin = admin;
	}

}
