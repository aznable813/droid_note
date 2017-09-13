package com.Pre_Memo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;
import java.util.Calendar;
import java.text.*;
import java.util.*;
import com.mycompany.Multi_Dialog.*;
import android.os.*;


public class DBAdapter{
	public static final String DB_NAME = "memoDB.db";
	
	
	static final int DB_Ver = 1;
	
	public static final int DATA_INIT_TYPE=1;
	public static final int DATA_NORM_TYPE=2;
	public static final int DATA_DELETE_TYPE=-1;
	
	public static final String DB_TABLE_NAME="memoData";
	public static final String AUTO_ID="_id";
	public static final String MEMO_DATA="dataMemo";
	public static final String UP_DATE="updateDate";
	public static final String DATA_TYPE="dataType";
	public static final String ICON_ID="iconID";
	public static final String CR_ID="currentID";
	public static final String ASC="asc";
	public static final String DSC="desc";
	
	
	private final Context DBconte;
	private DatabaseHelper dbhelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context context){
		DBconte=context;
		dbhelper =new DatabaseHelper(DBconte);
	}
	
	//SQlite OpenHelper
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		DatabaseHelper(Context conte){
			super(conte,DB_NAME,null,DB_Ver);
		}

		@Override
		public void onCreate(SQLiteDatabase db){
			// Table Create
			db.execSQL("create table if not exists "
					   + DB_TABLE_NAME
					   + "("
					   + AUTO_ID
					   +" integer primary key autoincrement, "
					   + MEMO_DATA
					   + " text, "
					   + UP_DATE
					   + " text, "
					   + DATA_TYPE
					   + " integer,"
					   + ICON_ID
					   + " integer,"
					   + CR_ID
					   + " integer)");
			//Initial Data insert
			db.execSQL("insert into "
					   + DB_TABLE_NAME
					   + " ("
					   + MEMO_DATA + ", "
					   + UP_DATE + ", "
					   + DATA_TYPE + ", "
					   + ICON_ID + ", "
					   + CR_ID + ") "
					   + "values('', '', " + DATA_INIT_TYPE + ", " + imageAdapter.DEF_ICON + ", 0)");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldver, int newver){
			// TODO: Implement this method
			db.execSQL("drop table if exists " + DB_TABLE_NAME);
			onCreate(db);
		}
	}
	
	private String getOrderFromPref(){
		String RtnStr;

		Ope_Pref pref=new Ope_Pref(DBconte);
		RtnStr=
			RtnOdrCD(pref.getString(DBconte.getString(R.string.K_Odr1),""))+
			" "+
			RtnOdrCD(pref.getString(DBconte.getString(R.string.K_Odr1_by),""))+
			","+
			RtnOdrCD(pref.getString(DBconte.getString(R.string.K_Odr2),""))+
			" "+
			RtnOdrCD(pref.getString(DBconte.getString(R.string.K_Odr2_by),""));
		return RtnStr;
	}
	private String RtnOdrCD(String TarStr){
		String RtnStr;
		if(TarStr.equals(DBconte.getString(R.string.Ent1_Odr))){
			RtnStr=UP_DATE;
		}else if(TarStr.equals(DBconte.getString(R.string.Ent2_Odr))){
			RtnStr=MEMO_DATA;
		}else if(TarStr.equals(DBconte.getString(R.string.Ent3_Odr))){
			RtnStr=ICON_ID;
		}else if(TarStr.equals(DBconte.getString(R.string.Ent1_Odr_by))){
			RtnStr=ASC;
		}else if(TarStr.equals(DBconte.getString(R.string.Ent2_Odr_by))){
			RtnStr=DSC;
		}else{
			RtnStr="";
		}
		return RtnStr;
	}
	
	//#################
	//Adapter Method(Open,Close,and so on ...)
	//#################
	//DB Open
	public DBAdapter open(int OpenMode){
		//OpenMode: 1=read,2=write
		if(OpenMode == 1){
			db=dbhelper.getReadableDatabase();
		}else if(OpenMode==2){
			db=dbhelper.getWritableDatabase();
		}else{
			db=null;
		}
		return this;
	}
	//DB Close
	public void close(){
		dbhelper.close();
	}
	
//#############################################################
//#######                                              ########
//#######          Method that DB-Operations           ########
//#######                                              ########
//#############################################################
	
	//################ Delete Records logically or physically & Recover Records ############################
	//Delete All Data(@@@@ need to open [WritableMode])
	public boolean allMemoDel(){
		return db.delete(DB_TABLE_NAME,null,null)>0;
	}
	//@@@@@@@@@@@@@@@@@ Delete TargetRecord With ID(@@@@ need to open [WritableMode])
	public boolean MemoDel(long TarID){
		long CrId = getC_CRID();
		if(TarID==CrId){
			clearCurrent();
		}
		ContentValues Values = new ContentValues();
		Values.put(DATA_TYPE,DATA_DELETE_TYPE);
		return db.update(DB_TABLE_NAME, Values,AUTO_ID+"="+TarID, null)>0;
	}
	//@@@@@@@@@@@@@@@@@ physical Delete With ID(@@@@ need to open [WritableMode])
	public boolean MemoErase(long TarID){
		long CrId = getC_CRID();
		if(TarID==CrId){
			clearCurrent();
		}
		return db.delete(DB_TABLE_NAME,AUTO_ID+"="+TarID,null)>0;
	}
	//@@@@@@@@@@@@@@@@@ physical Delete Records in trush-box(@@@@ need to open [WritableMode])
	public long allMemoErase(){
		return db.delete(DB_TABLE_NAME,DATA_TYPE+"="+DATA_DELETE_TYPE,null);
	}
	//@@@@@@@@@@@@@@@@@ Recovery TargetRecord With ID(@@@@ need to open [WritableMode])
	public boolean MemoRecover(long TarID){
		long CrId = getC_CRID();
		if(TarID==CrId){
			clearCurrent();
		}
		ContentValues Values = new ContentValues();
		Values.put(DATA_TYPE,DATA_NORM_TYPE);
		return db.update(DB_TABLE_NAME, Values,AUTO_ID+"="+TarID, null)>0;
	}
	//@@@@@@@@@@@@@@@@@ Recovery all Records in trush-box(@@@@ need to open [WritableMode])
	public long allMemoRcover(){
		ContentValues Values = new ContentValues();
		Values.put(DATA_TYPE, DATA_NORM_TYPE);
		return db.update(DB_TABLE_NAME, Values, DATA_TYPE+"=" + DATA_DELETE_TYPE, null);
	}
	//################ Delete Records logically or physically & Recover Records ############################
	
	
	//Get CurrentRec CRID
	public long getC_CRID(){
		final Cursor c = getCurrentMemo();
		c.moveToFirst();
		return c.getLong(c.getColumnIndex(CR_ID));
	}

	//################ get One Record and Return Cursor ##########################################
	//Get a TargetRecord with ID
	public Cursor getMemoById(long TarID){
		return db.query(DB_TABLE_NAME,null,AUTO_ID+"=?",
						new String[]{String.valueOf(TarID)},null,null,null,null);
	}
	//Get a Current Memo in Table(Type Init only)
	public Cursor getCurrentMemo(){
		return db.query(DB_TABLE_NAME,null,DATA_TYPE+"=?",
						new String[]{String.valueOf(DATA_INIT_TYPE)},null,null,null,null);
	}
	//################  get One Record and Return Cursor ##########################################
	//#####################  get some Records and Return Cursor ##########################################################################
	//Get TargetRecords with Filtered categ
	public Cursor getMemoByCateg(int TarCateg){
		return db.query(DB_TABLE_NAME,null,DATA_TYPE+"=? AND "+ICON_ID+"=?",
						new String[]{String.valueOf(DATA_NORM_TYPE),String.valueOf(TarCateg)},null,null,getOrderFromPref(),null);
	}
	//Get TargetRecords with FindString
	public Cursor getMemoByStr(String TarStr){
		String FindStr = "%"+TarStr+"%";
		return db.query(DB_TABLE_NAME,null,DATA_TYPE+"=? AND "+MEMO_DATA+" like ?",
						new String[]{String.valueOf(DATA_NORM_TYPE),FindStr},null,null,getOrderFromPref(),null);
	}
	//Get TargetRecords with categId and FindString
	public Cursor getMemoByCategAndStr(int TarCateg,String TarStr){
		String FindStr = "%"+TarStr+"%";
		return db.query(DB_TABLE_NAME,null,DATA_TYPE+"=? AND "+ICON_ID+"=? AND "+MEMO_DATA+" like ?",
						new String[]{String.valueOf(DATA_NORM_TYPE),String.valueOf(TarCateg),FindStr},null,null,getOrderFromPref(),null);
	}
	//Get All Memos in Table(Type data only)
	public Cursor getAllMemo(){
		
		return db.query(DB_TABLE_NAME,null,DATA_TYPE+"=?",
						new String[]{String.valueOf(DATA_NORM_TYPE)},null,null,getOrderFromPref(),null);
	}
	//Get Deleted Memos in Table(Type Deleted data only)
	public Cursor getDeletedMemo(){
		return db.query(DB_TABLE_NAME,null,DATA_TYPE+"=?",
						new String[]{String.valueOf(DATA_DELETE_TYPE)},null,null,getOrderFromPref(),null);
	}
	//#####################  get Records and Return Cursor ##########################################################################
	
	//update Current record in Table(Type Init only)
	public long updateCurrent(String putMemo,int iconid,long crntID){
		ContentValues Values = new ContentValues();
		Values.put(MEMO_DATA, putMemo);
		Values.put(ICON_ID, iconid);
		Values.put(CR_ID, crntID);
		return db.update(DB_TABLE_NAME, Values, DATA_TYPE+"=" + DATA_INIT_TYPE, null);
	}
	//update CRID in Current record in Table(Type Init only)
	public long updateCRID(long crntID){
		ContentValues Values = new ContentValues();
		Values.put(CR_ID, crntID);
		return db.update(DB_TABLE_NAME, Values, DATA_TYPE+"=" + DATA_INIT_TYPE, null);
	}
	//save Current Rec without CRID (save temporaly)
	public long saveCurrent(String putMemo,int iconid){
		ContentValues Values = new ContentValues();
		Values.put(MEMO_DATA, putMemo);
		Values.put(ICON_ID, iconid);
		return db.update(DB_TABLE_NAME, Values, DATA_TYPE+"=" + DATA_INIT_TYPE, null);
	}
	//clear Current record in Table(Memo="",Icon=Default,CRID=0)
	public void clearCurrent(){
		updateCurrent("",imageAdapter.DEF_ICON,0);
	}
	//save currentrecord as new record(@@@@ need to open [WritableMode])
	public long Current_SaveAs_NewRec(){
		final Cursor c = getCurrentMemo();
		c.moveToFirst();
		return SaveMemo(c.getString(c.getColumnIndex(MEMO_DATA)),
						c.getInt(c.getColumnIndex(ICON_ID)),
						c.getLong(c.getColumnIndex(CR_ID)));
	}
	
	
	//Insert Or Update (@@@@ need to open [WritableMode])
	public long SaveMemo(String putMemo,int iconid,long crntId){
		final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final Date dateFmt = new Date(System.currentTimeMillis());
		ContentValues Values = new ContentValues();
		if(crntId==0){
			Values.put(MEMO_DATA,putMemo);
			Values.put(UP_DATE,df.format(dateFmt));
			Values.put(DATA_TYPE,DATA_NORM_TYPE);
			Values.put(ICON_ID,iconid);
			Values.put(CR_ID,0);
			return db.insert(DB_TABLE_NAME,null,Values);
			//return 1;
		}else{
            Values.put(MEMO_DATA, putMemo);
			Values.put(UP_DATE,df.format(dateFmt));
			Values.put(ICON_ID,iconid);
            Values.put(CR_ID, 0);
            return db.update(DB_TABLE_NAME, Values,AUTO_ID+"=" + crntId, null);
		}
		
	}
	
	//Current Memo State Check Method
	public int Current_Chk(){
		//## NewRec and Notext 			= 1
		//## Also EditRec and Nochange 	= 1
		//## NewRec and AnyText 		= 2
		//## EditRec and AnyChange 		= 3
	
		final int RtnCD;
		final Cursor c = getCurrentMemo();
		c.moveToFirst();
		final long CrID=c.getLong(c.getColumnIndex(CR_ID));
		
		if(CrID==0){
			String NewStr=c.getString(c.getColumnIndex(MEMO_DATA));
			if(NewStr==null){
				NewStr="";
			}
			if(NewStr.equals("")){
				RtnCD=1;
			}else{
				RtnCD=2;
			}
		}else{
			Cursor Tc = getMemoById(CrID);
			Tc.moveToFirst();
			final String cStr=c.getString(c.getColumnIndex(MEMO_DATA));
			final int cIcon=c.getInt(c.getColumnIndex(ICON_ID));
			final String TcStr=Tc.getString(Tc.getColumnIndex(MEMO_DATA));
			final int TcIcon=Tc.getInt(Tc.getColumnIndex(ICON_ID));
			
			if(TcIcon==cIcon && TcStr.equals(cStr)){
				RtnCD=1;
			}else{
				RtnCD=3;
			}
		}
		return RtnCD;
	}
}
