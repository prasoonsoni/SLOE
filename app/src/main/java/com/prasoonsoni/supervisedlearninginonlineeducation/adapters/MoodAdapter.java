package com.prasoonsoni.supervisedlearninginonlineeducation.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prasoonsoni.supervisedlearninginonlineeducation.DataActivity;
import com.prasoonsoni.supervisedlearninginonlineeducation.R;
import com.prasoonsoni.supervisedlearninginonlineeducation.WebViewActivity;
import com.prasoonsoni.supervisedlearninginonlineeducation.models.MoodModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MyViewHolder> {
    ArrayList<MoodModel> list;
    Context context;

    public MoodAdapter(ArrayList<MoodModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_mood, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MoodModel model = list.get(position);
        holder.date.setText(model.getDate());
        String emoji = null;
        String color = null;
        if(model.getOverall().equals("Attentive")){
          holder.moodCard.setCardBackgroundColor(Color.parseColor("#A1ECBF"));
          color = "#A1ECBF";
          emoji = "\uD83D\uDE42";
        } else if (model.getOverall().equals("Depressed")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#ff726f"));
            emoji = "\uD83D\uDE1E";
            color = "#ff726f";
        } else if(model.getOverall().equals("Cheerful")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#fea82f"));
            emoji = "\uD83D\uDE00";
            color = "#fea82f";
        } else if(model.getOverall().equals("Unattentive")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#958ce8"));
            emoji = "\uD83D\uDE44";
            color = "#958ce8";
        } else if(model.getOverall().equals("Confused")){
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#fdd5b0"));
            emoji = "\uD83D\uDE15";
            color = "#fdd5b0";
        } else {
            holder.moodCard.setCardBackgroundColor(Color.parseColor("#dbdbe5"));
        }

        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        if(model.getDate().equals(Integer.toString(currentDay))){
            holder.forColor.setBackgroundResource(R.drawable.border);
        }

        String finalEmoji = emoji;
        String finalColor = color;

        holder.moodCard.setOnClickListener(v -> {
            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document(Integer.toString(position + 1));
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Intent intent = new Intent(context, DataActivity.class);
                        intent.putExtra("mood", model.getOverall());
                        intent.putExtra("position", position);
                        intent.putExtra("emoji", finalEmoji);
                        intent.putExtra("color", finalColor);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "No time-wise data exists for the selected day", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

        holder.moodCard.setOnLongClickListener(v -> {
            context.startActivity(new Intent(context, WebViewActivity.class));
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        CardView moodCard;
        ConstraintLayout forColor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            moodCard = itemView.findViewById(R.id.moodCard);
            forColor = itemView.findViewById(R.id.forColor);

        }
    }
}
