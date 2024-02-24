package com.android.activity;

import android.Vietnalyze;
import android.app.Activity;
import android.util.Log;

public class Vinalyze extends Activity {
	@Override
	 public void onStart()
	    {
	       super.onStart();
	       Log.i("Flurry", "onStart second");
	       // your code
	       
	       //Vietnalyze
	       
	       Vietnalyze.onStartSession(this, "EU56N2MEJJZH77OULDK3");
	    }
	@Override
	    public void onStop()
	    {
	       super.onStop();
	       Log.i("Flurry", "onStop");
	       // your code
	       
	       Vietnalyze.onEndSession(this);
	    }

}
