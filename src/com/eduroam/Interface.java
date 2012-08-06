package com.eduroam;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class Interface extends TabActivity {

	TabHost myTabHost;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab);
	    myTabHost = getTabHost();
        myTabHost.setup();
        TabHazirla();
	   
	}
private void TabHazirla() {
		
		TabSpec tsAndroid = myTabHost.newTabSpec("and");
		
		//tsAndroid.setIndicator("Android",getResources().getDrawable(R.drawable.android));
		
		tsAndroid.setIndicator("Connection");
		Intent intentAndroid = new Intent(this,ConnectionTab.class);
	
		
		tsAndroid.setContent(intentAndroid);
		
		myTabHost.addTab(tsAndroid);
		
		
		TabSpec tsJava = myTabHost.newTabSpec("Configurations");
		//tsJava.setIndicator("Java",getResources().getDrawable(R.drawable.java));
		tsJava.setIndicator("Configurations");
		
		Intent intentJava = new Intent(this,Configurations.class);
		
		tsJava.setContent(intentJava);
		
		myTabHost.addTab(tsJava);
		/*
		TabSpec tsKariyer = myTabHost.newTabSpec("kariyer");
		tsKariyer.setIndicator("Kariyer",getResources().getDrawable(R.drawable.kariyer));
		
		Intent intentKAriyer = new Intent(this,KariyerAct.class);
		
		tsKariyer.setContent(intentKAriyer);
		
		myTabHost.addTab(tsKariyer);
		*/
	}
	

}
