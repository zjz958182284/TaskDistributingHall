package com.example.taskdistributinghall.Activity.AcceptorList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdistributinghall.Activity.MainPage.MainActivity;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Fragment.Home.RecyclerViewAdapter;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class AcceptorListAdapter extends RecyclerView.Adapter<AcceptorListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    @NotNull
    private List<User> users;
    private OnItemClickListener clickButtonListener;
    private OnItemClickListener clickItemListener;
    public void  setButtonClickListener(AcceptorListAdapter.OnItemClickListener itemClickListener){
        this.clickButtonListener=itemClickListener;
    }

    public void  setClickItemListener(AcceptorListAdapter.OnItemClickListener itemClickListener){
        this.clickItemListener=itemClickListener;
    }

    public AcceptorListAdapter(Context context, @NotNull List<User> users,int taskID) {
        this.users=users;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.accept_people_list_view,parent,false);


        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.imgView.setImageBitmap(user.headPortrait);
        holder.completeView.setText("任务完成数量:" + user.completedTask);
        holder.acceptedView.setText("任务接受总量:" + user.acceptedTask);
        holder.nameView.setText(user.name);
        if( user.acceptedTask==0) holder.completeRateView.setText("还未接受过任何任务");
        else{
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rate=  df.format((double)user.completedTask/user.acceptedTask*100);
            holder.completeRateView.setText("任务完成率:"+rate+"%");
        }
        View v = holder.itemView;


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clickItemListener.OnItemClick(user);
            }
        });

        (v.findViewById(R.id.accept_people_confirm_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButtonListener.OnItemClick(users.get(position));
            }
        });
    }




    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolder extends  RecyclerView.ViewHolder {

        private ImageView imgView;
        private TextView nameView;
        private   TextView completeView;
        private TextView acceptedView;
        private TextView completeRateView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            imgView=itemView.findViewById(R.id.accept_people_head_image);
            nameView=itemView.findViewById(R.id.accept_people_name);
            completeRateView=itemView.findViewById(R.id.complete_rate);
            acceptedView=itemView.findViewById(R.id.accepted_task_number);
            completeView=itemView.findViewById(R.id.complete_number);


        }
    }

    public interface OnItemClickListener{
        public void OnItemClick(User user);
    }
}
