package com.lyl.mychat;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.lyl.mychat.fragment.TabFragment;
import com.lyl.mychat.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));

    //SparseArray类似于map，key的类型为int，是Android特有的，比map效率高
    //private List<TabFragment> fragments = new ArrayList<>();
    private SparseArray<TabFragment> fragments = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.d("onCreate");

        initViews();
        initDatas();

        //add fragment的错误写法！
        //旋转屏幕时，onCreate会执行，此时activity中会出现4个新对象。
        // 但此后不会调用getItem方法，因为fragmentManger和FragmentPagerAdapter管理fragment，
        // 屏幕旋转后，fragmentManger和FragmentPagerAdapter可以恢复上次的fragment对象。不需要再次调用getItem方法
        //fragments.add(TabFragment.newInstance(mTitles.get(0)));
        //fragments.add(TabFragment.newInstance(mTitles.get(1)));
        //fragments.add(TabFragment.newInstance(mTitles.get(2)));
        //fragments.add(TabFragment.newInstance(mTitles.get(3)));

        //此时操作fragments，会无法执行成功
        //fragments.get(0);

        //大部分应用使用 android:screenOrientation="portrait"，使屏幕不会旋转，但APP很可能在后台被杀死，
        // 当APP重新返回主界面时，依然会出现只执行onCreate方法，不走getItem方法。
        // 此时调用fragment的方法，不生效

    }

    private void initDatas() {
        //https://blog.csdn.net/xiaolaohuqwer/article/details/75670294
        // viewpager是默认预加载下一页的界面的。
        // viewpager提供了一个设置预加载页面数量的方法，那就是setOffscreenPageLimit()。
        // 默认不设置数量的情况下预加载下一页。设置0和1是同样的效果。设置2表示预加载下2页。
        //使用mTitles.size()，当扩展title的时候，此处代码不需修改。
        mViewPager.setOffscreenPageLimit(mTitles.size());

        //FragmentPagerAdapter：对fragment的回收，会执行onDestroyView方法，fragment并不会完全销毁掉。
        //  当再次回到以回收的fragment时，会调用onCreateView方法。
        //FragmentStatePagerAdapter:对fragment的回收，会执行onDestroyView、onDestroy方法，
        //  完全销毁滑动过去的fragment。当需要初始化的时候，会重新初始化页面，调用onCreate、onCreateView方法。

        //4个Tab，如微信这样的，都是主tab，不希望fragment那么容易被销毁。
        // 当从第四页回到第一页时，不用再重新请求数据，用FragmentPagerAdapter

        //1000个tab，如图片预览，不销毁对象，内存消耗过快，此时要用FragmentStatePagerAdapter。可很好的控制内存。

        //此处用FragmentPagerAdapter，若fragment被销毁，再onCreate的时候，网络请求很耗时。
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
//                fragments.get(i);
                L.d("i = " + i);
                return TabFragment.newInstance(mTitles.get(i));
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                //add fragment的正确写法！
                TabFragment fm = (TabFragment)super.instantiateItem(container, position);
                fragments.put(position, fm);
                L.d("instantiateItem i = " + position);
                return fm;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                fragments.remove(position);
                L.d("destroyItem i = " + position);
                super.destroyItem(container, position, object);
            }
        });
    }

    private void initViews() {
        mViewPager = findViewById(R.id.vp);

        findViewById(R.id.btn_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragment 和 Activity的交互
                //如何获取第一个fragment
                TabFragment tabFragment = fragments.get(0);
                //若设置的是默认缓存，即mViewPager.setOffscreenPageLimit(size);的参数为0或1时
                // 左边一个fragment，右边一个fragment，滑动到第3个fragment时，
                // 第0个fragment已经被remove了，此时tabFragment为null
                if (null != tabFragment) {//判空
                    tabFragment.changeTitle("微信改变了");
                }
            }
        });

    }
}
