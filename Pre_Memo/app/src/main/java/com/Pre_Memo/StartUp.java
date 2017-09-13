package com.Pre_Memo;
import android.app.*;
import android.os.*;
import android.content.*;
import com.mycompany.Multi_Dialog.*;
import android.preference.*;
import android.widget.*;
import java.io.*;

public class StartUp extends Activity{
	
	public static final String PREF_INIT_FLG="seted";
	private String StartScreen;
	private Activity ThisActivity=this;
	private Context ThisConte=this;

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		Ope_Pref pref=new Ope_Pref(ThisConte);
		StartScreen=pref.getString(getString(R.string.K_Startup),"");
		GoStartScreen(StartScreen);
	}

	@Override
	protected void onResume(){
		// TODO: Implement this method
		super.onResume();
		finish();
	}
	
	private void GoStartScreen(String TarIntent){
		if(TarIntent.equals(getString(R.string.activity_main))){
			Intent intent =new Intent(ThisActivity,MainActivity.class);
			startActivityForResult(intent,0);
		}else if(TarIntent.equals(getString(R.string.activity_list_v))){
			Intent intent =new Intent(ThisActivity,list_view.class);
			startActivityForResult(intent,0);
		}else if(TarIntent.equals(getString(R.string.activity_list_memo))){
			Intent intent =new Intent(ThisActivity,input_memo.class);
			startActivityForResult(intent,0);
		}else{
			InitSetings();
			Intent intent =new Intent(ThisActivity,MainActivity.class);
			startActivityForResult(intent,0);
		}
	}
	
	protected void InitSetings(){
		
		Init_Prf_Setting Initsets = new Init_Prf_Setting(this);
		Initsets.initPrefSet();
	}
}
