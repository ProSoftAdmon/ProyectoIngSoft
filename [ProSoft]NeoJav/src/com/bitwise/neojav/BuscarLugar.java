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

import DataBase.RemoteDB;
import DataBase.LocalDB;
import Logica.Dialogo;
import Logica.IUtils;
import Logica.Utils;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BuscarLugar extends Activity {

	/*private static Object lat;
	private static Object lng;
	private static Object place;*/
	
	private IUtils ut;
	
	private EditText search;
	private Button enter;
	private GoogleMap mMap;
	private LocalDB admin;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] s;
	private static Context th;

	private DrawerLayout mDrawerLayout2;
	private ListView mDrawerList2;
	private ActionBarDrawerToggle mDrawerToggle2;

	private CharSequence mDrawerTitle2;
	private CharSequence mTitle2;
	private String[] s2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		th = BuscarLugar.this;
		setContentView(R.layout.buscar_lugar);
		setUpMapIfNeeded();
		mMap.setMyLocationEnabled(true);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
		mDrawerList = (ListView) findViewById(R.id.left_drawer2);
		s = getResources().getStringArray(R.array.drawer_array);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, s));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(1);
		}
		mTitle2 = mDrawerTitle2 = getTitle();
		mDrawerLayout2 = (DrawerLayout) findViewById(R.id.drawer_layout2);
		mDrawerList2 = (ListView) findViewById(R.id.right_drawer);
		s2 = getResources().getStringArray(R.array.drawer_array2);
		mDrawerLayout2.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.END);
		mDrawerList2.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, s2));
		mDrawerList2.setOnItemClickListener(new DrawerItemClickListener2());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerLayout2.setDrawerListener(mDrawerToggle2);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setUpMapIfNeeded();
		ut.cargarMarkersPHP(getFragmentManager(),mMap);
		search = (EditText) findViewById(R.id.search);
		enter = (Button) findViewById(R.id.Buscar);
		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				markerPunto();
			}
		});
		search.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					markerPunto();
					return true;
				}
				return false;
			}
		});
	}
	
	public void markerPunto(){
		String text = search.getText().toString();
		if (!text.isEmpty()) {
			ut.getLatLongFromAddress(ut.trimer(text.trim()));
			setUpMapIfNeeded();
			mMap.clear();
			ut.cargarMarkersPHP(getFragmentManager(),mMap);
			LatLng position = new LatLng(Double.parseDouble(Utils.getLat()
					.toString()),
					Double.parseDouble(Utils.getLng().toString()));
			mMap.addMarker(new MarkerOptions().position(position)
					.title(Utils.getPlace().toString().split(",")[0])
					.snippet(Utils.getPlace().toString()));
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(position).zoom(16) // Sets the zoom
					.bearing(90).tilt(0).build();
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				// The Map is verified. It is now safe to manipulate the map.

			}
		}
	}

	@Override
	protected void onResume() {
		setUpMapIfNeeded();
		super.onResume();
	}

	/*private void cargarMarkersPHP() {
		try {
			JSONArray j = null;
			RemoteDB ini = new RemoteDB();
			ini.setmUrl(ini.getmUrl()+"NeoJav/Markers/consultarMarker.php");
			ini.start();
			boolean flag = false;
			Dialogo d = new Dialogo();
			while (ini.isAlive()) {
				if (!flag) {
					d.setButtons(1);
					d.setTitle("Conectando");
					d.setMessage("Conectando a la base de datos espere porfavor...");
					d.setPosMes("Ok");
					flag = true;
					d.show(getFragmentManager(), "Conectando");
				}
			}
			d.dismiss();
			if (ini.getResponse().length() > 1) {
				StringTokenizer st = new StringTokenizer(ini.getResponse(), "]");
				while (st.hasMoreTokens()) {
					j = new JSONArray(st.nextToken() + "]");
					mMap.addMarker(new MarkerOptions()
							.position(
									new LatLng(Double.parseDouble(j
											.getString(0)), Double
											.parseDouble(j.getString(1))))
							.title(j.getString(2))
							.snippet(j.getString(3))
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(BuscarLugar.this, DrawerActivity.class);
		intent.putExtra("position", 0);
		startActivity(intent);
		finish();
	}

	/*public String trimer(String s) {
		String ret = new String();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				ret += s.charAt(i);
			}
		}
		return ret;
	}*/

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(mDrawerLayout.isDrawerOpen(Gravity.START)){
			MapFragment m = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			FrameLayout f = (FrameLayout) findViewById(R.id.mapContainer);
			Display display = getWindowManager().getDefaultDisplay();
			m.getView().setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,Gravity.RIGHT));
			f.setPadding(0, 0, 0, 0);
		}
		else{
			MapFragment m = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			FrameLayout f = (FrameLayout) findViewById(R.id.mapContainer);
			Display display = getWindowManager().getDefaultDisplay();
			m.getView().setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth()*0.25), LayoutParams.MATCH_PARENT,Gravity.RIGHT));
			f.setPadding((int) (display.getWidth()*0.25), 0, 0, 0);
		}
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_settings:
			if (mDrawerLayout2.isDrawerOpen(Gravity.END)) {
				mDrawerLayout2.closeDrawer(Gravity.END);
				MapFragment m = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
				FrameLayout f = (FrameLayout) findViewById(R.id.mapContainer);
				Display display = getWindowManager().getDefaultDisplay();
				m.getView().setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				f.setPadding(0, 0, 0, 0);
			} else {
				mDrawerLayout2.openDrawer(Gravity.END);
				MapFragment m = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
				FrameLayout f = (FrameLayout) findViewById(R.id.mapContainer);
				Display display = getWindowManager().getDefaultDisplay();
				m.getView().setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth()*0.25), LayoutParams.MATCH_PARENT));
				f.setPadding(0, 0, 0, 0);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.buscar_lugar, menu);
		return true;
	}

	/*public void getLatLongFromAddress(String youraddress) {
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

	}*/

	public LocalDB getAdmin() {
		return admin;
	}

	public void setAdmin(LocalDB admin) {
		this.admin = admin;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private class DrawerItemClickListener2 implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem2(position);
		}
	}

	private void selectItem(int position) {
		if (position == 1) {
			mDrawerLayout.closeDrawer(mDrawerList);
			FrameLayout f = (FrameLayout) findViewById(R.id.mapContainer);
			f.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			Fragment fragment = new NuevaViewFragment();
			Bundle args = new Bundle();
			args.putInt(NuevaViewFragment.ARG_PLANET_NUMBER, position);
			fragment.setArguments(args);

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame2, fragment).commit();
			mDrawerList.setItemChecked(position, true);
			setTitle(s[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	private void selectItem2(int position) {
		if (position == 0) {

		} else {
			/*
			 * Fragment fragment = new PlanetFragment(); Bundle args = new
			 * Bundle(); args.putInt(PlanetFragment.ARG_PLANET_NUMBER,
			 * position); fragment.setArguments(args);
			 * 
			 * FragmentManager fragmentManager = getFragmentManager();
			 * fragmentManager.beginTransaction().replace(R.id.content_frame2,
			 * fragment).commit(); mDrawerList.setItemChecked(position, true);
			 * setTitle(s[position]); mDrawerLayout.closeDrawer(mDrawerList);
			 */
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class NuevaViewFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public NuevaViewFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			Intent in = new Intent(th, DrawerActivity.class);
			in.putExtra("position", i);
			startActivity(in);
			String title = getResources().getStringArray(R.array.drawer_array)[i];
			getActivity().setTitle(title);
			return rootView;
		}
	}
}
