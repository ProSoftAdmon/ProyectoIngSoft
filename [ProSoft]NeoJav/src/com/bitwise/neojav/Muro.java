package com.bitwise.neojav;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import DataBase.LocalDB;
import Logica.IUtils;
import Logica.Utils;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Muro extends Activity {

	private IUtils ut = new Utils();
	private ListView mList;
	private ArrayList<Contacto> contactos = new ArrayList<Contacto>();
	private ArrayList<Publicacion> publicaciones = new ArrayList<Publicacion>();
	private String[] contac = new String[1];

    private LocalDB sqlite;
	
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
		setContentView(R.layout.neojav_main);
		setData();
		th = Muro.this;
		ut.cargarMuro(getFragmentManager());
		sqlite = new LocalDB(Muro.this, "local");
		mList = (ListView) findViewById(R.id.publicaciones);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,contac,publicaciones);
		mList.setAdapter(adapter); 
		mList.setOnItemClickListener(new MuroItemClickListener());
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout4);
		mDrawerList = (ListView) findViewById(R.id.left_drawer4);
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
		ImageButton b = (ImageButton) findViewById(R.id.publicar);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText t = (EditText) findViewById(R.id.textoMuro);
				if(!t.getText().toString().isEmpty()){
					Date horaActual=new Date();
					String fecha = (horaActual.getYear()+1900)+"-"+(horaActual.getMonth()+1)+"-"+horaActual.getDate();
					String hora = horaActual.getHours()+":"+horaActual.getMinutes()+":"+horaActual.getSeconds();
					String param = new String(t.getText().toString()+"&categoria=Lugar%20De%20Ocio&autor="+sqlite.nombreUsuario()+"&fecha="+fecha+"&hora="+hora);
					ut.publicar(getFragmentManager(),param);
					setData();
					((BaseAdapter) mList.getAdapter()).notifyDataSetChanged();
					t.setText("");
				}
			}
		});
		if (savedInstanceState == null) {
			selectItemDrawer(0);
		}
	}
	
	/**
	 * Metodo que carga la informacion del muro
	 */
	public void setData() {
		contactos.clear();
		publicaciones.clear();
		try {
			List<JSONArray> publis = ut.cargarMuro(this.getFragmentManager());
			for(int i=0;i<publis.size();i++){
				Publicacion p = new Publicacion();
				p.setAutor(publis.get(i).getString(5));
				p.setFecha(publis.get(i).getString(2));
				p.setHora(publis.get(i).getString(3));
				//p.setImagen(publis.get(i).getString(4));
				p.setMensaje(publis.get(i).getString(1));
				p.setCategoria(publis.get(i).getString(6));
				publicaciones.add(p);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clase encargada de responder al evento de click del menu
	 */
	private class MuroItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/**
	 * Metodo que ejecuta una accion por cada uno de los items del menu
	 * @param position item seleccionado
	 */
	public void selectItem(int position) {
		/*String numero = contactos.get(position).getPbx();
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + numero));
		startActivity(callIntent);*/
	}

	/**
	 * Clase encargada del manejo de lista dentro de la actividad
	 */
	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
		private ArrayList<Publicacion> contactos;

		@Override
		public int getCount() {
			return contactos.size();
		}

		public MySimpleArrayAdapter(Context context, String[] values,
				ArrayList<Publicacion> contactos) {
			super(context, R.layout.muro_list_item, values);
			this.context = context;
			this.values = values;
			this.contactos = contactos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = null;
			if (position < contactos.size()) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.muro_list_item, parent, false);
				ImageView perfil = (ImageView) rowView.findViewById(R.id.imagenPerfil);
				TextView userPub = (TextView) rowView.findViewById(R.id.NombreUserPub);
				TextView horaPub = (TextView) rowView.findViewById(R.id.HoraPub);
				TextView mensaje = (TextView) rowView.findViewById(R.id.MensajePub);
				//ImageView imagenOp = (ImageView) rowView.findViewById(R.id.imagenPub);
				TextView etiquetas = (TextView) rowView.findViewById(R.id.EtiquetasPub);
				//perfil.setImageBitmap(contactos.get(position).getImagen());
				userPub.setText(contactos.get(position).getAutor());
				horaPub.setText(contactos.get(position).getFecha() + " " + contactos.get(position).getHora());
				mensaje.setText(contactos.get(position).getMensaje());
				etiquetas.setText(contactos.get(position).getCategoria());
			}
			return rowView;
		}

		public String[] getValues() {
			return values;
		}
	}
	
	/* (non-Javadoc)
	 * Metodo listener al presionar el boton atras
	 * @see android.app.Activity#onBackPressed()
	 */
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.muro, menu);
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

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItemDrawer(position);
		}
	}

	private void selectItemDrawer(int position) {
		if (position == 0) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Fragment fragment = new NuevaViewFragment();
			Bundle args = new Bundle();
			args.putInt(NuevaViewFragment.ARG_PLANET_NUMBER, position);
			fragment.setArguments(args);

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame4, fragment).commit();
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
