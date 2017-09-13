package com.Pre_Memo;

import android.preference.*;
import android.preference.Preference.OnPreferenceClickListener;
import android.os.*;
import android.content.*;
import java.util.*;
import android.app.*;
import android.widget.*;
import com.mycompany.Multi_Dialog.*;
import android.support.v7.app.*;
import android.support.v7.app.AlertDialog;
import java.io.*;

public class SettingFrgm extends PreferenceFragment{
	
	private SharedPreferences spf;
	private Activity ThisActi;
	private String ErrDsp;
	
	//const
	SettingFrgm(Activity activity){
		//Nothing
		ThisActi=activity;
		ErrDsp=ThisActi.getString(R.string.ErrDsp);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		// fragment再生成抑止
		setRetainInstance(true);
		addPreferencesFromResource(R.xml.settings);
		spf = PreferenceManager.getDefaultSharedPreferences(ThisActi);
		
	}

	
	@Override
	public void onResume(){
		// TODO: Implement this method
		super.onResume();
		spf.registerOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public void onPause(){
		// TODO: Implement this method
		super.onPause();
		spf.unregisterOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		loadPref();
		setPrefListeners();
		
	}


	private void loadPref(){
		findPreference(getString(R.string.K_Startup)).setSummary(spf.getString(getString(R.string.K_Startup),ErrDsp));
		findPreference(getString(R.string.K_Odr1)).setSummary(spf.getString(getString(R.string.K_Odr1),ErrDsp));
		findPreference(getString(R.string.K_Odr1_by)).setSummary(spf.getString(getString(R.string.K_Odr1_by),ErrDsp));
		findPreference(getString(R.string.K_Odr2)).setSummary(spf.getString(getString(R.string.K_Odr2),ErrDsp));
		findPreference(getString(R.string.K_Odr2_by)).setSummary(spf.getString(getString(R.string.K_Odr2_by),ErrDsp));
		setLpEntry((ListPreference)findPreference(getString(R.string.K_Odr1)),spf.getString(getString(R.string.K_Odr2),ErrDsp));
		setLpEntry((ListPreference)findPreference(getString(R.string.K_Odr2)),spf.getString(getString(R.string.K_Odr1),ErrDsp));
		if(spf.getBoolean(getString(R.string.K_rcv_Btn),false)){
			findPreference(getString(R.string.K_rcv_Btn))
			.setSummary(spf.getString(getString(R.string.K_rcv_Btn_Date),getString(R.string.RcvErrMsg)));
		}else{
			findPreference(getString(R.string.K_rcv_Btn)).setSummary(getString(R.string.RcvErrMsg));
		}
		
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener listener = 
		new SharedPreferences.OnSharedPreferenceChangeListener(){

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sp1, String pkey){
			// TODO: Implement this method
			if(pkey.equals(getString(R.string.K_Startup))){
				findPreference(pkey).setSummary(sp1.getString(pkey,ErrDsp));
			}else if(pkey.equals(getString(R.string.K_Odr1))){
				findPreference(pkey).setSummary(sp1.getString(pkey,ErrDsp));
				setLpEntry((ListPreference)findPreference(getString(R.string.K_Odr2)),sp1.getString(pkey,ErrDsp));
			}else if(pkey.equals(getString(R.string.K_Odr2))){
				findPreference(pkey).setSummary(sp1.getString(pkey,ErrDsp));
				setLpEntry((ListPreference)findPreference(getString(R.string.K_Odr1)),sp1.getString(pkey,ErrDsp));
			}else if(pkey.equals(getString(R.string.K_Odr1_by))){
				findPreference(pkey).setSummary(sp1.getString(pkey,ErrDsp));
			}else if(pkey.equals(getString(R.string.K_Odr2_by))){
				findPreference(pkey).setSummary(sp1.getString(pkey,ErrDsp));
			}
		}
	};
	
	private void setLpEntry(ListPreference lp,String SelectedStr){
		lp.setEntries(rmvStrsAry(getResources().getStringArray(R.array.entries_Odr),SelectedStr));
		lp.setEntryValues(rmvStrsAry(getResources().getStringArray(R.array.entries_Odr),SelectedStr));
	}
	//
	private String[] rmvStrsAry(String[] StrAry,String rvmStr){
		String[] RtnStrAry = new String[StrAry.length-1];
		int j=0;
		for(int i=0;i<StrAry.length;i++){
			if(!StrAry[i].equals(rvmStr)){
				RtnStrAry[j]=StrAry[i];
				j++;
			}
		}
		return RtnStrAry;
	}
	//Back Button Listener
	private void setPrefListeners(){
		
		//Reset Settings Click Event
		findPreference(getString(R.string.K_Set_Def))
			.setOnPreferenceClickListener(new OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1){
					AlertDialog.Builder builder = new AlertDialog.Builder(ThisActi);
					builder.setTitle(R.string.Title_Set_Def)
						.setMessage(R.string.PreSetDefMsg);
					builder.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
								Init_Prf_Setting Initpref = new Init_Prf_Setting(ThisActi);
								Initpref.initPrefSet();
								ThisActi.finish();
								Toast.makeText(ThisActi,getString(R.string.SetDefFinMsg),Toast.LENGTH_SHORT).show();
							}
						});
					builder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							// User clicked Cancel button
								Toast.makeText(ThisActi,getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
							}
						});

					builder.show();
					
					//Toast.makeText(ThisActi,p1.getKey()+" DA YO !",Toast.LENGTH_SHORT).show();
					return false;
				}
		});
		
		//recover Preference Click Event
		findPreference(getString(R.string.K_rcv_Btn))
			.setOnPreferenceClickListener(new OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference p1){
				boolean RcvFlg = spf.getBoolean(p1.getKey(),false);
				if(RcvFlg){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(ThisActi);
					builder.setTitle(R.string.Title_Rcv)
						.setMessage(R.string.PreRcvMsg);
					builder.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
							int rtnCD=0;
							File_OpeS fos = new File_OpeS(ThisActi.getApplicationContext());
							File SrcF = new File(ThisActi.getFilesDir().getParent() + "/databases/"+fos.PrevStr+DBAdapter.DB_NAME);
							File DstF = new File(ThisActi.getFilesDir().getParent() + "/databases/"+DBAdapter.DB_NAME);
								
							rtnCD=fos.ExistDelCopy(SrcF,DstF,4);
							switch(rtnCD){
								case 0:
								case 1:
									Toast.makeText(ThisActi,getString(R.string.RcvFinMsg),Toast.LENGTH_SHORT).show();
									break;
								default:
									Toast.makeText(ThisActi,getString(R.string.RcvRsltErrMsg)+rtnCD,Toast.LENGTH_SHORT).show();
							}
							Toast.makeText(ThisActi,getString(R.string.RcvFinMsg),Toast.LENGTH_SHORT).show();

							}
						});
					builder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Toast.makeText(ThisActi,getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
						}
					});
						
					builder.show();
					//start process here !
				}else{
					Toast.makeText(ThisActi,getString(R.string.RcvErrMsg),Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
		
		//To Back Preference Click Event
		findPreference(getString(R.string.K_back_Btn))
			.setOnPreferenceClickListener(new OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference p1){
				//Toast.makeText(ThisActi,p1.getKey(),Toast.LENGTH_SHORT).show();
				ThisActi.finish();
				return false;
			}
		});
	}
	
	
	
	
}
