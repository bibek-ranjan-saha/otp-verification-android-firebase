package com.crazy.phoneauthotpdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.androidnetworking.AndroidNetworking;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class manageotp extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    OtpView o1;
    Button b2;
//    ImageView back_btn;
    FirebaseAuth mAuth;
    String otpnum;
    String verificationcode;
    String apikey;
    String phone;
    TextView tv2,t;
    GoogleApiClient googleApiClient;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);

        //all initializations
        mAuth = FirebaseAuth.getInstance();
        tv2 = findViewById(R.id.textView2);
        t = findViewById(R.id.text);

        db = FirebaseFirestore.getInstance();

        privatekeys privatekeys = new privatekeys();
        apikey = privatekeys.apikey;

        o1 = findViewById(R.id.otp1);

//        back_btn = findViewById(R.id.back);
        b2 = findViewById(R.id.button);

        //getting phone number from previous activity
        phone = getIntent().getStringExtra("phonenumber");
        tv2.setText(tv2.getText().toString()+phone);


        o1.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {
                otpnum = otp;
                b2.setEnabled(true);
            }
        });

        //calling send otp method on startup of activity
        SendVerificationToUser(phone);

        //recaptcha testing
        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
                == ConnectionResult.SUCCESS) {
            // The SafetyNet Attestation API is available.
            Toast.makeText(getApplicationContext(),"SafetyNet Attestation API Successful",Toast.LENGTH_LONG).show();
        } else {
            // Prompt user to update Google Play services.
            Toast.makeText(getApplicationContext(),"Please Update Google Play services.",Toast.LENGTH_LONG).show();
        }
        AndroidNetworking.initialize(this);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(manageotp.this)
                .build();
        googleApiClient.connect();

        SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,apikey)
                .setResultCallback(recaptchaTokenResult -> {
                    Status status  = recaptchaTokenResult.getStatus();
                    if (status != null && status.isSuccess()){
                        Toast.makeText(getApplicationContext(),"recaptcha verified",Toast.LENGTH_LONG).show();
                    }
                });

//        back_btn.setOnClickListener(v -> onBackPressed());

        b2.setOnClickListener(v -> {
            verifyCode(otpnum);
        });

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendVerificationToUser(phone);
            }
        });

    }

    //sending code to user
    private void SendVerificationToUser(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            b2.setEnabled(true);
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull  PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationcode  = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(manageotp.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String vc) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode,vc);
        signInTheUserByCrediential(credential);
    }

    private void signInTheUserByCrediential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(manageotp.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(manageotp.this,dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(manageotp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(manageotp.this,"connection suspended, try again", Toast.LENGTH_SHORT).show();
    }
}