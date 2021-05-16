package com.prasoonsoni.supervisedlearninginonlineeducation;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prasoonsoni.supervisedlearninginonlineeducation.adapters.ClassAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static TextView moodText, moodEmoji;
    public static ArrayList<Map<String, String>> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        RecyclerView classRecyclerView = findViewById(R.id.classRecyclerView);
        ClassAdapter classAdapter = new ClassAdapter(this, classList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        classRecyclerView.setLayoutManager(layoutManager);
        classRecyclerView.setAdapter(classAdapter);

        toolbar = findViewById(R.id.toolbar);
        moodText = findViewById(R.id.moodText);
        moodEmoji = findViewById(R.id.moodEmoji);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        Bundle bd = getIntent().getExtras();
        moodText.setText(bd.getString("mood"));
        moodText.setTextColor(Color.parseColor(bd.getString("color")));
        moodEmoji.setText(bd.getString("emoji"));
        int position = bd.getInt("position");

        classList.clear();
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document(Integer.toString(position + 1));
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                    for (int i = 1; i <= documentSnapshot.getData().size(); i++){
                        HashMap<String, String> hashMap = (HashMap<String, String>) documentSnapshot.getData().get(Integer.toString(i));
                        classList.add(hashMap);
                    }
                    classAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}