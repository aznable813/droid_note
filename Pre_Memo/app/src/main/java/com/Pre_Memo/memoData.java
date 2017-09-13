package com.Pre_Memo;

public class memoData{
	
	private int iconID;
	private long iD;
	private String memoStr;
	private String upDate;
	private int dataType;
	
	//Const
	public memoData(long id,String memostr,String update,int datatype){
		this.iD = id;
		this.memoStr=memostr;
		this.upDate=update;
		this.dataType=datatype;
	}
	
	//setter
	public void setIconId(int pIconId){
		iconID=pIconId;
	}
	
	//getter
	public int getIconID(){
		return iconID;
	}
	public long getId(){
		return iD;
	}
	public String getMemoStr(){
		return memoStr;
	}
	public String getUpDate(){
		return upDate;
	}
	public int getDataType(){
		return dataType;
	}
}
