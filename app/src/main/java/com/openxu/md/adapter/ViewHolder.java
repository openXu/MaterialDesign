package com.openxu.md.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


/**
 * autour : openXu
 * date : 2017/9/7 19:04
 * className : ViewHolder
 * version : 1.0
 * description : 通用的ViewHolder
 */
public class ViewHolder  extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private ViewDataBinding binding;
    private Context mContext;

    public ViewHolder(Context context, ViewDataBinding binding, ViewGroup parent) {
        super(binding.getRoot());
        this.binding = binding;
        this.mContext = context;
        this.mViews = new SparseArray<>();
    }

    public static ViewHolder get(Context context, ViewGroup parent, int layoutId) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                layoutId,parent, false);
        return new ViewHolder(context, binding, parent);
    }

    /**
     * 通过viewId获取控件
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = binding.getRoot().findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ViewDataBinding getBinding(){
        return binding;
    }
    public ViewHolder setOnClickListener(int viewId,  View.OnClickListener listener) {
        if(viewId==-1){
            binding.getRoot().setOnClickListener(listener);
        }else{
            View view = getView(viewId);
            view.setOnClickListener(listener);
        }
        return this;
    }
    public ViewHolder setOnLongClickListener(int viewId,  View.OnLongClickListener listener) {
        if(viewId==-1){
            binding.getRoot().setOnLongClickListener(listener);
        }else{
            View view = getView(viewId);
            view.setOnLongClickListener(listener);
        }
        return this;
    }
}