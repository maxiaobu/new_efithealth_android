package com.efithealth.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.efithealth.R;
import com.easemob.exceptions.EaseMobException;

public class PublicGroupsSeachActivity extends BaseActivity{
    private RelativeLayout containerLayout;
    private EditText idET;
    private TextView nameText;
    public static EMGroup searchedGroup;
    private Button search_pub_group;

    @SuppressLint("InlinedApi")
	@Override
    protected void onCreate(Bundle arg0) {
    	if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}
        super.onCreate(arg0);
        setContentView(R.layout.activity_public_groups_search);
        
        containerLayout = (RelativeLayout) findViewById(R.id.rl_searched_group);
        idET = (EditText) findViewById(R.id.et_search_id);
        nameText = (TextView) findViewById(R.id.name);
        search_pub_group=(Button) findViewById(R.id.search_pub_group);
        search_pub_group.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				searchGroup();
			}
		});
        
        searchedGroup = null;
    }
    
    /**
     * 搜索
     * @param v
     */
    public void searchGroup(){
        if(TextUtils.isEmpty(idET.getText())){
            return;
        }
        
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.searching));
        pd.setCancelable(false);
        pd.show();
        
        new Thread(new Runnable() {

            public void run() {
                try {
                    searchedGroup = EMGroupManager.getInstance().getGroupFromServer(idET.getText().toString());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            containerLayout.setVisibility(View.VISIBLE);
                            nameText.setText(searchedGroup.getGroupName());
                        }
                    });
                    
                } catch (final EaseMobException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            searchedGroup = null;
                            containerLayout.setVisibility(View.GONE);
                            if(e.getErrorCode() == EMError.GROUP_NOT_EXIST){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.group_not_existed), 0).show();
                            }else{
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.group_search_failed) + " : " + getString(R.string.connect_failuer_toast), 0).show();
                            }
                        }
                    });
                }
            }
        }).start();
        
    }
    
    
    /**
     * 点击搜索到的群组进入群组信息页面
     * @param view
     */
    public void enterToDetails(View view){
        startActivity(new Intent(this, GroupSimpleDetailActivity.class));
    }
}
