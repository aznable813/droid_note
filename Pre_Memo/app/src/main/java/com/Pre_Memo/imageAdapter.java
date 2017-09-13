package com.Pre_Memo;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.graphics.drawable.*;
import android.content.*;
import java.util.zip.*;

public class imageAdapter extends BaseAdapter{
	public static final int StartImgId = 1;
	public static final int DEF_ICON=1;
	
	private ArrayList<Drawable> imgList;
	private LayoutInflater mInfra;
	private int rowLayoutID;
	
	
	public imageAdapter(Context conte,int rowlayoutid){
		mInfra=(LayoutInflater)conte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowLayoutID=rowlayoutid;
	}
	
	public void addImgs(ArrayList<Drawable> imgs){
		imgList=imgs;	
	}
	
	@Override
	public Drawable getItem(int p1){
		// TODO: Implement this method
		return imgList.get(p1);
	}
	@Override
	public long getItemId(int p1){
		// TODO: Implement this method
		return p1;
	}

	@Override
	public int getCount(){
		// TODO: Implement this method
		return imgList.size();
	}

	@Override
	public View getView(int position, View convView, ViewGroup parent){
		// TODO: Implement this method
		final cst_LayoutView LayView;
		
		if(convView==null){
			LayView=(cst_LayoutView)mInfra.inflate(rowLayoutID,null);
		}else{
			LayView=(cst_LayoutView)convView;
		}
		LayView.bindView(getItem(position));
		return LayView;
	}
}
