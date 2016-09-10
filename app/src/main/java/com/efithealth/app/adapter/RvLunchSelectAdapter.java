package com.efithealth.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.efithealth.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/18.
 */
public class RvLunchSelectAdapter extends RecyclerView.Adapter  {
    public interface LunchSelectItemClickListener {
        public void onItemClick(View view,int postion);
    }

    public LunchSelectItemClickListener mListener;
    public void setLunchSelectItemClickListener(LunchSelectItemClickListener listener){
        mListener=listener;
    }

    private Context mContext;
    private int[] menuIcons;
    private String[] menuTitle;


    public RvLunchSelectAdapter(Context context,int[] menuIcons,String[] menuTitle) {
        mContext = context;
        this.menuIcons=menuIcons;
        this.menuTitle=menuTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_lunch_select, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MyViewHolder viewHolder= (MyViewHolder) holder;
        viewHolder.mTextView.setText(menuTitle[position]);
        viewHolder.mImageView.setImageResource(menuIcons[position]);
        viewHolder.mMrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null){
                    mListener.onItemClick(view,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuIcons.length;
    }

    static class MyViewHolder  extends RecyclerView.ViewHolder  {
        @Bind(R.id.image_view)
        ImageView mImageView;
        @Bind(R.id.text_view)
        TextView mTextView;
        @Bind(R.id.mr_layout)
        MaterialRippleLayout mMrLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
