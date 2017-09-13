package com.mycompany.Multi_Dialog;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DialogFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.app.*;


public class FileSelectDialog extends DialogFragment implements OnClickListener {
	
	private Context myConte;
	private boolean DirFlg;
	private int FrgmntID;
	private String FltStr;
	private boolean FltBackStr;
	
    /** リスナー */
    private OnFileSelectDialogListener thisListener = null;
    /** ファイル情報 */
    private File fileData = null;
    /** 表示中のファイル情報リスト */
    private List<File> viewFileDataList = null;
    
	//Const
	public FileSelectDialog(Context conte,boolean dirflg,int frgmntid) {
		this(conte,dirflg,frgmntid,null,false);
    }
	public FileSelectDialog(Context conte,boolean dirflg,int frgmntid,String fltstr,boolean fltback) {
		myConte=conte;
	  	DirFlg=dirflg;
		FrgmntID=frgmntid;
		FltStr=fltstr;
		FltBackStr=fltback;
    }
    //@param dialog ダイアログ
    //@param which 選択位置
    @Override
    public void onClick(DialogInterface dialog, int which) {
		if (viewFileDataList.get(which).isDirectory()) {
			show(viewFileDataList.get(which).getAbsolutePath() + "/");
		}else{
			thisListener.onClickFileSelect(viewFileDataList.get(which),DirFlg,FrgmntID);
		}
    }
	
	// show Dialog method with items in the indicating direcctory   
    // @param dirPath = indicating direcctory 
    public void show(final String dirPath) {
        // File infomations in the indicating Directory
        fileData = new File(dirPath);
        // Filelist in the indicating Directory
        File[] fileArray = fileData.listFiles();
        // 名前リスト
        List<String> nameList = new ArrayList<String>();

        if (fileArray != null) {
            // ファイル情報マップ
            Map<String, File> map = new HashMap<String, File>();
            for (File file : fileArray) {
				if (file.isDirectory()) {
					nameList.add(file.getName() + "/");
					map.put(nameList.get(map.size()), file);
				}else{
					if(!DirFlg){
						if(FltStr==null){
							nameList.add(file.getName());
							map.put(nameList.get(map.size()), file);
						}else{
							if(file.getName().length()>=FltStr.length()){
								if(FltBackStr){
									if(file.getName().substring(file.getName().length()-(FltStr.length())).equals(FltStr)){
										nameList.add(file.getName());
										map.put(nameList.get(map.size()), file);
									}
								}else{
									if(file.getName().substring(0,FltStr.length()).equals(FltStr)){
										nameList.add(file.getName());
										map.put(nameList.get(map.size()), file);
									}
								}
							}
							
						}
						
					}
				}
			}
			
            // ソート
            Collections.sort(nameList);

            // ファイル情報リスト
            viewFileDataList = new ArrayList<File>();

            for (int i = 0; i < nameList.size(); i++) {
                viewFileDataList.add(map.get(nameList.get(i)));
            }
        }

        // ダイアログを生成
        AlertDialog.Builder dialog = new AlertDialog.Builder(myConte);
        dialog.setTitle(dirPath);
        dialog.setItems(nameList.toArray(new String[0]), this);
		
		if(DirFlg){
			dialog.setIcon(R.drawable.icon_select_folder);
			
        	dialog.setPositiveButton(myConte.getString(R.string.FileSelectDlg_Here), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int value) {
					thisListener.onClickFileSelect(fileData,DirFlg,FrgmntID);
				}
			});
		}else{
			dialog.setIcon(R.drawable.icon_select_file);
		}
		
        dialog.setNeutralButton(myConte.getString(R.string.FileSelectDlg_parent),new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int value) {
				if (!"/".equals(dirPath)) {
					String dirPathNew = dirPath.substring(0, dirPath.length() - 1);
					dirPathNew = dirPathNew.substring(0, dirPathNew.lastIndexOf("/") + 1);
					// to parent
					show(dirPathNew);
				} else {
					// stay directory
					show(dirPath);
				}
			}
		});
		dialog.setNegativeButton(myConte.getString(R.string.FileSelectDlg_cancel),new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int value) {
				thisListener.onClickFileSelect(null,DirFlg,FrgmntID);
			}
		});
		
        dialog.show();
    }
	
 	//Set Listener Method
    public void setOnFileSelectDialogListener(OnFileSelectDialogListener listener) {
        thisListener = listener;
    }
	//Remove Listener Method
    public void removeFileSelectDialogListener() {
        thisListener = null;
    }

    // Dialog List onClick interface 
    public interface OnFileSelectDialogListener {
        public void onClickFileSelect(File file,boolean dirflg,int RtnID);
    }
}
