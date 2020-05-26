package com.lyl.mychat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyl.mychat.R;
import com.lyl.mychat.utils.L;

/**
 * Created by lym on 2020/5/26
 * Describe :
 */
public class TabFragment extends Fragment {
    private static final String BUNDLE_TITLE = "bundle_title";

    private TextView mTvTitle;
    private String mTitle;

    public static TabFragment newInstance(String title){ //0530
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        TabFragment fm = new TabFragment();

        // 应用置于后台，被杀死进程。回到该应用时，系统会调用默认的构造方法，但是不会调用这句。
        // 此时mTitle为null。但是newInstance中设置了一个Arguments，重进APP时，Arguments会恢复，
        // 此时title可从Arguments中取值。
        //fm.mTitle = title;

        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) { //0530
            mTitle = arguments.getString(BUNDLE_TITLE, "");
        }
//        L.d("onCreate， title = " + mTitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //L.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_tab, container, false);//0530
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //L.d(TAG, "onViewCreated");
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);
    }

    @Override
    public void onDestroyView() {
//        L.d("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
//        L.d("onDestroy");
        super.onDestroy();
    }

    public void changeTitle(String title){

        //只有fragment被add到activity中后，fragment生命周期方法才会被执行，mTvTitle才被初始化。
        // fragment暴露给外界操作其view时，要判断是否初始化
        if (!isAdded() || !isResumed()) return;

        mTvTitle.setText(title);
    }
}
