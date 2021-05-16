package com.prasoonsoni.supervisedlearninginonlineeducation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;
import com.prasoonsoni.supervisedlearninginonlineeducation.adapters.MoodAdapter;
import com.prasoonsoni.supervisedlearninginonlineeducation.models.MoodModel;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ArrayList<MoodModel> moodList = new ArrayList<>();
    private LottieAnimationView loading;
    private Interpreter interpreter;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_PERMISSION_CODE = 100, STORAGE_PERMISSION_CODE = 101;
    String mCurrentPhotoPath;
    int size, class1, class2, class3, class4, class5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        loading = findViewById(R.id.loading);
        TextView nameText = findViewById(R.id.nameTextView);
        TextView schoolText = findViewById(R.id.schoolTextView);
        FloatingActionButton logout = findViewById(R.id.logout);

        if(mAuth.getCurrentUser() != null) {

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Setting up for first time use");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            logout.setOnClickListener(v -> {
                mAuth.signOut();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });

            CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                    .build();
            FirebaseModelDownloader.getInstance()
                    .getModel("DetectionModel", DownloadType.LOCAL_MODEL, conditions)
                    .addOnSuccessListener(model -> {
                        progress.dismiss();
                        File modelFile = model.getFile();
                        if (modelFile != null) {
                            interpreter = new Interpreter(modelFile);
                        }
                    }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());

        }

        RecyclerView moodRecyclerView = findViewById(R.id.moodRecyclerView);
        MoodAdapter moodAdapter = new MoodAdapter(moodList,this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,5);
        moodRecyclerView.setLayoutManager(layoutManager);
        moodRecyclerView.setAdapter(moodAdapter);

        loading.setVisibility(View.VISIBLE);
        moodRecyclerView.setVisibility(View.INVISIBLE);

        if (mAuth.getCurrentUser() != null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData").child(mAuth.getCurrentUser().getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Map<String, String> data = new HashMap<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        data.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                    }
                    nameText.setText(data.get("name"));
                    schoolText.setText(data.get("school"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    moodList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        MoodModel model = dataSnapshot.getValue(MoodModel.class);
                        moodList.add(model);
                    }
                    moodAdapter.notifyDataSetChanged();
                    if (moodList.isEmpty()){
                        loading.setVisibility(View.VISIBLE);
                        moodRecyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        loading.setVisibility(View.INVISIBLE);
                        moodRecyclerView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                    }
                    ImageView imageView = findViewById(R.id.imageView2);
                    imageView.setImageBitmap(rotatedBitmap);
                    processBitmap(rotatedBitmap);
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void processBitmap(Bitmap bitmap) {
        ByteBuffer input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int px = bitmap.getPixel(x, y);

                // Get channel values from the pixel value.
                int r = Color.red(px);
                int g = Color.green(px);
                int b = Color.blue(px);

                // Normalize channel values to [-1.0, 1.0]. This requirement depends
                // on the model. For example, some models might require values to be
                // normalized to the range [0.0, 1.0] instead.
                float rf = (r - 127) / 255.0f;
                float gf = (g - 127) / 255.0f;
                float bf = (b - 127) / 255.0f;

                input.putFloat(rf);
                input.putFloat(gf);
                input.putFloat(bf);
            }
        }

        int bufferSize = 1000 * 4 / Byte.SIZE;
        ByteBuffer output = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());

        interpreter.run(input, output);

        output.rewind();
        FloatBuffer mood = output.asFloatBuffer();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getResources().openRawResource(R.raw.labels)));

        float attentive, unattentive, confused, depressed, cheerful;
        try {
            String label = reader.readLine();
            attentive = mood.get(0);
            Log.i("HelloOutput", String.format("%s: %1.4f", label, mood.get(0)));
            label = reader.readLine();
            unattentive = mood.get(1);
            Log.i("HelloOutput", String.format("%s: %1.4f", label, mood.get(1)));
            label = reader.readLine();
            confused = mood.get(2);
            Log.i("HelloOutput", String.format("%s: %1.4f", label, mood.get(2)));
            label = reader.readLine();
            depressed = mood.get(3);
            Log.i("HelloOutput", String.format("%s: %1.4f", label, mood.get(3)));
            label = reader.readLine();
            cheerful = mood.get(4);
            Log.i("HelloOutput", String.format("%s: %1.4f", label, mood.get(4)));

            Map<Float, String> moods = new HashMap<>();
            moods.put(attentive, "Attentive");
            moods.put(unattentive, "Unattentive");
            moods.put(confused, "Confused");
            moods.put(depressed, "Depressed");
            moods.put(cheerful, "Cheerful");

            float currentMood = Math.max(attentive, Math.max(unattentive, Math.max(confused, Math.max(depressed, cheerful))));

            Map<String, Object> entry = new HashMap<>();
            entry.put("mood", moods.get(currentMood));
            LocalDateTime date = LocalDateTime.now();
            int startTime = date.getHour();
            entry.put("startTime", startTime + ":00");
            entry.put("endTime", (startTime + 1) + ":00");

            size = 0;
            DocumentReference reference = FirebaseFirestore.getInstance()
                    .collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document(Integer.toString(date.getDayOfMonth()));
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            size = documentSnapshot.getData().size();
                        }else{
                            size = 0;
                        }
                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put(Integer.toString(size + 1), entry);
                        reference.set(hashMap, SetOptions.merge());
                        size++;

                        class1 = 0; class2 = 0; class3 = 0; class4 = 0; class5 = 0;
                        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        for (int i = 1; i <= documentSnapshot.getData().size(); i++) {
                                            HashMap<String, String> hashMap = (HashMap<String, String>) documentSnapshot.getData().get(Integer.toString(i));
                                            if (hashMap.get("mood").equals("Attentive")){
                                                class1++;
                                            }else if (hashMap.get("mood").equals("Unattentive")){
                                                class2++;
                                            }else if (hashMap.get("mood").equals("Confused")){
                                                class3++;
                                            }else if (hashMap.get("mood").equals("Depressed")){
                                                class4++;
                                            }else if (hashMap.get("mood").equals("Cheerful")){
                                                class5++;
                                            }
                                        }
                                        Log.i("HelloOutputAtt", Integer.toString(class1));
                                        Log.i("HelloOutputUnAtt", Integer.toString(class2));
                                        Log.i("HelloOutputConf", Integer.toString(class3));
                                        Log.i("HelloOutputDep", Integer.toString(class4));
                                        Log.i("HelloOutputCher", Integer.toString(class5));

                                        Log.i("HelloOutputOver", getMax(class1, class2, class3, class4, class5));

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("Overall", getMax(class1, class2, class3, class4, class5));
                                        map.put("date", Integer.toString(date.getDayOfMonth()));
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                                .child(mAuth.getCurrentUser().getUid()).child(Integer.toString(date.getDayOfMonth()));
                                        databaseReference.setValue(map);
                                    }
                                }

                            }
                        });

                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void dispatchTakePictureIntent() {

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.blunderbois.sloe.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    //takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                    //takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                    //takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public String getMax(int a, int b, int c, int d, int e){
        if (a > b) {
            if (a > c) {
                if (a > d) {
                    if (a >= e)
                        return "Attentive";
                    else
                        return "Cheerful";
                } else{
                    if (d >= e)
                        return "Depressed";
                    else
                        return "Cheerful";
                }
            } else {
                if (c > d){
                    if (c > e)
                        return "Confused";
                    else
                        return "Cheerful";
                }
                else{
                    if (d >= e)
                        return "Depressed";
                    else
                        return "Cheerful";
                }

            }
        }else {
            if (b > c) {
                if (b > d) {
                    if (b >= e)
                        return "Unattentive";
                    else
                        return "Cheerful";
                } else{
                    if (d >= e)
                        return "Depressed";
                    else
                        return "Cheerful";
                }
            } else {
                if (c > d) {
                    if (c >= e)
                        return "Confused";
                    else
                        return "Cheerful";
                } else
                if (d >= e)
                    return "Depressed";
                else
                    return "Cheerful";
            }
        }
    }

}