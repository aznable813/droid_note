package com.Pre_Memo;
import android.content.*;
import com.mycompany.Multi_Dialog.*;
import java.io.*;
import android.os.*;

public class Init_Prf_Setting{
	
	//Set Para
	private Context conte;
	public static final String PREF_INIT_FLG="seted";
	
	//Const
	public Init_Prf_Setting(Context mconte){
		conte = mconte;
	}
	
	//#### << Methods >> ####
	public void initPrefSet(){
		
		// Downloadフォルダーのパス
		File pathExternalPublicDir = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DOWNLOADS);
		String dlpath = pathExternalPublicDir.getPath();

		Ope_Pref pref=new Ope_Pref(conte);
		pref.saveString(conte.getString(R.string.K_Startup),conte.getString(R.string.activity_main));
		pref.saveString(conte.getString(R.string.K_Odr1),conte.getString(R.string.Ent1_Odr));
		pref.saveString(conte.getString(R.string.K_Odr1_by),conte.getString(R.string.Ent2_Odr_by));
		pref.saveString(conte.getString(R.string.K_Odr2),conte.getString(R.string.Ent3_Odr));
		pref.saveString(conte.getString(R.string.K_Odr2_by),conte.getString(R.string.Ent1_Odr_by));
		pref.saveString(conte.getString(R.string.K_CrntPath),dlpath);
		pref.saveString(conte.getString(R.string.K_Init),PREF_INIT_FLG);
		
	}
	
}
