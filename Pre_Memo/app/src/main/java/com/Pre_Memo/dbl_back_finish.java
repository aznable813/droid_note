package com.Pre_Memo;
import android.content.*;
import android.widget.*;
import android.app.*;

public class dbl_back_finish{
	
	private Activity thisActi;
	
	//const
	public dbl_back_finish(Activity acti){
		thisActi=acti;
	}
	public boolean BackFinish(boolean flg){
		if(thisActi.getCallingActivity()!=null){
			if (flg){
				thisActi.finish();
			}else{
				Toast.makeText(thisActi,thisActi.getString(R.string.Dbl_Back_Msg), Toast.LENGTH_SHORT).show();
			}
		}else{
			thisActi.finish();
		}
		return true;
	}
}
