package cn;

import android.content.Context;

import com.hyphenate.easeui.model.EasePreferenceManager;

/**
 * Created by Administrator on 2017/6/8.
 */
public class LiveModel {
    /**
     * save current username
     * @param username
     */
    public void setCurrentUserName(String username){
        EasePreferenceManager.getInstance().setCurrentUserName(username);
    }

    public String getCurrentUsernName(){
        return EasePreferenceManager.getInstance().getCurrentUsername();
    }
}
