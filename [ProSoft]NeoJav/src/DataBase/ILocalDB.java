package DataBase;

public interface ILocalDB {

	public String consultarUser();
    
    public void guardarUser(String user,String contrasena);
	
    public void borrarUser();
}
