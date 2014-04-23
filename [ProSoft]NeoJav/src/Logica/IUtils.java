package Logica;

import android.app.FragmentManager;
import com.google.android.gms.maps.GoogleMap;

public interface IUtils {
	
	public String cargarDirectorio(FragmentManager fM);
	
	public void cargarMarkersPHP(FragmentManager fM,GoogleMap mMap);

	public void getLatLongFromAddress(String youraddress);
	
	public String trimer(String s);
	
	
}
