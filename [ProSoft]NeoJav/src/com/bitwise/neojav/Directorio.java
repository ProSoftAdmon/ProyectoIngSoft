package com.bitwise.neojav;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;

import Logica.IUtils;
import Logica.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Directorio extends Activity {

	private IUtils ut = new Utils();
	private ListView mList;
	private ArrayList<Contacto> contactos = new ArrayList<Contacto>();
	private String[] contac;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directorio);
		setData();
		mList = (ListView) findViewById(R.id.listcontac);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, contac,contactos);
	    mList.setAdapter(adapter);
		mList.setOnItemClickListener(new DirectorioItemClickListener());
	}

	public void setData() {
		contactos.clear();

		Contacto c = new Contacto();

		try {
			String ini = ut.cargarDirectorio(this.getFragmentManager());
			JSONArray j = null;
			contac = new String[ini.length()];
			long i = 0;
			if (ini.length() > 1) {
				StringTokenizer st = new StringTokenizer(ini, "]");
				while (st.hasMoreTokens()) {
					j = new JSONArray(st.nextToken() + "]");
					c.setIndice(j.getString(0));
					c.setDependencia(j.getString(1));
					c.setCargo(j.getString(2));
					c.setNombre(j.getString(3));
					c.setApellido(j.getString(4));
					c.setExtension(Integer.parseInt(j.getString(5)));
					contactos.add(c);
					contac[(int) i]=""+j.getString(0)+" "+j.getString(1)+"|"+j.getString(3)+" "+j.getString(4)+" "+j.getString(2)+"|"+j.getString(5);
					i++;
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.directorio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class DirectorioItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

	public void selectItem(int position) {
		String numero = contactos.get(position).getPbx();
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:"+numero));
        startActivity(callIntent);
	}
	
	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
		private ArrayList<Contacto> contactos;

		public MySimpleArrayAdapter(Context context, String[] values,ArrayList<Contacto> contactos) {
			super(context, R.layout.directorio_list_item, values);
			this.context = context;
			this.values = values;
			this.contactos = contactos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.directorio_list_item,
					parent, false);
			TextView indice = (TextView) rowView.findViewById(R.id.id);
			TextView nombre = (TextView) rowView.findViewById(R.id.nombre);
			TextView extension = (TextView) rowView.findViewById(R.id.ext);
			indice.setText(contactos.get(position).getIndice());
			nombre.setText(contactos.get(position).getNombre()+" "+contactos.get(position).getApellido()+" "+contactos.get(position).getCargo());
			extension.setText(String.valueOf(contactos.get(position).getExtension()));

			return rowView;
		}
	}

}
