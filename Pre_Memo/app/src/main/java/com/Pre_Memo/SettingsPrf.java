package com.Pre_Memo;
import android.app.*;
import android.os.*;
import android.preference.*;
import android.preference.Preference.*;
import android.content.*;

public class SettingsPrf extends PreferenceActivity {

	private SharedPreferences spf;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		spf = PreferenceManager.getDefaultSharedPreferences(this);
		if(!spf.getString(getString(R.string.K_Init),"").equals(StartUp.PREF_INIT_FLG)){
			InitSetings();
		}
		getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new SettingFrgm(this))
			.commit();
	}
	
	protected void InitSetings(){
		Init_Prf_Setting Initsets = new Init_Prf_Setting(this);
		Initsets.initPrefSet();
		
	}
} 
