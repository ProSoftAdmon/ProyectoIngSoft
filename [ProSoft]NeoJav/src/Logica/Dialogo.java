package Logica;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class Dialogo extends DialogFragment {

	private String title = new String("");
	private String message = new String("");
	private String posMes = new String("");
	private String negMes = new String(""); 
	private int buttons = 1;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		if(buttons==1){
			builder.setPositiveButton(posMes, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
		}
		else if(buttons == 2){
			builder.setNegativeButton(negMes, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					
				}
			});
		}
		Dialog dialog = builder.create();
		return dialog;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPosMes() {
		return posMes;
	}

	public void setPosMes(String posMes) {
		this.posMes = posMes;
	}

	public String getNegMes() {
		return negMes;
	}

	public void setNegMes(String negMes) {
		this.negMes = negMes;
	}

	public int getButtons() {
		return buttons;
	}

	public void setButtons(int buttons) {
		this.buttons = buttons;
	}
	
	
	
}
