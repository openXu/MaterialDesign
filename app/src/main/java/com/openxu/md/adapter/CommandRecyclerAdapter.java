package com.openxu.md.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * autour : openXu
 * date : 2017/9/7 19:05
 * className : CommandRecyclerAdapter
 * version : 1.0
 * description : 通用的CommandRecyclerAdapter
 */
public abstract class CommandRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected boolean longClickAble = false;   //是否支持长点击

    public void setLongClickAble(boolean longClickAble) {
        this.longClickAble = longClickAble;
    }

    public CommandRecyclerAdapter(Context context, int layoutId, List<T> datas) {
        mDatas = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        if (datas != null)
            mDatas.addAll(datas);

        //数据集中的项是否具有唯一标识，设置为true避免刷新时老数据闪烁，需要重写getItemId()
        setHasStableIds(true);
    }

    public void setData(List<T> datas) {
        mDatas.clear();
        if (datas != null)
            mDatas.addAll(datas);
        notifyDataSetChanged();
    }
    /**添加数据，局部刷新，一般用于分页加载*/
    public void addData(List<T> datas) {
        int startPoint = mDatas.size();
        if (datas != null)
            mDatas.addAll(datas);
        //需要配合 setHasStableIds(true),避免闪烁
//        notifyDataSetChanged();
        //局部刷新
        notifyItemRangeChanged(startPoint, datas.size());
    }
    public void addData(T data) {
        int startPoint = mDatas.size();
        if (data != null)
            mDatas.add(data);
        notifyItemRangeChanged(startPoint, 1);
    }
    public void removeData(T data) {
        if(mDatas.contains(data)) {
            mDatas.remove(data);
            notifyDataSetChanged();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return ViewHolder.get(mContext, parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.getBinding().setVariable(BR.data, getItem(position));
        convert(holder, getItem(position), position);
        holder.setOnClickListener(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(getItem(position), position);
            }
        });
        if(longClickAble){
            holder.setOnLongClickListener(-1, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClick(getItem(position), position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null==mDatas?0:mDatas.size();
    }
    @Override
    public long getItemId(int position) {
        return null==getItem(position)?-1:getItem(position).toString().hashCode();
    }

    public List<T> getDatas() {
        return mDatas;
    }
    public T getItem(int position) {
        return null == mDatas||position>=mDatas.size()? null : mDatas.get(position);
    }
    /**
     * 重写此方法，将数据绑定到控件上
     * @param holder
     * @param t
     */
    public abstract void convert(ViewHolder holder, T t, int position) ;

    /***
     * item点击
     * @param data
     */
    public abstract void onItemClick(T data, int position);

    /***
     * item点击
     * @param data
     */
    public void onItemLongClick(T data, int position){}

}