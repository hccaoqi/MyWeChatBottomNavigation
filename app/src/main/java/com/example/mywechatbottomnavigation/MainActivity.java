package com.example.mywechatbottomnavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mywechatbottomnavigation.fragment.BaseFragment;
import com.example.mywechatbottomnavigation.view.TabView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tab_weixin)
    TabView mTabWeixin;

    @BindView(R.id.tab_contact)
    TabView mTabContext;

    @BindView(R.id.tab_find)
    TabView mTabFind;

    @BindView(R.id.tab_profile)
    TabView mTabProfile;

    @BindArray(R.array.tab_array)
    String[] mTabTitle;

    private List<TabView> mTabViews = new ArrayList<>();

    private static final int INDEX_WEIXIN = 0;
    private static final int INDEX_CONTACT = 1;
    private static final int INDEX_FIND = 2;
    private static final int INDEX_PROFILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTabViews.add(mTabWeixin);
        mTabViews.add(mTabContext);
        mTabViews.add(mTabFind);
        mTabViews.add(mTabProfile);

        mViewPager.setOffscreenPageLimit(mTabTitle.length - 1);
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            /**
             * @param position 滑动的时候，position总是代表左边的View， position+1总是代表右边的View
             * @param positionOffset 左边View位移的比例
             * @param positionOffsetPixels 左边View位移的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 左边View进行动画
                mTabViews.get(position).setXPercentage(1 - positionOffset);
                // 如果positionOffset非0，那么就代表右边的View可见，也就说明需要对右边的View进行动画
                if(positionOffset > 0){
                    mTabViews.get(position+1).setXPercentage(positionOffset);
                }
            }
        });

    }

    private void updateCurrentTab(int index){
        for(int i = 0; i < mTabViews.size(); i++){
            if(index == i){
                mTabViews.get(i).setXPercentage(1);
            }else{
                mTabViews.get(i).setXPercentage(0);
            }
        }
    }

    @OnClick({ R.id.tab_weixin, R.id.tab_contact, R.id.tab_find, R.id.tab_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_weixin:
                mViewPager.setCurrentItem(INDEX_WEIXIN, false);
                updateCurrentTab(INDEX_WEIXIN);
                break;
            case R.id.tab_contact:
                mViewPager.setCurrentItem(INDEX_CONTACT, false);
                updateCurrentTab(INDEX_CONTACT);
                break;
            case R.id.tab_find:
                mViewPager.setCurrentItem(INDEX_FIND, false);
                updateCurrentTab(INDEX_FIND);
                break;
            case R.id.tab_profile:
                mViewPager.setCurrentItem(INDEX_PROFILE, false);
                updateCurrentTab(INDEX_PROFILE);
                break;
        }
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return getTabFragment(i, mTabTitle[i]);
        }

        @Override
        public int getCount() {
            return mTabTitle.length;
        }

    }

    private Fragment getTabFragment(int index, String title){
        Fragment fragment = null;
        switch (index){
            case INDEX_WEIXIN:
                fragment = BaseFragment.newInstance(title);
                break;
            case  INDEX_CONTACT:
                fragment = BaseFragment.newInstance(title);
                break;
            case  INDEX_FIND:
                fragment = BaseFragment.newInstance(title);
                break;
            case  INDEX_PROFILE:
                fragment = BaseFragment.newInstance(title);
                break;
        }
        return fragment;
    }
}
