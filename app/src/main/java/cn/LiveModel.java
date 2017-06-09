package cn;

import android.content.Context;

import com.hyphenate.easeui.model.EasePreferenceManager;

import java.util.Map;

import cn.ucai.live.data.local.LiveDao;
import cn.ucai.live.data.model.Gift;

/**
 * Created by Administrator on 2017/6/8.
 */
public class LiveModel {
    LiveDao dao ;
    protected Context context=null;

    public LiveModel() {
    }

    /**
     * save current username
     * @param username
     */
    public void setCurrentUserName(String username){
        EasePreferenceManager.getInstance().setCurrentUserName(username);
    }

    public String getCurrentUserName(){
        return EasePreferenceManager.getInstance().getCurrentUsername();
    }

    public Map<Integer,Gift> getGiftList() {
        dao = new LiveDao();
        return dao.getGiftList();
    }
}
