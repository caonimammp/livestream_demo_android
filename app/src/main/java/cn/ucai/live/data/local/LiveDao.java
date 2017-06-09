package cn.ucai.live.data.local;

import java.util.List;
import java.util.Map;

import cn.ucai.live.data.model.Gift;

/**
 * Created by Administrator on 2017/6/9.
 */

public class LiveDao {
    public static final  String GIFT_TABLE_NAME="t_superwechat_gift";
    public static final  String GIFT_COLUMN_ID="m_gift_id";
    public static final  String GIFT_COLUMN_NAME="m_gift_name";
    public static final  String GIFT_COLUMN_URL="m_gift_url";
    public static final  String GIFT_COLUMN_PRICE="m_gift_price";

    public void setGIftList(List<Gift> list){
        LiveDBManager.getInstance().saveContactList(list);
    }
    public Map<Integer,Gift> getGiftList(){
        return LiveDBManager.getInstance().getGiftList();
    }
}
