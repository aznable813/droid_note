package com.Pre_Memo;
import android.content.*;
import java.util.*;
import android.content.res.*;
import android.graphics.drawable.*;

public class GetAppArarys {
	private Context ThisConte;
	
	//const
	public GetAppArarys(Context conte){
		ThisConte=conte;
	}
	
	//################  Methods  ################
	
	// Get iCon Drawable from arraylist
	public ArrayList<Drawable> get_ImgDrawable(){
		TypedArray imgList = ThisConte.getResources().obtainTypedArray(R.array.categ_list);
		ArrayList<Drawable> imgArray = new ArrayList<Drawable>();
		for(int i=0 ; i<imgList.length() ; i++){
			imgArray.add(i,imgList.getDrawable(i));	
		}
		return imgArray;
	}
	// Get iConIDs from arraylist
	public ArrayList<Integer> get_ImgID(){
		TypedArray imgList = ThisConte.getResources().obtainTypedArray(R.array.categ_list);
		ArrayList<Integer> ImgIdList=new ArrayList<Integer>();
		for(int i=0 ; i<imgList.length() ; i++){	
			ImgIdList.add(i,imgList.getResourceId(i,0));
		}
		return ImgIdList;
	}
	
}
