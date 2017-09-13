package com.Pre_Memo;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.content.*;
import com.mycompany.Multi_Dialog.*;
import java.io.*;
import java.text.*;
import java.sql.*;

public class MainActivity extends Activity implements DialogLstnr,FileSelectDialog.OnFileSelectDialogListener{
	
	private final Activity ThisActvty=this;
	private final Context thisConte=this;
	
	private final DialogLstnr lstener=this;
	private FileSelectDialog fileDlg;
	private FileSelectDialog.OnFileSelectDialogListener FileDlgLstnr = this;
	
	private static final int Dlg_Before_Add=1;
	private static final int Dlg_BK_RUN_OC=2;
	private static final int Dlg_Rstore_RUN_OC=3;
	private DBAdapter db;
	
	//File Select Dialog ID definision
	private static final int FSDlg_SAVE_AS=1;
	private static final int FSDlg_RESTORE_DB=2;
	
	private String DB_FileStr = DBAdapter.DB_NAME;
	private String DB_SrcPath;
	private String DB_SrcParent;
	//private String defPath;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
		setContentView(R.layout.home_page);
		setClickLsnrs();
		
		DB_SrcPath=thisConte.getFilesDir().getParent() + "/databases/"+DB_FileStr;
		DB_SrcParent=thisConte.getFilesDir().getParent() + "/databases/";
		
    }

	private void setClickLsnrs(){
		//Set to AddNew button
		findViewById(R.id.btn_to_add_new).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				ChkAndGo_input();
			}
		});
		//Set toMemo button
		findViewById(R.id.btn_to_memo).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(ThisActvty,input_memo.class);
				startActivity(intent);
			}
		});
		//Set toList button
		findViewById(R.id.btn_to_lists).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(ThisActvty,list_view.class);
				startActivity(intent);
			}
		});
		//Set to RecycleBin button
		findViewById(R.id.btn_to_recycle_bin).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(ThisActvty,Garbage_Can_View.class);
				startActivity(intent);
			}
		});
		//Set to Settings button
		findViewById(R.id.btn_to_settings).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent =new Intent(ThisActvty,SettingsPrf.class);
				startActivity(intent);
			}
		});
		//Set to Backup button
		findViewById(R.id.btn_to_backup).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				// Call Dialog 
				CustomDialogFragment mydialog =CustomDialogFragment.newInstance
					(getString(R.string.title_dlg_BefoBKUP)
				 	,getString(R.string.msg_dlg_BefoBKUP)
					,CustomDialogFragment.Type_O_C
					,Dlg_BK_RUN_OC);
				mydialog.setDialogLstnr(lstener);
				mydialog.setCancelable(false);
				mydialog.show(getFragmentManager(),"Dlg_BK_RUN_OC");
			}
		});
		//Set to Restore button
		findViewById(R.id.btn_to_restore).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				// Call Dialog 
				CustomDialogFragment mydialog =CustomDialogFragment.newInstance
					(getString(R.string.title_dlg_BefoRestore)
					,getString(R.string.msg_dlg_BefoRestore)
					,CustomDialogFragment.Type_O_C
					,Dlg_Rstore_RUN_OC);
				mydialog.setDialogLstnr(lstener);
				mydialog.setCancelable(false);
				mydialog.show(getFragmentManager(),"Dlg_Rstore_RUN_OC");
			}
		});
	}
	

	@Override
	protected void onPause(){
		// TODO: Implement this method
		super.onPause();
		
	}

//############### DefaultPath From preference Method #################################################
	private String GetDefPath(){
		// Downloadフォルダーのパス
		File pathExternalPublicDir = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DOWNLOADS);
		String dlpath = pathExternalPublicDir.getPath();
		
		Ope_Pref opf = new Ope_Pref(thisConte);
		return opf.getString(getString(R.string.K_CrntPath),dlpath);
	}
	private void setDefPath(String value){
		Ope_Pref opf = new Ope_Pref(thisConte);
		opf.saveString(getString(R.string.K_CrntPath),value);
	}
//############### DefaultPath From preference Method #################################################
	
//############### Check And Go Input Method #############################################################
	private void ChkAndGo_input(){
		db=new DBAdapter(thisConte);
		ChkPendingMemo Pm = new ChkPendingMemo(thisConte,db);
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
		Intent intent = new Intent(ThisActvty,input_memo.class);
		intent.putExtra("TargetID",input_memo.ClrFlg);
		startActivity(intent);
	}
//############### Go Input Method #######################################################################
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
		//When Btn_BackUp Selected
		}else if(RtnID==Dlg_BK_RUN_OC){
			switch(ClickBtn){
				case 1:
					fileDlg = new FileSelectDialog(thisConte,true,FSDlg_SAVE_AS);
					fileDlg.setOnFileSelectDialogListener(FileDlgLstnr);
					fileDlg.show(GetDefPath());
					break;
				case 2:
				case 4:
					Toast.makeText(getApplication(),getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
					break;
			}
		//When Btn_Restore Selected	
		}else if(RtnID==Dlg_Rstore_RUN_OC){
			switch(ClickBtn){
				case 1:
					fileDlg = new FileSelectDialog(thisConte,false,FSDlg_RESTORE_DB,DB_FileStr,true);
					fileDlg.setOnFileSelectDialogListener(FileDlgLstnr);
					fileDlg.show(GetDefPath());
					break;
				case 2:
				case 4:
					Toast.makeText(getApplication(),getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
					break;
			}
		}else{
			//stab
		}
	}

//############### DialogListener #######################################################################
//############### FileSelectDialogListener #######################################################################
	@Override
	public void onClickFileSelect(File file, boolean dirflg,int frgmntID){
		if(file!=null){
			//when Save as
			if(frgmntID==FSDlg_SAVE_AS){
				// TODO: Implement this method
				int BuffSize = 4;
				int RtnCd = 0;
				final File AgnFile = file;
				final boolean AgnDirflg = dirflg;
				final File Src = new File(DB_SrcPath);
				final File Dst = new File(file.getAbsolutePath()+"/"+DBAdapter.DB_NAME);

				File_OpeS FOS = new File_OpeS(thisConte);
				RtnCd=FOS.ChkFileCopy(Src,Dst,BuffSize);
				//when Nothing is err
				if(RtnCd==0){
					setDefPath(file.getAbsolutePath()+"/");
					Toast.makeText(thisConte,getString(R.string.msg_FinBKUP),Toast.LENGTH_SHORT).show();
				//When there is a same file in the directory
				}else if(RtnCd==2){
					AlertDialog.Builder builder = new AlertDialog.Builder(thisConte);
					builder.setTitle(DBAdapter.DB_NAME)
					.setMessage(getString(R.string.msg_preDelBKUP));
					builder.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//delete the same name file and retry
							if(Dst.delete()){
								onClickFileSelect(AgnFile,AgnDirflg,FSDlg_SAVE_AS);
							}else{
								//fail to delete the file
								Toast.makeText(thisConte,getString(R.string.BKOVWErrMsg),Toast.LENGTH_SHORT).show();
							}
						}
					});
					// Cancel Bkup process
					builder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Toast.makeText(thisConte,getString(R.string.Cancel_Act),Toast.LENGTH_SHORT).show();
						}
					});
					builder.show();
				}else{
					Toast.makeText(thisConte,getString(R.string.BKRsltErrMsg)+String.valueOf(RtnCd),Toast.LENGTH_SHORT).show();
				}
				
			//when Restore Run	
			}else if(frgmntID==FSDlg_RESTORE_DB){

				int RtnCd = 0;
				File DstFile = new File(thisConte.getFilesDir().getParent() + "/databases/"+DBAdapter.DB_NAME);
				File_OpeS FOS = new File_OpeS(thisConte);

				RtnCd = FOS.Copy2Genes(file,DstFile,4);
				
				if(RtnCd==0){
					final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					final Date dateFmt = new Date(System.currentTimeMillis());
					Ope_Pref opf = new Ope_Pref(thisConte);
					opf.saveString(getString(R.string.K_rcv_Btn_Date),df.format(dateFmt));
					opf.saveBoolean(getString(R.string.K_rcv_Btn),true);
					setDefPath(file.getParent()+"/");
					Toast.makeText(thisConte,getString(R.string.msg_FinRestore),Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(thisConte,getString(R.string.RstrRsltErrMsg)+String.valueOf(RtnCd),Toast.LENGTH_SHORT).show();
				}

			}else{
				//stub
			}

		}
	}
//############### FileSelectDialogListener #######################################################################

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

