package com.efithealth.app.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.efithealth.R;
import com.efithealth.app.maxiaobu.base.BaseAty;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LunchDetailActivity extends BaseAty implements AppBarLayout.OnOffsetChangedListener {

    @Bind(R.id.iv_food)
    ImageView mIvFood;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctl_name)
    CollapsingToolbarLayout mCtlName;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.root_layout)
    CoordinatorLayout mRootLayout;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_detail);
        ButterKnife.bind(this);

        initView();
        initData();


    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mAppBar.addOnOffsetChangedListener(this);
        mActionBar.setTitle("");
        CoordinatorLayout.LayoutParams linearParams = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        linearParams.height = width;
        mAppBar.setLayoutParams(linearParams);
    }

    private void initData() {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset <= -mCtlName.getHeight() + mToolbar.getHeight() + 80) {
            mActionBar.setTitle("sdkjfhsdkjhfkjdsh");
        } else {
            mActionBar.setTitle("");
        }
    }
}
