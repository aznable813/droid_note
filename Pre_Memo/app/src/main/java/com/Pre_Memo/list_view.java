package com.Pre_Memo;
import android.app.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.database.*;
import android.view.*;
import android.view.View.*;
import android.content.*;
import android.graphics.*;
import android.content.res.*;
import android.graphics.drawable.*;

import android.support.v7.appcompat.*;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.AdapterView.*;
import android.widget.ActionMenuView.*;
import android.widget.SearchView.*;
import android.view.MenuItem;
import com.mycompany.Multi_Dialog.*;

public class list_view extends Activity implements 
OnItemSelectedListener,SearchView.OnQueryTextListener,DialogLstnr,vp_btn_listener{
	
	private DBAdapter db;
	private ArrayList<memoData> listStr;
	private final Context mConte = this;
	private CustomAdapter lapt;
	private ListView lv;
	
	private int vpMargin;
	
	private final int vpLstenerID=1;	
	private final int Dlg_Before_Add=1;
	
	private Toolbar toolbar;
	private Spinner testSpinner;

	//menuItems
	private MenuItem addMemoItem;
	private MenuItem editMemoItem;
	private MenuItem toHomeItem;
	private MenuItem searchItem;
	private MenuItem settingItem;
	private SearchView searchV;

	//Yes No Cancel flg
	private final DialogLstnr lstener = this;
	private long Go_ID=0;
	
	
	private ArrayList<Integer> ImgIdList;
	private String SearchStr="";
	private int SelectedID=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		Point size =new Point();
		disp.getSize(size);

		int btnWidPix = (int)(getResources().getDimension(R.dimen.vp_btn_del_width)+
			getResources().getDimension(R.dimen.vp_mrgin_Pad));
		vpMargin=size.x-btnWidPix;
		int selectorPix = (int)(getResources().getDimension(R.dimen.IC_half)*2);
		
		toolbar =(Toolbar)findViewById(R.id.my_toolbar);
		testSpinner =(Spinner)toolbar.findViewById(R.id.spinner);
		toolbar.inflateMenu(R.menu.search_menu);
		
		getMenuItem();
		
		searchV.setMaxWidth(size.x - selectorPix);
		setItemListeners();
		searchV.setOnQueryTextListener(this);
		
		GetAppArarys getArry = new GetAppArarys(mConte);
		ImgIdList=getArry.get_ImgID();
		
		imageAdapter imgAdptr =new imageAdapter(getApplication(),R.layout.spinner_only_image_row);
		imgAdptr.addImgs(getArry.get_ImgDrawable());
		testSpinner.setAdapter(imgAdptr);
		testSpinner.setOnItemSelectedListener(this);
		
		db = new DBAdapter(this);
		//put data into lists called "lv". 
		lv=(ListView)findViewById(R.id.list_v);

		//set  deta of lists into adapter
		toFilter(SelectedID,SearchStr);
		
	}
	
//############### getItems #########################################################################
	private void getMenuItem(){
		searchItem=toolbar.getMenu().findItem(R.id.search);
		addMemoItem=toolbar.getMenu().findItem(R.id.addmemo);
		editMemoItem=toolbar.getMenu().findItem(R.id.editmemo);
		toHomeItem=toolbar.getMenu().findItem(R.id.tohome);
		settingItem=toolbar.getMenu().findItem(R.id.settings);
		searchV = (SearchView) searchItem.getActionView();
	}
//############### getItems #########################################################################
	
//############### setVisibleItems #########################################################################
	private void setVisibleItems(Boolean Flg){
		addMemoItem.setVisible(Flg);
		editMemoItem.setVisible(Flg);
		toHomeItem.setVisible(Flg);
		settingItem.setVisible(Flg);
	}
//############### setVisibleItems #########################################################################
	
//############### ItemListeners #########################################################################
	private void setItemListeners(){
		
		//@@@@@ editMemoBtn Click Event
		editMemoItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem admItem){
				//Toast.makeText(getApplication(),"ADD MEMO Action",Toast.LENGTH_SHORT).show();
				Go_ID=0;
				goInputMemo();
				return true;
			}
		});
		//@@@@@ AddMemoBtn Click Event
		addMemoItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem admItem){
				Go_ID=input_memo.ClrFlg;
				ChkAndGo_input();
				return true;
			}
		});
		//@@@@@ tohomeBtn Click Event
		toHomeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem toHItem){
				Intent intent =new Intent(mConte,MainActivity.class);
				startActivity(intent);
				return true;
			}
		});
		//@@@@@ toSettingsBtn Click Event
		settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
				@Override
				public boolean onMenuItemClick(MenuItem setItem){
					Intent intent =new Intent(mConte,SettingsPrf.class);
					startActivity(intent);
					return true;
				}
			});
		//@@@@@ SearchView Open or Close Event
		searchV.setOnSearchClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				setVisibleItems(false);
				
				//Set Focus SearchView 
				searchV.requestFocus();
			}
		});
		searchV.setOnCloseListener(new OnCloseListener(){
			@Override
			public boolean onClose(){
				setVisibleItems(true);
				return false;
			}
		});
	}
//############### ItemListeners #########################################################################


	@Override
	protected void onResume(){
		// TODO: Implement this method
		super.onResume();
		testSpinner.setFocusable(false);
		toFilter(SelectedID,SearchStr);
		lapt.notifyDataSetChanged();
	}
	@Override
	protected void onPause(){
		// TODO: Implement this method
		super.onPause();
		lapt.removeVpBtnListener();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent,View view,int position,long id) {
		SelectedID=position;
		if(SearchStr==null){
			SearchStr="";
		}
		toFilter(SelectedID,SearchStr);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//stub
	}

	@Override
	public boolean onQueryTextChange(String p1){
		// TODO: Implement this method
		SearchStr=p1;
		if(SearchStr.equals("")){
			toFilter(SelectedID,SearchStr);
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String p1){
		// TODO: Implement this method
		if(p1==null){
			SearchStr="";
		}else{
			SearchStr=p1;
		}
		toFilter(SelectedID,SearchStr);
		return true;
	}
	
//############### Check And Go Input Method #############################################################
	private void ChkAndGo_input(){
		ChkPendingMemo Pm = new ChkPendingMemo(mConte,db);
	
		if(Pm.getPendingFlg()){
			goInputMemo();
		}else{
			if(Pm.getErrFlg()){
				Toast.makeText(getApplication(),getString(R.string.excep_Err),Toast.LENGTH_SHORT).show();
			}else{
				// Call Dialog 
				CustomDialogFragment mydialog =CustomDialogFragment.newInstance
				(Pm.getDiagTitle(),Pm.getDiagMsg(),CustomDialogFragment.Type_Y_N_C,Dlg_Before_Add);
				mydialog.setDialogLstnr(lstener);
				mydialog.setCancelable(false);
				mydialog.show(getFragmentManager(),"addmemo_ync_Dialog");
			}
		}
	}
//############### Check And Go Input Method #############################################################
	
//############### Go Input Method #######################################################################
	private void goInputMemo(){
		Intent intent = new Intent(mConte,input_memo.class);
		intent.putExtra("TargetID",Go_ID);
		startActivity(intent);
	}
//############### Go Input Method #######################################################################
	
	
	//Filter Method By ID and String
	private void toFilter(int filterId,String filterStr){
		db.open(1);
		Cursor c=null;
		if(filterId==0){
			if(filterStr.equals("")){
				c = db.getAllMemo();
			}else{
				c = db.getMemoByStr(filterStr);
			}
		}else{
			if(filterStr.equals("")){
				c = db.getMemoByCateg(filterId);
			}else{
				c = db.getMemoByCategAndStr(filterId,filterStr);
			}
		}
		CursorToMemo CTM = new CursorToMemo(c);
		CTM.setImgList(ImgIdList);
		listStr = new ArrayList<memoData>();
		listStr = CTM.getMemolist();
		lapt=new CustomAdapter(mConte,listStr,vpMargin,R.layout.page1,R.layout.page2,vpLstenerID);
		lv.setAdapter(lapt);
		lapt.setVpBtnlistener(this);
		//DB Close
		db.close();
	}
	
//############### DialogListener #######################################################################
	@Override
	public void ActClick(int ClickBtn, int RtnID){
		// TODO: Implement this method

		if(RtnID==Dlg_Before_Add){
			switch(ClickBtn){
				case 1:
					db.open(2);
					db.Current_SaveAs_NewRec();
					db.close();
					goInputMemo();
					break;
				case 2:
					goInputMemo();
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
			//stab
		}
	}

	
//############### DialogListener #######################################################################

	
//############### vpListener #######################################################################
	@Override
	public void VP_ActClick(int ClickBtn, int RtnID, long memoID, String memoStr){
		// TODO: Implement this method
		if(RtnID==vpLstenerID){
			switch(ClickBtn){
				case CustomAdapter.Rtn_ListBody_clk:
					Go_ID=memoID;
					ChkAndGo_input();
					break;
				case CustomAdapter.Rtn_btn1_clk:
					db.open(2);
					db.MemoDel(memoID);
					db.close();
					toFilter(SelectedID,SearchStr);
					lapt.notifyDataSetChanged();
					Toast.makeText(this,"[ "+memoStr+" ]\n"+getString(R.string.btn_del_Clicked), Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
//############### vpListener #######################################################################	

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
	
