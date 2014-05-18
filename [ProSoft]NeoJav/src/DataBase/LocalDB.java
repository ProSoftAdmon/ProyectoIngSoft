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
        db.execSQL("CREATE TABLE IF NOT EXISTS localUser(username text, contrasena REAL, nombre text,apellido text,imagen text DEFAULT '',estado text DEFAULT '',horas int,no_post int,PRIMARY KEY (username))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists localUser");
        db.execSQL("CREATE TABLE localUser(username text, contrasena REAL, nombre text,apellido text,imagen text DEFAULT '',estado text DEFAULT '',horas int,no_post int,PRIMARY KEY (username))");        
    } 
    
    public void dropTable(){
    	SQLiteDatabase bd=this.getWritableDatabase();
        bd.execSQL("drop table if exists localUser");
        bd.close();
    }
    
    public void createTable(){
    	SQLiteDatabase bd=this.getWritableDatabase();
        bd.execSQL("CREATE TABLE IF NOT EXISTS localUser(username text, contrasena REAL, nombre text,apellido text,imagen text DEFAULT '',estado text DEFAULT '',horas int,no_post int,PRIMARY KEY (username))");
        bd.close();
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
    
    public String nombreUsuario(){
    	String s = new String();
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select username from localUser",null);
        if(fila.moveToFirst()){
        	s = new String(fila.getString(0));
        }
        bd.close();
    	return s;
    }
    
    public String estado(){
    	String s = new String();
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select estado from localUser",null);
        if(fila.moveToFirst()){
        	s = new String(fila.getString(0));
        }
        bd.close();
    	return s;
    }
    
    public String imagen(){
    	String s = new String();
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select imagen from localUser",null);
        if(fila.moveToFirst()){
        	s = new String(fila.getString(0));
        }
        bd.close();
    	return s;
    }
    
    public String nombre(){
    	String s = new String();
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select nombre from localUser",null);
        if(fila.moveToFirst()){
        	s = new String(fila.getString(0));
        }
        bd.close();
    	return s;
    }
    
    public String apellido(){
    	String s = new String();
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select apellido from localUser",null);
        if(fila.moveToFirst()){
        	s = new String(fila.getString(0));
        }
        bd.close();
    	return s;
    }
    
    public int horas(){
    	int s = 0;
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select horas from localUser",null);
        if(fila.moveToFirst()){
        	s =fila.getInt(0);
        }
        bd.close();
    	return s;
    }
    
    public int no_post(){
    	int s = 0;
    	SQLiteDatabase bd=this.getWritableDatabase();
        Cursor fila=bd.rawQuery("select no_post from localUser",null);
        if(fila.moveToFirst()){
        	s = fila.getInt(0);
        }
        bd.close();
    	return s;
    }
    
    public void actualizar(String param,String act,String user)
    {
    	SQLiteDatabase bd=this.getWritableDatabase();
    	bd.execSQL("UPDATE localUser SET " + act + "=" + param + " WHERE username='"+user+"'"); 
    	bd.close();
    }
    
    public void guardarUser(String user,String contrasena){
    	SQLiteDatabase bd=this.getWritableDatabase();
    	bd.execSQL("INSERT INTO localUser (username,contrasena) VALUES ('"+user+"','"+contrasena+"') "); 
    	bd.close();
    }
    
    public void guardarUserName(String user,String contrasena,String name,String lastnam){
    	SQLiteDatabase bd=this.getWritableDatabase();
    	bd.execSQL("INSERT INTO localUser (username,contrasena,nombre,apellido) VALUES ('"+user+"','"+contrasena+"','"+name+"','"+lastnam+"') "); 
    	bd.close();
    }
    
    public void borrarUser(){
    	SQLiteDatabase bd=this.getWritableDatabase();
    	bd.delete("localUser", "", null);
    	bd.close();
    }
    
    public void insertarPerfil(String img,String est,int horas,int no_post,String nombre,String apellido,String user){
    	SQLiteDatabase bd=this.getWritableDatabase();
    	bd.execSQL("UPDATE localUser SET imagen='"+img+"',estado='"+est+"',horas="+horas+",no_post="+no_post+",nombre='"+nombre+"',apellido='"+apellido+"' WHERE username='"+user+"'"); 
    	bd.close();
    }
}
