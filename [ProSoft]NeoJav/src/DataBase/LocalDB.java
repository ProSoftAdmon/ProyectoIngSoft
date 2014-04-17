package DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocalDB extends SQLiteOpenHelper {

    public LocalDB(Context context, String nombre) {
        super(context, nombre, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS localUser(username text, contrasena REAL, nombre text,PRIMARY KEY (username))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists localUser");
        db.execSQL("CREATE TABLE localUser(username text, contrasena REAL, nombre text,PRIMARY KEY (username))");        
    } 
    
    public String consultarUser(){
    	String s = new String();
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select username from localUser",null);
        if(fila.moveToFirst()){
        	s = new String("si");
        }
        bd.close();
    	return s;
    }
    
    public void cargarMarkers(GoogleMap mMap){
        SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select * from markers",null);
        fila.moveToFirst();
        while (!fila.isAfterLast()) {
        	System.out.println(fila.getString(0)+ fila.getString(1));
        	mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Double.parseDouble(fila.getString(0)), Double.parseDouble(fila.getString(1))))
					.title(fila.getString(2))
					.snippet(fila.getString(3))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            fila.moveToNext();
        }
        bd.close();
    }
    
    public void guardarUser(String user,String contrasena){
    	SQLiteDatabase bd=this.getWritableDatabase();
    	bd.execSQL("INSERT INTO localUser (username,contrasena) VALUES ('"+user+"','"+contrasena+"') "); 
    	bd.close();
    }
    
    /*public void copyDataBase(){
    	try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            //if (sd.canWrite()) {
                String currentDBPath = "//data//"+ "com.bitwise.neojav//databases//administracion";
                String backupDBPath = "administracion";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd+"/backups/", backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                System.out.println(currentDB.getAbsolutePath()+"||"+backupDB.getAbsolutePath());
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

           // }
        } catch (Exception e) {
        	System.out.println("error" + e.getMessage());
        }
    }*/
}
