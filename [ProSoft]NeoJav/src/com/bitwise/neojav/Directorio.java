package com.bitwise.neojav;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;

import com.bitwise.neojav.BuscarLugar.NuevaViewFragment;
import com.bitwise.neojav.DrawerActivity.PlanetFragment;

import Logica.IUtils;
import Logica.Utils;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Directorio extends Activity {

	private IUtils ut = new Utils();
	private ListView mList;
	private ArrayList<Contacto> contactos = new ArrayList<Contacto>();
	private String[] contac;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] s;
	private static Context th;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directorio);
		setData();
		mList = (ListView) findViewById(R.id.listcontac);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, contac,
				contactos);
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(new DirectorioItemClickListener());
		th = Directorio.this;
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout3);
		mDrawerList = (ListView) findViewById(R.id.left_drawer3);
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
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItemDrawer(2);
		}
	}
	
	public void onBackPressed() {
		Intent intent = new Intent(Directorio.this, DrawerActivity.class);
		intent.putExtra("position", 0);
		startActivity(intent);
		finish();
	}

	public void setData() {
		contactos.clear();

		try {
			String ini = ut.cargarDirectorio(this.getFragmentManager());
			JSONArray j = null;
			contac = new String[ini.length()];
			int i = 0;
			if (ini.length() > 1) {
				StringTokenizer st = new StringTokenizer(ini, "]");
				while (st.hasMoreTokens()) {
					Contacto c = new Contacto();
					j = new JSONArray(st.nextToken() + "]");
					c.setIndice(j.getString(0));
					c.setDependencia(j.getString(1));
					c.setCargo(j.getString(2));
					c.setNombre(j.getString(3));
					c.setApellido(j.getString(4));
					c.setExtension(Integer.parseInt(j.getString(5)));
					contactos.add(c);
					contac[i] = "" + j.getString(0) + " " + j.getString(1)
							+ "|" + j.getString(3) + " " + j.getString(4) + " "
							+ j.getString(2) + "|" + j.getString(5);
					i++;
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.directorio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DirectorioItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public void selectItem(int position) {
		String numero = contactos.get(position).getPbx();
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + numero));
		startActivity(callIntent);
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
		private ArrayList<Contacto> contactos;

		@Override
		public int getCount() {
			return contactos.size();
		}

		public MySimpleArrayAdapter(Context context, String[] values,
				ArrayList<Contacto> contactos) {
			super(context, R.layout.directorio_list_item, values);
			this.context = context;
			this.values = values;
			this.contactos = contactos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = null;
			if (position < contactos.size()) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.directorio_list_item,
						parent, false);
				TextView indice = (TextView) rowView.findViewById(R.id.id);
				TextView nombre = (TextView) rowView.findViewById(R.id.nombre);
				TextView extension = (TextView) rowView.findViewById(R.id.ext);
				indice.setText(contactos.get(position).getIndice());
				nombre.setText(contactos.get(position).getNombre() + " "
						+ contactos.get(position).getApellido() + " "
						+ contactos.get(position).getCargo());
				extension
						.setText("Extension: "
								+ String.valueOf(contactos.get(position)
										.getExtension()));
			}

			return rowView;
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItemDrawer(position);
		}
	}

	private void selectItemDrawer(int position) {
		if (position == 2) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Fragment fragment = new NuevaViewFragment();
			Bundle args = new Bundle();
			args.putInt(NuevaViewFragment.ARG_PLANET_NUMBER, position);
			fragment.setArguments(args);

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame3, fragment).commit();
			mDrawerList.setItemChecked(position, true);
			setTitle(s[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
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
