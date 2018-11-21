package com.example.mysterious.multitypeadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * 封装的itemview
 */
public abstract class BaseWrapperMultiTypeItemView<T, VH extends ViewHolder> extends ItemViewBinder<T, VH> {


    private Context mContext;
    public LayoutInflater layoutInflater;

    public BaseWrapperMultiTypeItemView(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        BaseWrapperMultiTypeViewHolder viewHolder = BaseWrapperMultiTypeViewHolder.createViewHolder(mContext, parent, getViewHolderResId());
        return (VH) viewHolder;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract int getViewHolderResId();

    @Override
    protected void onBindViewHolder(@NonNull VH holder, @NonNull T item, @NonNull List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            super.onBindViewHolder(holder, item, payloads);
        } else {
            //RecyclerView的局部刷新机制封装
            if (payloads != null && payloads.size() > 0) {
                for (int i = 0; i < payloads.size(); i++) {
                    Object key = payloads.get(i);
                    if (key != null) {
                        onNotifyItemContentChange(key, item, holder);
                    }
                }
            }
        }

    }

    //局部刷新item
    public void onNotifyItemContentChange(Object key, @NonNull T item, @NonNull VH holder) {

    }
}
