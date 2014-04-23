package DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDB extends SQLiteOpenHelper implements ILocalDB{

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
    
    public void guardarUser(String user,String contrasena){
    	SQLiteDatabase bd=this.getWritableDatabase();
    	bd.execSQL("INSERT INTO localUser (username,contrasena) VALUES ('"+user+"','"+contrasena+"') "); 
    	bd.close();
    }
}
