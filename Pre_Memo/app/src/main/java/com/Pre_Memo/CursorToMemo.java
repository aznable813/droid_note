package com.Pre_Memo;
import android.database.*;
import java.util.*;

public class CursorToMemo{
	
	private Cursor Crsr;
	private String titleStr;
	private memoData memodat;
	private ArrayList<Integer> ImageIdList;
	private ArrayList<memoData> Memolists;
	
	//const
	public CursorToMemo(Cursor crsr){
		Crsr=crsr;
	}
	//setter
	public void setImgList(ArrayList<Integer> imageidList){
		ImageIdList=imageidList;
	}
	
	//Private method
	private String makeTitle(String AllStr){
		String StrLF = "\n";
		int LF_num;
		LF_num = AllStr.indexOf(StrLF);
		if(LF_num!=-1){
			while(LF_num==0){
				AllStr=AllStr.substring(StrLF.length());
				LF_num = AllStr.indexOf(StrLF);
			}
			AllStr=AllStr.substring(0,LF_num);
		}
		return AllStr;
	} 
	
	//method
	public ArrayList<memoData> getMemolist(){
		Memolists=new ArrayList<memoData>();
		if(Crsr.getCount()>0){
			//一番先頭のレコードに移動
			Crsr.moveToFirst();
			//検索結果の件数だけ繰り返し
			for(int i=0;i<Crsr.getCount();i++){
				titleStr=Crsr.getString(1);
				//titleStr=titleStr.replace("\n","");
				titleStr=makeTitle(titleStr);
				//ListView用のArrayListに追加
				memodat=new memoData(Crsr.getLong(Crsr.getColumnIndex(DBAdapter.AUTO_ID)),
										titleStr,
										Crsr.getString(Crsr.getColumnIndex(DBAdapter.UP_DATE)),
										Crsr.getInt(Crsr.getColumnIndex(DBAdapter.DATA_TYPE)));
				
				memodat.setIconId(ImageIdList.get(Crsr.getInt(Crsr.getColumnIndex(DBAdapter.ICON_ID))));
				Memolists.add(memodat);
				//次のレコードへ移動
				Crsr.moveToNext();
			}
		}
		return Memolists;
	}
	
	
}
