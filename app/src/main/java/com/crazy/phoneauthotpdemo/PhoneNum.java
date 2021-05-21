package com.crazy.phoneauthotpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class PhoneNum extends AppCompatActivity {
    Button b12;
    ImageView close;
    EditText phnum;
    String num;
    CountryCodePicker countryCodePicker;
    FirebaseFirestore db;
    String phonu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_num);

        SharedPreferences preferences = getSharedPreferences("my_data",MODE_PRIVATE);

         phonu = preferences.getString("phone","not available");

        b12 = findViewById(R.id.button);
        close  = findViewById(R.id.close);
        phnum = findViewById(R.id.editText);
        countryCodePicker = findViewById(R.id.ccp);
        db = FirebaseFirestore.getInstance();

        countryCodePicker.registerCarrierNumberEditText(phnum);

        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    num = "+"+countryCodePicker.getFullNumber();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("phone",num);
                    editor.apply();
//                    AppD appD = new AppD();
//                    appD.setPhonenum(num);
                    Intent i = new Intent(PhoneNum.this,manageotp.class);
                    i.putExtra("phonenumber",num);
                    startActivity(i);
            }
        });

        close.setOnClickListener(v -> onBackPressed());
    }
}