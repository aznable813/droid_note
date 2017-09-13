package com.mycompany.Multi_Dialog;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.app.*;
import android.os.*;
import java.util.*;

public class CustomDialogFragment extends DialogFragment {
	
	private DialogLstnr Lstnr=null;
	private Dialog dialog;
	private Button Positive_btn;
	private Button Negative_btn;
	private Button Cancel_btn;
	private TextView txTitle;
	private TextView txDLmsg;
	private Button Close_btn;
	
	private String title;
	private String message;
	private int type;
	private int RtnID;
	
	
	public static final int Type_O_C=1;
	public static final int Type_Y_N=2;
	public static final int Type_Y_N_C=3;

	
	// Factory Method
	// @param 	type=1,OK or Cancel
	//			type=2,Yes or No 
	//			type=3,Yes or No or Cancel
	
	public static CustomDialogFragment newInstance(String ar_title,String ar_DLmsg,int ar_type,int ar_RtnID){
		CustomDialogFragment instance = new CustomDialogFragment();
		
		//Set Bundle param
		Bundle Arguments = new Bundle();
		Arguments.putString("Title",ar_title);
		Arguments.putString("DLmsg",ar_DLmsg);
		Arguments.putInt("Type",ar_type);
		Arguments.putInt("RtnID",ar_RtnID);
		instance.setArguments(Arguments);
		return instance;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		//get bundle for param
		title = getArguments().getString("Title");
        message = getArguments().getString("DLmsg");
        type = getArguments().getInt("Type");
		RtnID=getArguments().getInt("RtnID");
		
		
        dialog = new Dialog(getActivity());
        // タイトル非表示
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		// 背景を透明にする
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// Set Views
		SetDialogViews(type,title,message);
		// Set Click Lisners
		setClickListers(type);
		
		
        return dialog;
    }
	
	// Inflate Dialog layout & Set Views
	private void SetDialogViews(int typemode,String title,String msg){
		//final String posiStr=getString(R.string.btn_yes);
		final String posiStr;
		final String negaStr;
		final String canStr;
		if(typemode==Type_O_C){
			posiStr=getString(R.string.CustomDialogFragment_btn_ok);
			negaStr=getString(R.string.CustomDialogFragment_btn_cancel);
		}else{
			posiStr=getString(R.string.CustomDialogFragment_btn_yes);
			negaStr=getString(R.string.CustomDialogFragment_btn_no);
		}
		if(typemode==Type_Y_N_C){
			canStr=getString(R.string.CustomDialogFragment_btn_cancel);
			dialog.setContentView(R.layout.dlg_btn3_lay);
			//Set ThirdBTN
			Cancel_btn = (Button)dialog.findViewById(R.id.Cancel_button);
			Cancel_btn.setText(canStr);
		}else{
			dialog.setContentView(R.layout.dlg_btn2_lay);
		}
		//Set PosiBTN
		Positive_btn = (Button)dialog.findViewById(R.id.positive_button);
		Positive_btn.setText(posiStr);
		//Set NegaBTN
		Negative_btn = (Button)dialog.findViewById(R.id.negative_button);
		Negative_btn.setText(negaStr);
		//Set Title
		txTitle = (TextView)dialog.findViewById(R.id.title);
		txTitle.setText(title);
		//Set DLmsg
		txDLmsg = (TextView)dialog.findViewById(R.id.message);
		txDLmsg.setText(msg);
		//Close_btn
		Close_btn = (Button)dialog.findViewById(R.id.close_button);
	}
	
	// Set listeners
	private void setClickListers(int typemode){
		Positive_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Action when on click positive button
				Lstnr.ActClick(1,RtnID);
				dismiss();
			}
		});
		Negative_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Action when on click negative button
				Lstnr.ActClick(2,RtnID);
				dismiss();
			}
		});
		Close_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Action when on click negative button
				Lstnr.ActClick(4,RtnID);
				dismiss();
			}
		});
		if(typemode==Type_Y_N_C){
			Cancel_btn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					//Action when on click negative button
					Lstnr.ActClick(3,RtnID);
					dismiss();
				}
			});
		}
	}
	
	//Method Set Listener
	public void setDialogLstnr(DialogLstnr listener){
		Lstnr=listener;
	}
	//Method Remove Listener
	public void removeDialogLstnr(){
		Lstnr=null;
	}

	@Override
	public void onAttach(Activity activity){
		// TODO: Implement this method
		super.onAttach(activity);
		Lstnr=(DialogLstnr)activity;
	}

	
	
	
}
