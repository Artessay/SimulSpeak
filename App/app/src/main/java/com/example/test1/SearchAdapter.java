package com.example.test1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter {
    @NonNull
    private Context mContext;
    private OnItemClickListener mListener;
    //private List<String> list;
    private List<Drawable> images=new ArrayList<>();
    private List<String> contents=new ArrayList<>();
    private List<String> times=new ArrayList<>();
    public void set_items(List<Drawable> images,List<String> contents,List<String> times) {
        this.images=images;
        this.contents=contents;
        this.times=times;
        //一定要记得加，否则视图不会更新！！！
        notifyDataSetChanged();
    }
    public SearchAdapter(Context context , OnItemClickListener listener,List<Drawable> images,List<String> contents,List<String> times){
        this.mContext = context;
        this.mListener = listener;
        this.images=images;
        this.contents=contents;
        this.times=times;
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.show_video, parent, false));
    }

    @Override
    //通过getItemViewType的返回值来选择具体的item显示
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TextView content_view =((LinearViewHolder)holder).itemView.findViewById(R.id.content);
        content_view.setText(contents.get(position));
        TextView time_view =((LinearViewHolder)holder).itemView.findViewById(R.id.time);
        time_view.setText(times.get(position));

        ImageView imageview = ((LinearViewHolder)holder).itemView.findViewById(R.id.image);
        imageview.setImageDrawable(images.get(position));
        imageview.setScaleType(ImageView.ScaleType.FIT_XY);
        imageview.setAdjustViewBounds(false);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
               mListener.onClick(position);
            }
       });
   }

    //去控制viewType的方法，根据位置的奇偶性来区分
    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0){
            return 0;//偶数
        }else{
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public LinearViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.content);
        }
    }


    //接口
    public interface  OnItemClickListener{
        void onClick(int pos);
    }
}
