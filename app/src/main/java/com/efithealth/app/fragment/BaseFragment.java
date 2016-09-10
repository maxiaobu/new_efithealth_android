package com.efithealth.app.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class BaseFragment extends Fragment {

	protected ProgressDialog dialog;

	public void showProgressDialog() {

		dialog = new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存中...");
		dialog.show();

	}
	public void showProgressDialog1() {

		dialog = new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在加载...");
		dialog.show();

	}
	public void showH5Dialog(String title,String message,String [] arr) {
//		new AlertDialog.Builder(getActivity())
//		.setTitle("title")
//		.setMessage("message")
//		.setPositiveButton("是", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Toast.makeText(getActivity(), "yes", 0).show();
//				
//			}
//		})
//		.setNegativeButton("否", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Toast.makeText(getActivity(), "no", 0).show();
//				
//			}
//		})
//		.show();
		
		
		
		//单选框
		 new AlertDialog.Builder(getActivity())  
				.setTitle(title)  
				.setIcon(android.R.drawable.ic_dialog_info)                  
				.setSingleChoiceItems(arr, 0,   
				  new DialogInterface.OnClickListener() {  
				                              
				     public void onClick(DialogInterface dialog, int which) {  
				    	 Log.i("basefragment", which+"");
				    	
//				        dialog.dismiss();  
				     }  
				  }  
				)  
				.setNegativeButton("取消", null)  
				.show();  
				
//				列表对话框
//				new AlertDialog.Builder(getActivity())  
//				.setTitle("列表框")  
//				.setItems(new String[] {"列表项1","列表项2","列表项3"}, null)  
//				.setNegativeButton("确定", null)  
//				.show();  
		 

	}
	public void closeProgressDialog(){
		dialog.dismiss();
	}
	

}
