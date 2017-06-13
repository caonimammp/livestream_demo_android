package cn.ucai.live.ui.activity;

/**
 * Created by Administrator on 2017/6/12.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseUserUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.LiveHelper;
import cn.ucai.live.R;
import cn.ucai.live.data.model.Gift;

/**
 * Created by Administrator on 2017/6/12 0012.
 */

public class RoomGiftDialog extends DialogFragment {
    @BindView(R.id.rv_gift)
    RecyclerView rvGift;
    @BindView(R.id.tv_my_bill)
    TextView tvMyBill;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    Unbinder unbinder;
    static View.OnClickListener mOnClickListener;
    private GridLayoutManager mGridLayoutManager;
    private LivegiftAdapter adapter;
    List<Gift> list;

    public RoomGiftDialog() {

    }


    public static RoomGiftDialog newInstance() {
        RoomGiftDialog dialog = new RoomGiftDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_list, container, false);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list = LiveHelper.getInstance().getGiftLists();
        Log.e("main","````````````````````"+list.size());
        if (list != null) {
            initData();
        } else {
            // down load gift list

        }
//        setupViewPager();
//        tabLayout.setupWithViewPager(viewPager);
    }
    public static void setOnClickListener(View.OnClickListener listener){
             mOnClickListener = listener;
        }

    private void initData() {
        mGridLayoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new LivegiftAdapter(list, getContext());
        rvGift.setLayoutManager(mGridLayoutManager);
        rvGift.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class LivegiftAdapter extends RecyclerView.Adapter<LivegiftAdapter.GiftViewHolder> {
        Context context;
        List<Gift> mlist;

        public LivegiftAdapter(List<Gift> list, Context context) {
            this.context = context;
            this.mlist = list;
        }

        @Override
        public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GiftViewHolder holder = new GiftViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gift, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(GiftViewHolder holder, int position) {
            Gift gift = mlist.get(position);
            if (gift != null) {
                holder.tvGiftName.setText(gift.getGname());
                holder.tvGiftPrice.setText("￥" + gift.getGprice());
                EaseUserUtils.setGiftAvatar(context, gift.getGurl(), holder.ivGiftThumb);
                holder.itemView.setTag(gift.getId());
                holder.itemView.setOnClickListener(mOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class GiftViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ivGiftThumb)
            ImageView ivGiftThumb;
            @BindView(R.id.tvGiftName)
            TextView tvGiftName;
            @BindView(R.id.tvGiftPrice)
            TextView tvGiftPrice;
            @BindView(R.id.layout_gift)
            LinearLayout layoutGift;

            GiftViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


}