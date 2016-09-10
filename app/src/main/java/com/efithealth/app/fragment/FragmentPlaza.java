package com.efithealth.app.fragment;

import com.efithealth.R;
import com.efithealth.app.activity.MainActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPlaza extends BaseFragment {

	private View v;
	private LinearLayout tv_hot,tv_group;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v=inflater.inflate(R.layout.fragment_plaza, container,false);
		return v;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		tv_hot=(LinearLayout) v.findViewById(R.id.finds_hot);
		tv_group=(LinearLayout) v.findViewById(R.id.finds_group);
		tv_hot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.instance.setTabSelection(601);
			}
		});
		
		tv_group.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.instance.setTabSelection(602);
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
