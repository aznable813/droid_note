package com.Pre_Memo;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import java.io.*;
import java.util.Calendar;
import android.database.sqlite.*;
import android.database.*;
import android.support.v7.widget.Toolbar;
import android.content.res.*;
import android.graphics.drawable.*;
import java.util.*;
import com.mycompany.Multi_Dialog.*;

import android.graphics.*;


public class input_memo extends Activity implements DialogLstnr{
	
	private final Activity ThisActvty=this;
	private final Context thisConte=this;
	//Clear Flag is "-1"
	public static final long ClrFlg=-1;
	
	//Dialog ID
	private static final int Dlg_Before_Clear=1;
	
	private EditText edit;
	
	//Menu_items
	private MenuItem saveItem;
	private MenuItem toListsItem;
	private MenuItem ClrItem;
	private MenuItem toHomeItem;
	private MenuItem SettingsItem;
	private MenuItem delItem;
	private MenuItem copyItem;
	
	private Toolbar toolbar;
	private Spinner testSpinner;
	private DBAdapter db;
	
	private String EditStr;
	private int SelectedIconID;
	private int EditCsrSta;
	private int EditCsrEnd;
	
	//Dialog
	private final DialogLstnr lstener = this;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_view);
		
		db=new DBAdapter(this);
		
		FindIDs();
		toolbar =(Toolbar)findViewById(R.id.inp_toolbar);
		testSpinner =(Spinner)toolbar.findViewById(R.id.inp_spinner);
		toolbar.inflateMenu(R.menu.input_lay_menu);
		getMenuItem();
		setItemListeners();
		
		TypedArray imgList = getResources().obtainTypedArray(R.array.categ_list);
		ArrayList<Drawable> imgArray = new ArrayList<Drawable>();
		for(int i=imageAdapter.StartImgId ; i<imgList.length() ; i++){
			imgArray.add(i-imageAdapter.StartImgId,imgList.getDrawable(i));	
		}
		imageAdapter imgAdptr =new imageAdapter(getApplication(),R.layout.spinner_only_image_row);
		imgAdptr.addImgs(imgArray);
		testSpinner.setAdapter(imgAdptr);
		
		// Initial Process
		Intent intent = getIntent();
		long TarId = intent.getLongExtra("TargetID",0);
		db.open(2);
			InitViews(TarId);
		db.close();
	}

	// Load View ID Method
	// ## Never forget to load this method following the method of "SetContentView"
	protected void FindIDs(){
		edit=(EditText)findViewById(R.id.txt_New);
		testSpinner=(Spinner)findViewById(R.id.inp_spinner);
	}
	
	//############### getMenuItem ###########################################################################
	private void getMenuItem(){
		saveItem=toolbar.getMenu().findItem(R.id.inp_mn_save);
		toListsItem=toolbar.getMenu().findItem(R.id.inp_mn_tolists);
		ClrItem=toolbar.getMenu().findItem(R.id.inp_mn_clear);
		
		toHomeItem=toolbar.getMenu().findItem(R.id.inp_mn_tohome);
		SettingsItem=toolbar.getMenu().findItem(R.id.inp_mn_settings);
		delItem=toolbar.getMenu().findItem(R.id.inp_mn_del);
		copyItem=toolbar.getMenu().findItem(R.id.inp_mn_copy);
	}
	//############### getMenuItem ###########################################################################
	
	//############### ItemListeners #########################################################################
	private void setItemListeners(){
		//@@ SaveItem Click
		saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem saveItem){
				//do Action
				saveInpMemo();
				return true;
			}
		});
		//@@ toListsItem Click
		toListsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem LstItem){
				//do Action
				Intent intent =new Intent(ThisActvty,list_view.class);
				startActivity(intent);
				return true;
			}
		});				
		//@@ ClrItem Click
		ClrItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem clearItem){
				//do Action
				getInputData();
				db.open(2);
				db.saveCurrent(EditStr,SelectedIconID);
				db.close();
				Chk_and_Clear();
				return true;
			}
		});	
		//@@ toHomeItem Click
		toHomeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem HomeItem){
				//do Action
				Intent intent =new Intent(ThisActvty,MainActivity.class);
				startActivity(intent);
				return true;
			}
		});	
		//@@ SettingsItem Click
		SettingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem SetItem){
				//do Action
				Intent intent =new Intent(ThisActvty,SettingsPrf.class);
				startActivity(intent);
				return true;
			}
		});	
		//@@ delItem Click
		delItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem deleteItem){
				//do Action
				db.open(2);
				if(db.getC_CRID()==0){
					InitViews(ClrFlg);
					Toast.makeText(thisConte,getString(R.string.btn_del_Clicked), Toast.LENGTH_SHORT).show();
				}else{
					if(db.MemoDel(db.getC_CRID())){
						InitViews(ClrFlg);
						Toast.makeText(thisConte,getString(R.string.btn_del_Clicked), Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(thisConte,getString(R.string.btn_Fail_del_Clicked), Toast.LENGTH_SHORT).show();
					}
				}
				db.close();
				return true;
			}
		});	
		//@@ copyItem Click
		copyItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem CpyItem){
				//do Action
				getInputData();
				if(EditStr.length()!=0){
					db.open(2);
					SaveInp_As_NewRec(EditStr,SelectedIconID);
					db.close();
					Toast.makeText(getApplication(),getString(R.string.Copied_Memo),Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});	
	}
	//############### ItemListeners #########################################################################

	
	//Use this method after loading method of  "FindIDs"
	//Need to load "DBAdapter.Open(2)" before this
	private void InitViews(long TarId){
		int setCsrSta = 0;
		int setCsrEnd = 0;
		
		if(TarId == ClrFlg){
			db.clearCurrent();
		}else if(TarId==0){
			setCsrSta=EditCsrSta;
			setCsrEnd=EditCsrEnd;
			EditCsrSta=0;
			EditCsrEnd=0;
			
		}else{
			Cursor c = db.getMemoById(TarId);
			c.moveToFirst();
			db.updateCurrent(c.getString(c.getColumnIndex(db.MEMO_DATA)),
								c.getInt(c.getColumnIndex(db.ICON_ID)),
							 	c.getLong(c.getColumnIndex(db.AUTO_ID)));
		}
		Cursor Cc = db.getCurrentMemo();
		Cc.moveToFirst();
		edit.setText(Cc.getString(Cc.getColumnIndex(db.MEMO_DATA)));
		if(Cc.getInt(Cc.getColumnIndex(db.ICON_ID))>=imageAdapter.StartImgId){
			testSpinner.setSelection(Cc.getInt(Cc.getColumnIndex(db.ICON_ID))-imageAdapter.StartImgId);
		}else{
			testSpinner.setSelection(Cc.getInt(Cc.getColumnIndex(db.ICON_ID)));
		}

		edit.setSelection(setCsrSta,setCsrEnd);
	}
	
	/// get input data //////////////////////
	private void getInputData(){
		EditStr = edit.getText().toString();
		SelectedIconID = testSpinner.getSelectedItemPosition()+imageAdapter.StartImgId;
	}
	
	//Save As New Rec
	private void SaveInp_As_NewRec(String memo,int iconId){
		long msglong=db.SaveMemo(memo,iconId,0);
		db.updateCRID(msglong);
	}
	
	
	/// Save DATA //////////////////////
	private void saveInpMemo(){
		getInputData();
		if(EditStr.length()!=0){
			db.open(2);
			db.saveCurrent(EditStr,SelectedIconID);
			int Recflg = db.Current_Chk();

			if(Recflg!=1){
				Cursor c = db.getCurrentMemo();
				c.moveToFirst();
				if(Recflg==2){
					SaveInp_As_NewRec(EditStr,SelectedIconID);
					Toast.makeText(getApplication(),getString(R.string.Saved_newMemo),Toast.LENGTH_LONG).show();
				}else if(Recflg==3){
					db.SaveMemo(EditStr,SelectedIconID,c.getLong(c.getColumnIndex(db.CR_ID)));
					Toast.makeText(getApplication(),getString(R.string.Updated_Memo),Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplication(),getString(R.string.excep_Err),Toast.LENGTH_LONG).show();
				}
			}
			//InitViews(0);
			db.close();
		}
	}

	//############### Check Clear Method #############################################################
	private void Chk_and_Clear(){
		
		ChkPendingMemo Pm = new ChkPendingMemo(thisConte,db);
		if(Pm.getPendingFlg()){
			db.open(2);
			InitViews(ClrFlg);
			db.close();
		}else{
			if(Pm.getErrFlg()){
				Toast.makeText(getApplication(),getString(R.string.excep_Err),Toast.LENGTH_SHORT).show();
			}else{
				// Call Dialog 
				CustomDialogFragment mydialog =CustomDialogFragment.newInstance
				(Pm.getDiagTitle(),Pm.getDiagMsg(),CustomDialogFragment.Type_Y_N_C,Dlg_Before_Clear);
				mydialog.setDialogLstnr(lstener);
				mydialog.setCancelable(false);
				mydialog.show(getFragmentManager(),"Clear_ync_Dialog");
			}
		}
	}
	//############### Check Clear Method #############################################################
	
	//############### DialogListener #######################################################################
	@Override
	public void ActClick(int ClickBtn, int RtnID){
		// TODO: Implement this method
		if(RtnID==Dlg_Before_Clear){
			switch(ClickBtn){
				case 1:
					db.open(2);
					db.Current_SaveAs_NewRec();
					InitViews(ClrFlg);
					db.close();
					break;
				case 2:
					db.open(2);
					InitViews(ClrFlg);
					db.close();
					break;
				case 3:
				case 4:
					Toast.makeText(getApplication(),getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(getApplication(),getString(R.string.excep_Err),Toast.LENGTH_SHORT).show();
					break;
			}
		}else{
			//Stab
		}
	}
	//############### DialogListener #######################################################################
	
	@Override
	protected void onStart(){
		// TODO: Implement this method
		super.onStart();
		db.open(2);
		InitViews(0);
		db.close();
	}
	
	@Override
	protected void onPause(){
		// TODO: Implement this method
		super.onPause();
		EditCsrSta = edit.getSelectionStart();
		EditCsrEnd = edit.getSelectionEnd();
		
		getInputData();
		db.open(2);
		db.saveCurrent(EditStr,SelectedIconID);
		db.close();
	}
	
//%%%%%%%  Double Back Finish when This Activity is root  %%%%%%%%%%%%%%%%%%%%%%%%%%	
	private boolean FinishFlag;
	@Override
	public void onBackPressed(){
		dbl_back_finish dbf = new dbl_back_finish(this);
		FinishFlag=dbf.BackFinish(FinishFlag);
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run(){
					FinishFlag = false;
				}
			}, 2000);
	}
//%%%%%%%  Double Back Finish when This Activity is root  %%%%%%%%%%%%%%%%%%%%%%%%%%	
}
