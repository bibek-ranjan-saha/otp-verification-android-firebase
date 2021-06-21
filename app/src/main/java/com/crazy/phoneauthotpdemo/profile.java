package com.crazy.phoneauthotpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class profile extends AppCompatActivity {

    TextView t1,t2,t3;
    Button b;
    FirebaseAuth auth;
    FirebaseFirestore db;
    CollectionReference reference;
    String langu,typu,phonu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences preferences = getSharedPreferences("my_data",MODE_PRIVATE);

        langu = preferences.getString("language","english");
        phonu = preferences.getString("phone","not available");
        typu = preferences.getString("type","not yet selected");

        auth = FirebaseAuth.getInstance();

        t1 = findViewById(R.id.textView4);
        t2 = findViewById(R.id.textView5);
        t3 = findViewById(R.id.textView6);
        b = findViewById(R.id.button);
        db = FirebaseFirestore.getInstance();

        t1.setText("Language is :"+langu);
        t2.setText("Phone number is :"+phonu);
        t3.setText("profile type is :"+typu);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finishAffinity();
            }
        });

        reference = db.collection("User");
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         
                    }
                });
    }
}