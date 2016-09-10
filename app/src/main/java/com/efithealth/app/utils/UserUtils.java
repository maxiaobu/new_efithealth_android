package com.efithealth.app.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.efithealth.applib.controller.HXSDKHelper;
import com.efithealth.app.DemoHXSDKHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.efithealth.R;
import com.efithealth.app.domain.User;
import com.squareup.picasso.Picasso;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(username);
       if(user == null){
            user = new User(username);
        }
        
        return user;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
        if(user != null && user.getAvatar() != null&&!user.getAvatar().equals("")){
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    
    /**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	User user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }

    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
    
    /**
     * 保存或更新某个用户
     * @param user
     */
	public static void saveUserInfo(User newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}
	public static void updateCurrentUserNick(String nickname){
		 ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().updateParseNickName(nickname);
	}
    public static void updateCurrentUserAvatar(String avatar){
    	 ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().setCurrentUserAvatar(avatar);
    }
    public static void updateUserInfo(	Map<String, User> localUsers){
    	ArrayList<User> mList = new ArrayList<User>();
        mList.addAll(localUsers.values());
    	((DemoHXSDKHelper)HXSDKHelper.getInstance()).updateContactList(mList) ;
    }
    
    
}
