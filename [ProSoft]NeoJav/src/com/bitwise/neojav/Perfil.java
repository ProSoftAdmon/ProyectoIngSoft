package com.bitwise.neojav;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import DataBase.LocalDB;
import Logica.IUtils;
import Logica.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Perfil extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] s;
	private static Context th;
	
	private static int RESULT_LOAD_IMAGE = 1;
	final int PIC_CROP = 2;
	
	private LocalDB ldb = new LocalDB(this,"local");
	private IUtils ut = new Utils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);
		th = Perfil.this;
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
		final TextView t1 = (TextView) findViewById(R.id.nombreApe);
		final TextView t2 = (TextView) findViewById(R.id.esta);
		TextView t3 = (TextView) findViewById(R.id.hours);
		TextView t4 = (TextView) findViewById(R.id.post);
		t1.setText(ldb.nombre()+" "+ldb.apellido());
		t2.setText(ldb.estado());
		t3.setText(String.valueOf(ldb.horas()));
		t4.setText(String.valueOf(ldb.no_post()));
		
		ImageView b = (ImageView) findViewById(R.id.imagenPerfil);
		if(!ldb.imagen().isEmpty() || ldb.imagen() == ""){
			try {
				URL url = new URL(ldb.imagen());
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input;
					input = connection.getInputStream();
				b.setImageBitmap(BitmapFactory.decodeStream(input));
	            b.getLayoutParams().height = 200;
	            b.getLayoutParams().width = 200;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);                 
                startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		ImageButton b1 = (ImageButton) findViewById(R.id.actuName);
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				final Dialog dialog = new Dialog(Perfil.this);
                dialog.setContentView(R.layout.custom_dialog);
                // Set dialog title
                dialog.setTitle("Cambiar Nombre");              
 
                dialog.show();
                 
                Button declineButton = (Button) dialog.findViewById(R.id.aceptarDialog);
                declineButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText ed = (EditText) dialog.findViewById(R.id.cambiarNom);
                        EditText ed2 = (EditText) dialog.findViewById(R.id.cambiarApe);
                        String e1 = new String(ed.getText().toString());
                        String e2 = new String(ed2.getText().toString());
                        System.out.println(e1);
                    	if(!e1.isEmpty() && !e2.isEmpty()){
	                        t1.setText(""+e1+" "+e2);
	                        ldb.actualizar("'"+e1+"'", "nombre", ldb.nombreUsuario());
	                        String param = new String("username="+ldb.nombreUsuario()+"&pic="+ldb.imagen()+"&no_post="+ldb.no_post()+"&horas="+ldb.horas()+"&estado="+ldb.estado()+"&nombre="+ldb.nombre()+"&apellido="+ldb.apellido());
	                        ut.actualizarDatos(getFragmentManager(), param);
	                        ldb.actualizar("'"+e2+"'", "apellido", ldb.nombreUsuario());
	                        param = new String("username="+ldb.nombreUsuario()+"&pic="+ldb.imagen()+"&no_post="+ldb.no_post()+"&horas="+ldb.horas()+"&estado="+ldb.estado()+"&nombre="+ldb.nombre()+"&apellido="+ldb.apellido());
	                        ut.actualizarDatos(getFragmentManager(), param);
	                        dialog.dismiss();                		
                    	}
                    	else{
                    		Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                        	toast.show();
                    	}
                    }
                });
			}
		});
		
		ImageButton b2 = (ImageButton) findViewById(R.id.actuEstado);
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(Perfil.this);
                dialog.setContentView(R.layout.custom_dialog2);
                // Set dialog title
                dialog.setTitle("Cambiar Estado");
                dialog.show();
                 
                Button declineButton = (Button) dialog.findViewById(R.id.aceptarDialog);   
                declineButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {                        
                        EditText ed = (EditText) dialog.findViewById(R.id.cambiarEst);
                        String e = new String(ed.getText().toString());
                    	if(!e.isEmpty()){
	                        t2.setText(e);
	                        ldb.actualizar("'"+e+"'", "estado", ldb.nombreUsuario());
	                        String param = new String("username="+ldb.nombreUsuario()+"&pic="+ldb.imagen()+"&no_post="+ldb.no_post()+"&horas="+ldb.horas()+"&estado="+ldb.estado()+"&nombre="+ldb.nombre()+"&apellido="+ldb.apellido());
	                        ut.actualizarDatos(getFragmentManager(), param);
	                        dialog.dismiss();
                    	}
                    	else{
                    		Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                        	toast.show();
                    	}
                    }
                });
				
			}
		});
	}

	/* (non-Javadoc)
	 * Metodo listener al presionar el boton atras
	 * @see android.app.Activity#onBackPressed()
	 */
	public void onBackPressed() {
		Intent intent = new Intent(Perfil.this, DrawerActivity.class);
		intent.putExtra("position", 0);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perfil, menu);
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
		if (position == 3) {
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
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);        
        ImageView imageView = (ImageView) findViewById(R.id.imagenPerfil);
        
        /*if (requestCode == PIC_CROP) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                imageView.setImageBitmap(selectedBitmap);
            }
        }*/
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            if(bm.getWidth() > 2048 && bm.getHeight() > 2048){
            	Toast toast = Toast.makeText(getApplicationContext(), "Imagen muy grande, Seleccione otra", Toast.LENGTH_SHORT);
            	toast.show();
            	Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);                 
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            	/*Intent cropIntent = new Intent("com.android.camera.action.CROP");
                cropIntent.setDataAndType(Uri.fromFile(new File(picturePath)), "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("outputX", 2048);
                cropIntent.putExtra("outputY", 2048);
                cropIntent.putExtra("return-data", true);
                startActivityForResult(cropIntent, PIC_CROP);*/
            }
            imageView.setImageBitmap(bm);
            imageView.getLayoutParams().height = 200;
            imageView.getLayoutParams().width = 200;
            File file = new File(picturePath);
            if (file.exists()) {
                UploaderFoto nuevaTarea = new UploaderFoto();
                nuevaTarea.execute(picturePath);
            }
            ldb.actualizar("'http://omargonzalez.dx.am/NeoJav/User/Images/"+file.getName()+"'", "imagen", ldb.nombreUsuario());
            String param = new String("username="+ldb.nombreUsuario()+"&pic="+ldb.imagen()+"&no_post="+ldb.no_post()+"&horas="+ldb.horas()+"&estado="+ldb.estado()+"&nombre="+ldb.nombre()+"&apellido="+ldb.apellido());
            ut.actualizarDatos(getFragmentManager(), param);         
        }     
    }
	
	class UploaderFoto extends AsyncTask<String, Void, Void>{
		 
	    ProgressDialog pDialog;
	    String miFoto = "";

	    protected Void doInBackground(String... params) {
	        miFoto = params[0];
	        try {
	            HttpClient httpclient = new DefaultHttpClient();
	            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	            HttpPost httppost = new HttpPost("http://omargonzalez.dx.am/NeoJav/User/upload.php");
	            File file = new File(miFoto);
	            MultipartEntity mpEntity = new MultipartEntity();
	            ContentBody foto = new FileBody(file, "image/*");
	            mpEntity.addPart("fotoUp", foto);
	            httppost.setEntity(mpEntity);
	            httpclient.execute(httppost);
	            httpclient.getConnectionManager().shutdown();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(Perfil.this);
	        pDialog.setMessage("Subiendo la imagen, espere." );
	        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }
	    protected void onPostExecute(Void result) {
	        super.onPostExecute(result);
	        pDialog.dismiss();
	    }
	}
}
