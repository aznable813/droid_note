package com.Pre_Memo;
import android.widget.*;
import android.content.*;
import android.util.*;

public class LayoutView extends LinearLayout{
	ImageView iconIV;
	TextView TitleStr;
	TextView UpdateStr;
	
	public LayoutView(Context conte,AttributeSet attr){
		super(conte,attr);
	}

	@Override
	protected void onFinishInflate(){
		// TODO: Implement this method
		super.onFinishInflate();
		iconIV=(ImageView)findViewById(R.id.List_Icon);

		TitleStr=(TextView)findViewById(R.id.List_Title);
		
		UpdateStr=(TextView)findViewById(R.id.List_Update);
		
	}
	
	public void BindView(memoData memo){
		iconIV.setImageResource(memo.getIconID());
		TitleStr.setText(memo.getMemoStr());
		UpdateStr.setText(memo.getUpDate());
	}
	
}
