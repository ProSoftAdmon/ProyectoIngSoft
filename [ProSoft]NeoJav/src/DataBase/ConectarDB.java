package DataBase;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class ConectarDB extends Thread {

	private String response;
	private String mURL;
	
	public ConectarDB() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		response = "";
		mURL = mURL.replace(" ", "%20");
		Log.i("LocAndroid Response HTTP Threas", "Ejecutando get 0: " + mURL);
		HttpClient httpclient = new DefaultHttpClient();

		Log.i("LocAndroid Response HTTP Thread", "Ejecutando get 1");
		HttpGet httppost = new HttpGet(mURL);
		Log.i("LocAndroid Response HTTP Thread", "Ejecutando get 2");
		try {
			Log.i("LocAndroid Response HTTP", "Ejecutando get");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = httpclient.execute(httppost, responseHandler);
			Log.i("LocAndroid Response HTTP", response);
		} catch (ClientProtocolException e) {
			Log.i("LocAndroid Response HTTP ERROR 1", e.getMessage());
		} catch (IOException e) {
			Log.i("LocAndroid Response HTTP ERROR 2", e.getMessage());
		}
		super.run();
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getmUrl() {
		return mURL;
	}

	public void setmUrl(String mUrl) {
		this.mURL = mUrl;
	}
	
}
