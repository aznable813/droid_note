package com.Pre_Memo;
import android.content.*;
import android.graphics.drawable.*;
import android.support.v7.appcompat.*;
import android.util.*;
import android.widget.*;

public class cst_LayoutView extends LinearLayout{
	
	
    // アイコン
    ImageView mIconView;

    public cst_LayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	// Action event when parent inflate is finished
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIconView = (ImageView) findViewById(R.id.spinner_rowImageView);
    }
	
	// put imagedata into a viewHolder
    public void bindView(Drawable item) {
        mIconView.setImageDrawable(item);
    }
	
}
