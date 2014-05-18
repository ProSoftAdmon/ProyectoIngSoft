package Logica;

import java.util.List;

import org.json.JSONArray;

import android.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;

public interface IUtils {
	
	public String cargarDirectorio(FragmentManager fM);
	
	public void cargarMarkersPHP(FragmentManager fM,GoogleMap mMap,String parametro);

	public void getLatLongFromAddress(String youraddress);
	
	public String trimer(String s);

	public List<JSONArray> cargarMuro(FragmentManager fragmentManager);
	
	public String publicar(FragmentManager fM,String param);
	
	public String actualizarDatos(FragmentManager fM,String param);
	
	public String[] obtenerUser(FragmentManager fM,String username);
}
