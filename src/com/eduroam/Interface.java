package com.eduroam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Interface extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.view);
	
	    Toast.makeText(this, "interface", Toast.LENGTH_LONG).show();
	}

}
