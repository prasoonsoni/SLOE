package com.prasoonsoni.supervisedlearninginonlineeducation.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blunderbois.sloe.R;

import java.util.ArrayList;
import java.util.Map;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {
    Context context;
    ArrayList<Map<String, String>> list;

    public ClassAdapter(Context context, ArrayList<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_class, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.startTime.setText(list.get(position).get("startTime") + " hrs");
        holder.endTime.setText(list.get(position).get("endTime") + " hrs");
        holder.mood.setText(list.get(position).get("mood"));

        if(list.get(position).get("mood").equals("Attentive")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#A1ECBF"));
        } else if (list.get(position).get("mood").equals("Depressed")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#ff726f"));
        } else if(list.get(position).get("mood").equals("Cheerful")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#fea82f"));
        } else if(list.get(position).get("mood").equals("Unattentive")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#958ce8"));
        } else if(list.get(position).get("mood").equals("Confused")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#fdd5b0"));
        } else {
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#dbdbe5"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView startTime, endTime, mood;
        CardView moodCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            mood = itemView.findViewById(R.id.mood);
            moodCard = itemView.findViewById(R.id.classCard);
        }
    }
}
