package com.Pre_Memo;
import android.app.*;
import android.os.*;
import android.support.v7.widget.Toolbar;
import android.widget.*;
import android.content.*;
import android.database.*;
import android.view.*;

public class Garbage_Contents extends Activity{
	private final Context ThisConte=this;
	private DBAdapter db;
	private GetAppArarys getarray;
	private Toolbar toolbar;
	private ImageView iconIV;
	private int IconID;
	private TextView delMemoTxt;
	private String DelMemoStr;
	private MenuItem BackItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garbage_contents);
		toolbar =(Toolbar)findViewById(R.id.del_contents_toolbar);
		toolbar.inflateMenu(R.menu.garbage_contents_menu);
		
		db=new DBAdapter(ThisConte);
		
		Intent intent = getIntent();
		long TarId = intent.getLongExtra("DelTargetID",0);
	    getarray=new GetAppArarys(ThisConte);
		db.open(1);
		 	Cursor c = db.getMemoById(TarId);
			c.moveToFirst();
			IconID=getarray.get_ImgID().get(c.getInt(c.getColumnIndex(db.ICON_ID)));
			DelMemoStr=c.getString(c.getColumnIndex(db.MEMO_DATA));
		db.close();
		
		FindIDs();
		setDelForm();
		getMenuItem();
		setItemListeners();
	}
	
	
	private void FindIDs(){
		iconIV=(ImageView)findViewById(R.id.garbage_contensIV);
		delMemoTxt=(TextView)findViewById(R.id.del_txt_v);
	}
	private void getMenuItem(){
		BackItem=toolbar.getMenu().findItem(R.id.return_garbage);
	}
	private void setDelForm(){
		iconIV.setImageResource(IconID);
		delMemoTxt.setText(DelMemoStr);
	}
	private void setItemListeners(){
		//@@ SaveItem Click
		BackItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem backItem){
				//do Action
				Intent intent =new Intent(ThisConte,Garbage_Can_View.class);
				startActivity(intent);
				return true;
			}
		});
	}
	
	@Override
	protected void onResume(){
		// TODO: Implement this method
		super.onResume();
		setDelForm();
	}
}
