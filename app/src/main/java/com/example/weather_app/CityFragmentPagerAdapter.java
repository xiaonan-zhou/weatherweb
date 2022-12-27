package com.example.weather_app;


import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 适配器，将CityWeatherFragment加载到mainactivity
 *FragmentPagerAdapter：对于前一个页面和后一个页面有缓存，不会即使销毁
 * FragmentStatePagerAdapter：即使销毁，保留当前页面状态
 */
public class CityFragmentPagerAdapter extends FragmentStatePagerAdapter{
    List<Fragment>fragmentList;
    public CityFragmentPagerAdapter(FragmentManager fm,List<Fragment>fragmentLis) {
        super(fm);
        this.fragmentList = fragmentLis;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    int childCount = 0;   //表示ViewPager包含的页数
    //    当ViewPager的页数发生改变时，必须要重写两个函数
    @Override
    public void notifyDataSetChanged() {
        this.childCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount>0) {
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
