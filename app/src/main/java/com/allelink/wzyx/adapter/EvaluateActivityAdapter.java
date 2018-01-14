package com.allelink.wzyx.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.GlideApp;
import com.allelink.wzyx.model.EvaluateListItem;
import com.allelink.wzyx.net.RestConstants;

import java.util.List;


/**
 * Created by sushijun on 2018/1/4.
 */

public class EvaluateActivityAdapter extends RecyclerView.Adapter<EvaluateActivityAdapter.viewHolder> {
    private static final String TAG = "EvaluateActivityAdapter";

    private List<EvaluateListItem> evaluateList;
    Context mContext;
    class viewHolder extends RecyclerView.ViewHolder{
        View EvaluateView;
        AppCompatImageView userImage;
        TextView user_name;
        RatingBar evaluateLevel;
        TextView evaluateContent;
        TextView evaluateDate;

        public viewHolder(View view) {
            super(view);
            EvaluateView=view;
            userImage=view.findViewById(R.id.iv_evaluate_user_image);
            user_name=view.findViewById(R.id.tv_evaluate_user_name);
            evaluateLevel=view.findViewById(R.id.rb_evaluate_level);
            evaluateContent=view.findViewById(R.id.tv_evaluate_content);
            evaluateDate=view.findViewById(R.id.tv_evaluate_date);
        }
    }

    public EvaluateActivityAdapter(List<EvaluateListItem> mEvaluateList){
        this.evaluateList=mEvaluateList;
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_evaluate,parent,false);
        final viewHolder holder=new viewHolder(view);
        mContext=view.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
         EvaluateListItem evaluate = evaluateList.get(position);
        if(evaluate.getPhoto() == null){
            evaluate.setPhoto("");
        }
        //holder.userImage.setImageURI(Uri.parse("http://101.132.191.9:8083/pic/"+evaluate.getPhoto()));
        GlideApp.with(mContext)
                .load(RestConstants.IMAGE_ROOT_URL+"pic/"+evaluate.getPhoto().replace("\\","/"))
                .placeholder(R.drawable.activity_default_pic)
                .error(R.drawable.activity_default_pic)
                .into(holder.userImage);
        holder.user_name.setText(evaluate.getNickname());
        holder.evaluateLevel.setRating(evaluate.getEvaluateLevel());
        holder.evaluateContent.setText(evaluate.getEvaluateContent());
        holder.evaluateDate.setText(evaluate.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return evaluateList.size();
    }



}
