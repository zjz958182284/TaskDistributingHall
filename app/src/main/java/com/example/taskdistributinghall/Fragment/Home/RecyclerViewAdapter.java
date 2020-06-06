package com.example.taskdistributinghall.Fragment.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    @NonNull
    // 儲存要顯示的資料。
    private List<Task> tasks;
    private LayoutInflater inflater;
    private ItemClick itemClick;
    public List<Task> getTasks(){
        return tasks;
    }
    public void setTasks(List<Task> tasks){
         this.tasks=tasks;
    }
    public RecyclerViewAdapter(Context context,@NotNull List<Task> tasks) {
        this.tasks=tasks;
        this.inflater=LayoutInflater.from(context);

    }



   //    @Override
   //    public void onClick(View view) {
   //      Context context= view.getContext();
   //      Intent intent =new Intent(context, Mission_detail_page.class);
   //      context.startActivity(intent);
   //    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //为每一行填充数据
        View v= inflater.inflate(R.layout.home_page_mission_view,parent,false);
        return new ViewHolder(v);
    }


    // RecyclerView會呼叫這個方法，我們必須把項目資料填入ViewHolder物件。
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 把資料設定給 holder。
        holder.imgView.setImageBitmap(tasks.get(position).taskPhoto);
        holder.timeView.setText((tasks.get(position).date).substring(0,16));
        holder.titleView.setText(tasks.get(position).title);
        holder.detailView.setText(tasks.get(position).content);

        int rewards=tasks.get(position).rewards;
        String reward=String.valueOf(rewards);
        holder.bountyView.setText(reward+"元");
        String type=tasks.get(position).type;
        if(type.equals("errand"))
            type="跑腿";
        else if(type.equals("study"))
            type="学习";
        else type="合作";
        holder.typeView.setText(type);
        View v=holder.itemView;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null)
                itemClick.onItemClick(tasks.get(position));
            }
        });

    }



    // RecyclerView會呼叫這個方法，我們要傳回總共有幾個項目。
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }


    // ViewHolder 是把項目中所有的 View 物件包起來。
    // 它在 onCreateViewHolder() 中使用。
   public  class ViewHolder extends  RecyclerView.ViewHolder {
       private ImageView imgView;
       private TextView titleView;
       private   TextView detailView;
       private TextView bountyView;
       private TextView timeView;
       private TextView typeView;
       private  TextView idView;


       //包含有每一行的View
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView=itemView.findViewById(R.id.mission_view);
            titleView =itemView.findViewById(R.id.mission_title);
            detailView=itemView.findViewById(R.id.description_text);
            bountyView=itemView.findViewById(R.id.bounty);
            timeView=itemView.findViewById(R.id.date);
            typeView=itemView.findViewById(R.id.mission_type);
        }

}


    public  static  interface  ItemClick{
        void onItemClick(Task task);
    }



}
