package taarak.greedygames.musicwiki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OTP_validation extends AppCompatActivity {

    public static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Boolean code_sent=false;
    ProgressBar prg_otp;
    TextView txt_resend,txt_timer;
    EditText edt_otp;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    String phone;
    Button btn_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_validation);

        sp=getApplicationContext().getSharedPreferences("Key",OTP_validation.MODE_PRIVATE);
        edt=sp.edit();
        btn_otp=findViewById(R.id.btn_reg_otp1);
        txt_resend=findViewById(R.id.txt_otp_resend);
        txt_timer=findViewById(R.id.txt_otp_timer);
        prg_otp=findViewById(R.id.prg_otp);
        edt_otp=findViewById(R.id.edt_reg_otp1);


        phone = sp.getString("log_phone", null);
        Log.e("login","e"+phone);
        timerr();
        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg_otp.setVisibility(View.VISIBLE);
                resendVerificationCode(phone,mResendToken);
                timerr();
            }
        });
        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(code_sent)
                {
                    prg_otp.setVisibility(View.VISIBLE);
                    String code=edt_otp.getText().toString();
                    //verifyPhoneNumberWithCode(phone,code);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

                    signInWithPhoneAuthCredential(credential);
                }
                if (!validatePhoneNumber()) {
                    Snackbar.make(v,"Not a Valid Number", Snackbar.LENGTH_LONG).setAction("Action",null).show();

                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.e(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                code_sent=false;
                //startActivity(new Intent(Login.this,MainActivity.class));
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.e(TAG, "onVerificationFailed", e);

                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    Toast.makeText(OTP_validation.this,"Invalid Number", Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }


            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.e(TAG, "onCodeSent:" + verificationId);

                code_sent=true;
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };
        startPhoneNumberVerification("+91"+phone);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OTP_validation.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        Log.e("CREDE",credential.getSmsCode()+" ");
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            checkmail();

                        } else {

                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_LONG).show();

                            edt_otp.setText("");
                            prg_otp.setVisibility(View.GONE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {


                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "signInWithCredential:failure"+e.getLocalizedMessage() );
                Snackbar.make(getCurrentFocus(),"Invalid OTP",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                edt_otp.setText("");
                prg_otp.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkconnected())
            startActivity(new Intent(getApplicationContext(),NoInternet.class));
    }
    private boolean isNetworkconnected(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isNetworkconnected())
            startActivity(new Intent(getApplicationContext(),NoInternet.class));
    }
    public void checkmail(){
        FirebaseUser use= FirebaseAuth.getInstance().getCurrentUser();

        edt.putString("phone",use.getPhoneNumber());
        edt.putString("uid",use.getUid());
        edt.apply();
        edt.commit();
        startActivity(new Intent(OTP_validation.this,HomePage.class));

    }
    private boolean validatePhoneNumber() {
        String phoneNumber = phone;
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(OTP_validation.this,"Invalid Number", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    protected void OnSignedIn(String phone) {

        SharedPreferences p=getApplicationContext().getSharedPreferences("Key", OTP_validation.MODE_PRIVATE);
        SharedPreferences.Editor edt=p.edit();




        try {


            FirebaseUser use= FirebaseAuth.getInstance().getCurrentUser();
            edt.putString("phone",phone);
            edt.putString("uid",use.getUid());
            edt.apply();
            edt.commit();
        }
        catch (Exception e)
        {
            Log.e("err",e.getMessage());
        }


    }

    public void timerr(){
        txt_timer.setVisibility(View.VISIBLE);
        txt_resend.setVisibility(View.GONE);
        new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millsUntilFinished) {

                txt_timer.setText("0."+(millsUntilFinished/1000));

            }
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                txt_timer.setVisibility(View.GONE);
                txt_resend.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}
