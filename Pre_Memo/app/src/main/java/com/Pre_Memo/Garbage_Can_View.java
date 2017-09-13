package com.Pre_Memo;
import android.app.*;
import android.os.*;
import android.content.*;
import android.view.*;
import android.database.*;
import java.util.*;
import android.widget.*;
import android.support.v7.widget.Toolbar;
import android.content.res.*;
import android.graphics.drawable.*;
import android.graphics.*;
import com.mycompany.Multi_Dialog.*;

public class Garbage_Can_View extends Activity implements vp_btn_listener,DialogLstnr {
	private final int Trush_Listener_ID = 1;
	private final int Dlg_ID_AllErase=1;
	private final int Dlg_ID_AllRcover=2;
	private final DialogLstnr dlgListener = this;
	
	private Context ThisConte;
	//private LayoutInflater ThisInfla;
	private DBAdapter db;
	private Toolbar toolbar;
	//Menu_items
	private MenuItem toHomeItem;
	private MenuItem toListsItem;
	private MenuItem all_erase_Item;
	private MenuItem all_recoverItem;
	
	private ListView lv;
	private ArrayList<memoData> listStr;
	private CustomAdapter lapt;
	private int vpmargin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garbage_listview);
		
		ThisConte=getApplication();
		db=new DBAdapter(ThisConte);
		
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		Point size =new Point();
		disp.getSize(size);
		int btnWidPix = (int)(getResources().getDimension(R.dimen.vp_btn_del_width)+
								getResources().getDimension(R.dimen.vp_btn_recov_width)+
								getResources().getDimension(R.dimen.vp_mrgin_Pad));
		vpmargin=size.x-btnWidPix;
		
		//set toolbar 
		toolbar =(Toolbar)findViewById(R.id.del_view_toolbar);
		toolbar.inflateMenu(R.menu.garbage_menu);
		getMenuItem();
		setItemListeners();
		
		
		//put data into lists called "lv". 
		lv=(ListView)findViewById(R.id.garbage_list_v);

		//set  deta of lists into adapter
		getDelMemos();
	}
	
//############### getItems #########################################################################
	private void getMenuItem(){
		toHomeItem=toolbar.getMenu().findItem(R.id.garbage_to_home);
		toListsItem=toolbar.getMenu().findItem(R.id.garbage_to_listview);
		all_erase_Item=toolbar.getMenu().findItem(R.id.item_all_erase);
		all_recoverItem=toolbar.getMenu().findItem(R.id.item_all_recover);
	}
//############### getItems #########################################################################

//############### ItemListeners #########################################################################
	private void setItemListeners(){
		//@@ SaveItem Click
		toHomeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
				@Override
				public boolean onMenuItemClick(MenuItem tohomeItem){
					//do Action
					Intent intent =new Intent(ThisConte,MainActivity.class);
					startActivity(intent);
					return true;
				}
			});
		//@@ toListsItem Click
		toListsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
				@Override
				public boolean onMenuItemClick(MenuItem LstItem){
					//do Action
					Intent intent =new Intent(ThisConte,list_view.class);
					startActivity(intent);
					return true;
				}
			});				
		//@@ AllErase Click
		all_erase_Item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem alleraseItem){
				//do Action
				db.open(1);
				Cursor c=null;
				c = db.getDeletedMemo();
				int num_of_delmemo = c.getCount();
				db.close();
					
				if(num_of_delmemo==0){
					Toast.makeText(ThisConte,getString(R.string.msg_already_empty), Toast.LENGTH_SHORT).show();
				}else{
					// Call Dialog 
					CustomDialogFragment mydialog =CustomDialogFragment.newInstance(
						ThisConte.getString(R.string.title_dlg_allErase),
						ThisConte.getString(R.string.msg_dlg_allErase)+String.valueOf(num_of_delmemo),
						CustomDialogFragment.Type_O_C,
						Dlg_ID_AllErase);
					mydialog.setDialogLstnr(dlgListener);
					mydialog.setCancelable(false);
					mydialog.show(getFragmentManager(),"all_Erase_Dlg");
					//Toast.makeText(ThisConte,"All Erase Clicked! "+String.valueOf(num_of_delmemo)+"Records!", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});	
		//@@ AllRecover Click
		all_recoverItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
				@Override
				public boolean onMenuItemClick(MenuItem HomeItem){
					//do Action
					db.open(1);
					Cursor c=null;
					c = db.getDeletedMemo();
					int num_of_delmemo = c.getCount();
					db.close();
					
					if(num_of_delmemo==0){
						Toast.makeText(ThisConte,getString(R.string.msg_already_empty), Toast.LENGTH_SHORT).show();
					}else{
					// Call Dialog 
						CustomDialogFragment mydialog =CustomDialogFragment.newInstance(
							ThisConte.getString(R.string.title_dlg_allRecover),
							ThisConte.getString(R.string.msg_dlg_allRecover)+String.valueOf(num_of_delmemo),
							CustomDialogFragment.Type_O_C,
							Dlg_ID_AllRcover);
						mydialog.setDialogLstnr(dlgListener);
						mydialog.setCancelable(false);
						mydialog.show(getFragmentManager(),"all_Recover_Dlg");
					}
					//Toast.makeText(ThisConte,"All Recover Clicked!", Toast.LENGTH_SHORT).show();
					return true;
				}
			});	
	}
	//############### ItemListeners #########################################################################

	//get memolist deleted
	private void getDelMemos(){
		db.open(1);
		Cursor c=null;
		c = db.getDeletedMemo();
		
		CursorToMemo CTM = new CursorToMemo(c);
		GetAppArarys getArry = new GetAppArarys(ThisConte);
		CTM.setImgList(getArry.get_ImgID());
		listStr = CTM.getMemolist();

		lapt = new CustomAdapter(ThisConte,listStr,vpmargin,R.layout.page1,R.layout.garbage_page,Trush_Listener_ID);
		lv.setAdapter(lapt);
		lapt.setVpBtnlistener(this);
		//DB Close
		db.close();
	}

	@Override
	protected void onResume(){
		// TODO: Implement this method
		super.onResume();
		getDelMemos();
	}

	@Override
	protected void onPause(){
		// TODO: Implement this method
		super.onPause();
		lapt.removeVpBtnListener();
	}
	
	//############### VPager Listener Action #########################################################################
	@Override
	public void VP_ActClick(int ClickBtn, int RtnID,long memoID,String memoStr){
		// TODO: Implement this method
		if(RtnID==Trush_Listener_ID){
			switch(ClickBtn){
				case CustomAdapter.Rtn_ListBody_clk:
					//long TarId = intent.getLongExtra("DelTargetID",0);
					Intent intent = new Intent(ThisConte,Garbage_Contents.class);
					intent.putExtra("DelTargetID",memoID);
					startActivity(intent);
					break;
				case CustomAdapter.Rtn_btn1_clk:
					db.open(2);
					db.MemoErase(memoID);
					db.close();
					//loadMemoList();
					getDelMemos();
					lapt.notifyDataSetChanged();
					Toast.makeText(this,"[ "+memoStr+" ]\n"+getString(R.string.btn_erase_Clicked), Toast.LENGTH_SHORT).show();
					break;
				case CustomAdapter.Rtn_btn2_clk:
					db.open(2);
					db.MemoRecover(memoID);
					db.close();
					//loadMemoList();
					getDelMemos();
					lapt.notifyDataSetChanged();
					Toast.makeText(this,"[ "+memoStr+" ]\n"+getString(R.string.btn_recover_Clicked), Toast.LENGTH_SHORT).show();
					break;
			}
		}
		
	}
	//############### VPager Listener Action #########################################################################
	
	//############### Dialog Listener Action #########################################################################
	public void ActClick(int ClickBtn, int RtnID){
		// All Erase Action
		if(RtnID==Dlg_ID_AllErase){
			switch(ClickBtn){
				case 1:
					// Do Action
					db.open(2);
					long comitRec=db.allMemoErase();
					db.close();
					getDelMemos();
					Toast.makeText(ThisConte,getString(R.string.btn_erase_Clicked)+"\n"+String.valueOf(comitRec),Toast.LENGTH_SHORT).show();
					break;
				case 2:
				case 4:
					// Cancel Action
					Toast.makeText(ThisConte,getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
					break;
				case 0:
					// Exception Error
					Toast.makeText(ThisConte,getString(R.string.excep_Err),Toast.LENGTH_SHORT).show();
					break;
			}
		// All Recover Action
		}else if(RtnID==Dlg_ID_AllRcover){
			switch(ClickBtn){
				case 1:
					// Do Action
					db.open(2);
					long comitRec=db.allMemoRcover();
					db.close();
					getDelMemos();
					Toast.makeText(ThisConte,getString(R.string.btn_recover_Clicked)+"\n"+String.valueOf(comitRec),Toast.LENGTH_SHORT).show();
					break;
				case 2:
				case 4:
					// Cancel Action
					Toast.makeText(ThisConte,getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
					break;
				case 0:
					// Exception Error
					Toast.makeText(ThisConte,getString(R.string.excep_Err),Toast.LENGTH_SHORT).show();
					break;
			}
		}else{
			// Exception Error
			Toast.makeText(ThisConte,getString(R.string.excep_Err),Toast.LENGTH_SHORT).show();
		}
	}
	//############### Dialog Listener Action #########################################################################
	
}
