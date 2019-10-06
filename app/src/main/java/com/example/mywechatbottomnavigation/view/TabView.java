package com.example.mywechatbottomnavigation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mywechatbottomnavigation.R;

public class TabView extends FrameLayout {

    private ImageView mNormalImageView, mNormalSelectedImageView;
    private TextView mTitleView;

    private String mTitle;
    private Drawable mNormalDrawable, mNormalSelectedDrawable;
    private int mTargetColor;

    // 标题和轮廓图默认的着色
    private static final int DEFAULT_TAB_COLOR = 0xff000000;

    // 标题和轮廓图最终的着色
    private static final int DEFAULT_TAB_TARGET_COLOR = 0x11E53;


    public TabView(@androidx.annotation.NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.tab_layout, this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        for(int i = 0; i < ta.getIndexCount(); i++){
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.TabView_tabColor:
                    // 获取标题和轮廓最终的着色
                    mTargetColor = ta.getColor(attr, DEFAULT_TAB_TARGET_COLOR);
                    break;

                case R.styleable.TabView_tabImage:
                    // 获取轮廓图
                    mNormalDrawable = ta.getDrawable(attr);
                    break;

                case R.styleable.TabView_tabSelectedImage:
                    // 获取选中图
                    mNormalSelectedDrawable = ta.getDrawable(attr);
                    break;

                case R.styleable.TabView_tabTitle:// 获取标题
                    // 获取标题
                    mTitle = ta.getString(attr);
                    break;

            }
        }
        ta.recycle();

    }

    /**
     * 当加载完XML文件后就会被调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = findViewById(R.id.tab_title);
        mTitleView.setText(mTitle);
        mTitleView.setTextColor(DEFAULT_TAB_COLOR);

        mNormalImageView = findViewById(R.id.tab_image);
        mNormalDrawable.setTint(DEFAULT_TAB_COLOR);
        mNormalDrawable.setAlpha(255);
        mNormalImageView.setImageDrawable(mNormalDrawable);

        mNormalSelectedImageView = findViewById(R.id.tab_selected_image);
        mNormalSelectedDrawable.setAlpha(0);
        mNormalSelectedImageView.setImageDrawable(mNormalSelectedDrawable);
    }

    /**
     * 根据进度值进行变色和透明度处理。
     *
     * @param percentage 进度值，取值[0, 1]。
     */
    public void setXPercentage(float percentage){

        if(percentage < 0 || percentage > 1){
            return;
        }
        // 1. 颜色变换
        int finalColor = evaluate(percentage, DEFAULT_TAB_COLOR, mTargetColor);
        mTitleView.setTextColor(finalColor);
        mNormalDrawable.setTint(finalColor);

        // 2. 透明度变换
        if(percentage >= 0.5 && percentage <= 1){
            int alpha = (int) Math.ceil(255 * (2 * percentage - 1 ));
            mNormalDrawable.setAlpha(255 - alpha);
            mNormalSelectedDrawable.setAlpha(alpha);
        }else{
            mNormalDrawable.setAlpha(255);
            mNormalSelectedDrawable.setAlpha(0);
        }

        invalidateUI();
    }

    /**
     * 根据不同线程更新UI。
     */
    private void invalidateUI(){
        if(Looper.myLooper() == Looper.getMainLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }

    /**
     * 计算不同进度值对应的颜色值，这个方法取自 ArgbEvaluator.java 类。
     *
     * @param percentage 进度值，范围[0, 1]。
     * @param startValue 起始颜色值。
     * @param endValue 最终颜色值。
     * @return 返回与进度值相应的颜色值。
     */
    private int evaluate(float percentage, int startValue, int endValue){
        int startInt = startValue;
        float startA = ((startInt >> 24) & 0xff) / 255.0f;
        float startR = ((startInt >> 16) & 0xff) / 255.0f;
        float startG = ((startInt >>  8) & 0xff) / 255.0f;
        float startB = (startInt         & 0xff) / 255.0f;

        int endInt = endValue;
        float endA = ((endInt >> 24) & 0xff) / 255.0f;
        float endR = ((endInt >> 16) & 0xff) / 255.0f;
        float endG = ((endInt >>  8) & 0xff) / 255.0f;
        float endB = (endInt         & 0xff) / 255.0f;

        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        float a = startA + percentage * (endA - startA);
        float r = startR + percentage * (endR - startR);
        float b = startB + percentage * (endB - startB);
        float g = startG + percentage * (endG - startG);

        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);


    }

}
