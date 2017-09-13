package com.Pre_Memo;
import android.content.*;

public class ChkPendingMemo{
	
	private boolean PendingFlg=false;
	private boolean ErrFlg=false;
	private int rtnCD = 0;
	private String TitleStr="";
	private String MsgStr="";
	
	//const
	public ChkPendingMemo(Context conte,DBAdapter db){
		db.open(1);
		rtnCD=db.Current_Chk();
		db.close();
		switch(rtnCD){
			case 1:
				PendingFlg=true;
				break;
			case 2:
				TitleStr=conte.getString(R.string.title_dialog_newMemoSave);
				MsgStr=conte.getString(R.string.msg_dialog_newMemoSave);
				break;
			case 3:
				TitleStr=conte.getString(R.string.title_dialog_ModifiedSave);
				MsgStr=conte.getString(R.string.msg_dialog_ModifiedSave);
				break;
			case 0:
				ErrFlg=true;
				break;
		}
	}
	
	//### getter ####################################
	
	//pending flg
	public boolean getPendingFlg(){
		return PendingFlg;
	}
	
	//Error Flg
	public boolean getErrFlg(){
		return ErrFlg;
	}
	
	// Get Dialog Title
	public String getDiagTitle(){
		return TitleStr;
	}
	
	// Get Dialog Msg
	public String getDiagMsg(){
		return MsgStr;
	}
	//### getter ####################################
}
