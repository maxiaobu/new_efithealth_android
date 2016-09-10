package com.efithealth.app.maxiaobu.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.efithealth.R;
import com.efithealth.app.maxiaobu.utils.RequestManager;


/**
 * Created by 马小布 on 2016/7/29.
 *
 * 设置过渡动画：后进原不动
 */
public class BaseFrg extends Fragment {
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }
}
