package com.csf.segment.lib;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csf.segment.R;


public class Segment extends LinearLayout implements View.OnClickListener{

  public interface OnTabChangedListener{
    void onTabChanged(int index);
  }
  
  private OnTabChangedListener mOnTabChangedListener;
  
  private static final int MODE_BLUE = 0;
  private static final int MODE_RED = 1;
  private String[] defaultEntries = {"操作一","操作二"};
  private int mPadding;
  private int mTabLfetBackground;
  private int mTabRightBackground;
  private int mTabMiddleBackground;
  private int mLineColor;
  private int mCurrentTabIndex;
  private ColorStateList mTabTextColor;
  private List<TextView> mTabs = new ArrayList<TextView>();
  private int mSelectedIndex = 0;
  public Segment(Context context) {
    this(context, null);
  }

  public Segment(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Segment(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs);
    setOrientation(HORIZONTAL);
    mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0f, context.getResources().getDisplayMetrics());
//    setPadding(mPadding,mPadding,mPadding,mPadding);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTabHost, defStyle, 0);
    CharSequence[] textArray = a.getTextArray(R.styleable.CustomTabHost_entries);
    int colorMode = a.getInt(R.styleable.CustomTabHost_colorMode, MODE_BLUE);
    mSelectedIndex = a.getInteger(R.styleable.CustomTabHost_selectedIndex, 0);
    a.recycle();
    switch (colorMode) {
      case MODE_RED:
        mTabLfetBackground = R.drawable.tabhost_tab_red_left_bg;
        mTabRightBackground = R.drawable.tabhost_tab_red_right_bg;
        mTabMiddleBackground = R.drawable.tabhost_tab_red_mid_bg;
        mLineColor = R.color.btn_tab_normal_red;
        mTabTextColor = context.getResources().getColorStateList(R.color.btn_tab_text_red_color);
        break;
      default:
        mTabLfetBackground = R.drawable.tabhost_tab_blue_left_bg;
        mTabRightBackground = R.drawable.tabhost_tab_blue_right_bg;
        mTabMiddleBackground = R.drawable.tabhost_tab_blue_mid_bg;
        mLineColor = R.color.btn_tab_normal;
        mTabTextColor = context.getResources().getColorStateList(R.color.btn_tab_text_blue_color);
        break;
    }
    
    if(textArray!=null){
      setEntries(textArray);
    }else{
      setEntries(defaultEntries);
    }
  }

  public void setEntries(CharSequence[] entries){
    removeAllViews();
    mTabs.clear();
    LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    LayoutParams lineParams = new LinearLayout.LayoutParams(mPadding,LayoutParams.MATCH_PARENT);
    params.gravity = Gravity.CENTER;
    for(int i=0;i<entries.length;i++){
      TextView button = new TextView(getContext());
      button.setGravity(Gravity.CENTER);
      button.setText(entries[i]);
      if(i==0){
        button.setBackgroundResource(mTabLfetBackground);
      }else if(i==entries.length-1){
        button.setBackgroundResource(mTabRightBackground);
      }else{
        button.setBackgroundResource(mTabMiddleBackground);
      }
      button.setLayoutParams(params);
      button.setTextColor(mTabTextColor);
      addView(button);
      if(i!=entries.length-1){
       View view = new View(getContext());
       view.setLayoutParams(lineParams);
       view.setBackgroundResource(mLineColor);
       addView(view);
      }
      
      button.setOnClickListener(this);
      button.setTag(i);
      mTabs.add(button);
    }
    setCurrentTab(mSelectedIndex);
  }

  @Override
  public void onClick(View v) {
    int index = (Integer) v.getTag();
    for(int i=0;i<mTabs.size();i++){
      if(i == index){
        mCurrentTabIndex = i;
        mTabs.get(i).setEnabled(false);
        if(mOnTabChangedListener!=null) mOnTabChangedListener.onTabChanged(i);
      }else{
        mTabs.get(i).setEnabled(true);
      }
    }
  }
  
  public void setCurrentTab(int index){
    if(index>=mTabs.size()||index<0){
      throw new UnsupportedOperationException("index is out of range");
    }
    for(int i=0;i<mTabs.size();i++){
      if(i == index){
        mTabs.get(i).performClick();
      }
    }
  }
  
  public void setOnTabChangedListener(OnTabChangedListener listener){
    mOnTabChangedListener = listener;
  }
  
  public int getCurrentTabIndex(){
    return mCurrentTabIndex;
  }
}
