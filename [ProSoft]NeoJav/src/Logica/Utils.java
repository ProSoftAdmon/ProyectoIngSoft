package Logica;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
import android.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Utils implements IUtils{
	
	private static Object lat;
	private static Object lng;
	private static Object place;
	private List<JSONArray> js = new ArrayList<JSONArray>();
	
	public String[] obtenerUser(FragmentManager fM,String username){
		try {
			String[] js = new String[7];
			JSONArray j = null;
			RemoteDB ini = new RemoteDB();
			ini.setmUrl(ini.getmUrl()+"NeoJav/User/getUser.php?usuario="+username);
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
					d.show(fM, "Conectando");
				}
			}
			d.dismiss();
			if (ini.getResponse().length() > 1) {
				StringTokenizer st = new StringTokenizer(ini.getResponse(), "]");
				while (st.hasMoreTokens()) {
					j = new JSONArray(st.nextToken() + "]");
					js[0] = j.getString(0);
					js[1] = j.getString(1);
					js[2] = j.getString(2);
					js[3] = j.getString(3);
					js[4] = j.getString(4);
					js[5] = j.getString(5);
					js[6] = j.getString(6);
				}
			}
			return js;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String actualizarDatos(FragmentManager fM,String param)
	{
		try {
			RemoteDB ini = new RemoteDB();
			ini.setmUrl(ini.getmUrl()+"NeoJav/User/actualizar.php?"+param);
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
					d.show(fM, "Conectando");
				}
			}
			d.dismiss();
			return ini.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String cargarDirectorio(FragmentManager fM)
	{
		try {
			RemoteDB ini = new RemoteDB();
			ini.setmUrl(ini.getmUrl()+"NeoJav/Contacto/consultarDirectorio.php");
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
					d.show(fM, "Conectando");
				}
			}
			d.dismiss();
			return ini.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<JSONArray> cargarMuro(FragmentManager fM){
		try {
			JSONArray j = null;
			RemoteDB ini = new RemoteDB();
			ini.setmUrl(ini.getmUrl()+"NeoJav/Muro/consultarMuro.php");
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
					d.show(fM, "Conectando");
				}
			}
			d.dismiss();
			if (ini.getResponse().length() > 1) {
				StringTokenizer st = new StringTokenizer(ini.getResponse(), "]");
				while (st.hasMoreTokens()) {
					j = new JSONArray(st.nextToken() + "]");
					js.add(j);
				}
			}
			return js;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String publicar(FragmentManager fM,String param){
		try {
			RemoteDB ini = new RemoteDB();
			ini.setmUrl(ini.getmUrl()+"NeoJav/Muro/publicar.php?mensaje="+param);
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
					d.show(fM, "Conectando");
				}
			}
			d.dismiss();
			return ini.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void cargarMarkersPHP(FragmentManager fM,GoogleMap mMap,String param) {
		try {
			float color = 0;
			
			JSONArray j = null;
			RemoteDB ini = new RemoteDB();
			ini.setmUrl(ini.getmUrl()+"NeoJav/Markers/consultarMarker.php?categoria="+param);
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
					d.show(fM, "Conectando");
				}
			}
			d.dismiss();
			if (ini.getResponse().length() > 1) {
				StringTokenizer st = new StringTokenizer(ini.getResponse(), "]");
				String parametro = new String();
				while (st.hasMoreTokens()) {
					j = new JSONArray(st.nextToken() + "]");
					parametro = j.getString(4);
					if(parametro.equals("Auditorios"))
						color =BitmapDescriptorFactory.HUE_GREEN;
					else if(parametro.equals("Cafeterias"))
						color = BitmapDescriptorFactory.HUE_ORANGE;
					else if(parametro.equals("Capillas"))
						color = BitmapDescriptorFactory.HUE_AZURE;
					else if(parametro.equals("Computadores"))
						color = BitmapDescriptorFactory.HUE_BLUE;
					else if(parametro.equals("Tienda Javeriana"))
						color = BitmapDescriptorFactory.HUE_RED;
					else if(parametro.equals("Cajeros"))
						color = BitmapDescriptorFactory.HUE_CYAN;
					else if(parametro.equals("Edificios"))
						color = BitmapDescriptorFactory.HUE_ORANGE;
					else if(parametro.equals("LugaresDeOcio"))
						color = BitmapDescriptorFactory.HUE_MAGENTA;
					else if(parametro.equals("Tiendas"))
						color = BitmapDescriptorFactory.HUE_YELLOW;
					else if(parametro.equals("Restaurantes"))
						color = BitmapDescriptorFactory.HUE_VIOLET;
					mMap.addMarker(new MarkerOptions()
							.position(
									new LatLng(Double.parseDouble(j
											.getString(0)), Double
											.parseDouble(j.getString(1))))
							.title(j.getString(2))
							.snippet(j.getString(3))
							.icon(BitmapDescriptorFactory
									.defaultMarker(color)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
	
	public String trimer(String s) {
		String ret = new String();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				ret += s.charAt(i);
			}
		}
		return ret;
	}

	public static Object getLat() {
		return lat;
	}

	public static void setLat(Object lat) {
		Utils.lat = lat;
	}

	public static Object getLng() {
		return lng;
	}

	public static void setLng(Object lng) {
		Utils.lng = lng;
	}

	public static Object getPlace() {
		return place;
	}

	public static void setPlace(Object place) {
		Utils.place = place;
	}	
}
