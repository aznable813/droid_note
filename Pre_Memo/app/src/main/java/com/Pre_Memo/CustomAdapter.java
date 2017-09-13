package com.Pre_Memo;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import android.support.v4.view.*;
import android.view.View.*;
import android.support.v4.content.*;

public class CustomAdapter extends BaseAdapter{
	
	public final static int Rtn_ListBody_clk = 1;
	public final static int Rtn_btn1_clk = 2;
	public final static int Rtn_btn2_clk = 3;
	
	private final Context ThisConte;
	private LayoutInflater ThisInfla;
	private ArrayList<memoData> MemoLists;
	private vp_btn_listener Listener = null; 
	private int vpMargin;
	private int ID_page1;
	private int ID_page2;
	private final int ListenerID;
	
	//Const  
	public CustomAdapter(
						Context conte,
						ArrayList<memoData> memos,
						int vpmargin,
						int res_page1,
						int res_page2,
						int listener_id){
		ThisConte=conte;
		ThisInfla=(LayoutInflater)ThisConte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		MemoLists=memos;
		vpMargin=vpmargin;
		ID_page1=res_page1;
		ID_page2=res_page2;
		ListenerID=listener_id;
	}
	
	@Override
	public int getCount(){
		return MemoLists.size();
		//return 0;
	}
	@Override
	public memoData getItem(int p1){
		return MemoLists.get(p1);
	}
	@Override
	public long getItemId(int p1){
		return MemoLists.get(p1).getId();
	}
	@Override
	public View getView(final int position, View ConvView, ViewGroup parent){
		final LinearLayout LL;
		if(ConvView==null){
			LL=(LinearLayout)ThisInfla.inflate(R.layout.vp_row,null);
		}else{
			LL=(LinearLayout)ConvView;
		}
		ViewPager vp = (ViewPager) LL.findViewById(R.id.ViewPager);
		vp.setPageMargin(-vpMargin);
		VP_Adapter vpa = new VP_Adapter(getItem(position));
		vp.setAdapter(vpa);
		return LL;
	}
	
	//inner class VP_Adapter
	private class VP_Adapter extends PagerAdapter{
		private memoData vpMemo;

		//Const
		public VP_Adapter(memoData memo){
			super();
			vpMemo=memo;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position){
			// TODO: Implement this method
			
			if(position==0){
				LayoutView Clv=(LayoutView)ThisInfla.inflate(ID_page1,null);
				Clv.BindView(vpMemo);
				container.addView(Clv);

				Clv.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						Listener.VP_ActClick(Rtn_ListBody_clk,ListenerID,vpMemo.getId(),vpMemo.getMemoStr());
					}
				});

				return Clv;
			}else{
				LinearLayout Llv=(LinearLayout)ThisInfla.inflate(ID_page2,null);
				container.addView(Llv);
					Llv.findViewById(R.id.btn_del).setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v){
								Listener.VP_ActClick(Rtn_btn1_clk,ListenerID,vpMemo.getId(),vpMemo.getMemoStr());
							}
						});
					if(Llv.findViewById(R.id.btn_recover)!=null){
						Llv.findViewById(R.id.btn_recover).setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v){
								//to do
								Listener.VP_ActClick(Rtn_btn2_clk,ListenerID,vpMemo.getId(),vpMemo.getMemoStr());
							}
						});
					}
				
				return Llv;
			}
		}
		@Override
		public void destroyItem(View container, int position, Object object){
			// TODO: Implement this method
			((ViewPager)container).removeView((View)object);
		}
		@Override
		public int getCount(){
			// TODO: Implement this method
			return 2;
		}

		@Override
		public boolean isViewFromObject(View p1, Object p2){
			// TODO: Implement this method
			return p1.equals(p2);
		}
	}
	
	//Vp_Btn_litener Method
	public void setVpBtnlistener(vp_btn_listener listener){
		Listener=listener;
	}
	public void removeVpBtnListener(){
		Listener=null;
	}
		
}
