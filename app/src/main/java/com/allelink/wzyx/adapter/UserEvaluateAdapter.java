package com.allelink.wzyx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.model.EvaluateListItem;
import com.allelink.wzyx.model.UserEvaluateItem;

import java.util.List;

/**
 * Created by sushijun on 2018/1/5.
 */

public class UserEvaluateAdapter extends RecyclerView.Adapter<UserEvaluateAdapter.viewHolder> {

    private List<UserEvaluateItem> userEvaluateList;

    class viewHolder extends RecyclerView.ViewHolder{
        View EvaluateView;
        //ImageView userImage;
        TextView activity_name;
        RatingBar evaluateLevel;
        TextView evaluateContent;
        TextView evaluateDate;

        public viewHolder(View view) {
            super(view);
            EvaluateView=view;
           // userImage=view.findViewById(R.id.iv_evaluate_user_image);
            activity_name=view.findViewById(R.id.tv_evaluate_activity_name);
            evaluateLevel=view.findViewById(R.id.rb_evaluate_activity_level);
            evaluateContent=view.findViewById(R.id.tv_evaluate_activity_content);
            evaluateDate=view.findViewById(R.id.tv_evaluate_activity_date);
        }
    }

    public UserEvaluateAdapter(List<UserEvaluateItem> mEvaluateList){
        this.userEvaluateList=mEvaluateList;
    }
    @Override
    public UserEvaluateAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_evaluate_list,parent,false);
        final UserEvaluateAdapter.viewHolder holder=new UserEvaluateAdapter.viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserEvaluateAdapter.viewHolder holder, int position) {
        UserEvaluateItem evaluate=userEvaluateList.get(position);
        // holder.userImage.setImageURI(Uri.parse(evaluate.getPhoto()));
        holder.activity_name.setText(evaluate.getActivityName());
        holder.evaluateLevel.setRating(evaluate.getEvaluateLevel());
        holder.evaluateContent.setText(evaluate.getEvaluateContent());
        holder.evaluateDate.setText(evaluate.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return userEvaluateList.size();
    }
}
