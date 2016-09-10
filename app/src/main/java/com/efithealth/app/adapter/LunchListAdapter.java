package com.efithealth.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.efithealth.R;
import com.efithealth.app.javabean.BeanGoodsList;
import com.efithealth.app.maxiaobu.widget.TouchHighlightImageButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/17.
 */
public class LunchListAdapter extends RecyclerView.Adapter {
    public interface OnImageItemClickListener {
        public void onItemClick(View view,String url);
    }
    public interface OnItemClickListener {
        public void onItemClick(View view,String what);
    }
    public OnImageItemClickListener mImageListener;
    public OnItemClickListener mListener;
    public void setOnImageItemClickListener(OnImageItemClickListener listener){
        mImageListener =listener;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }
    private Context mContext;
    private List<BeanGoodsList.ListBean> mData;

    public LunchListAdapter(Context context, List<BeanGoodsList.ListBean> mData) {
        mContext = context;
        this.mData=mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_lunch_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder= (MyViewHolder) holder;
        final BeanGoodsList.ListBean listBean = mData.get(position);
        String compodescr = listBean.getCompodescr();
        String[] split = compodescr.split(",");//热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg
        viewHolder.mTvEnergy.setText(split[0]);
        viewHolder.mTvProtein.setText(split[1].trim());

        viewHolder.mTvPrice.setText(listBean.getMerprice()+"元");
        viewHolder.mTvTitle.setText(listBean.getMername());
        viewHolder.mTvFoodType.setText(listBean.getMerdescr());
        final String imgsfilename = listBean.getImgsfilename();
        Glide.with(mContext).load(listBean.getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvGoods);
        viewHolder.mIvGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImageListener !=null){
                    mImageListener.onItemClick(view,imgsfilename);
                }
            }
        });
        viewHolder.mMrlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null){
                    mListener.onItemClick(viewHolder.mIvGoods,"");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_goods)
        TouchHighlightImageButton mIvGoods;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.tv_food_type)
        TextView mTvFoodType;
        @Bind(R.id.tv_energy)
        TextView mTvEnergy;
        @Bind(R.id.tv_protein)
        TextView mTvProtein;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.mrlayout)
        MaterialRippleLayout mMrlayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
