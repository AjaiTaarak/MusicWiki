package taarak.greedygames.musicwiki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vpaliy.last_fm_api.auth.LastFmAuth;
import com.vpaliy.last_fm_api.model.Session;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Login extends AppCompatActivity {

    EditText edt_phone;
    TextView txt_mailinvalid,txt_signup;
    Button btn_login;
    String phone;
    Session session;
    SharedPreferences sp;
    ProgressBar prg_login;
    SharedPreferences.Editor edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initviews();
        sp=getApplicationContext().getSharedPreferences("Key", Login.MODE_PRIVATE);
        edt=sp.edit();
        LastFmAuth.create(AppConstants.APIKEY,AppConstants.SECRET_KEY)
                .auth(getApplicationContext(),"Ajaitaarak","sollamaten1!")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    session=response.result;

                });
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                prg_login.setVisibility(View.VISIBLE);
                phone=edt_phone.getText().toString();
                try {
                    if (isNullOrEmpty(phone) && phone.length()==10) {
                        edt.putString("log_phone",phone);

                        edt.apply();
                        edt.commit();
                        startActivity(new Intent(Login.this,OTP_validation.class));
                        overridePendingTransition(0,0);
                    }
                    else {
                        txt_mailinvalid.setVisibility(View.VISIBLE);
                        prg_login.setVisibility(View.GONE);
                        edt_phone.setText("");
                    }
                }catch (Exception e)
                {

                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkconnected())

            startActivity(new Intent(getApplicationContext(),NoInternet.class));
        overridePendingTransition(0,0);
    }
    private boolean isNetworkconnected(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isNetworkconnected()) {
            startActivity(new Intent(getApplicationContext(), NoInternet.class));
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onBackPressed() {

    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return true;
        return false;
    }
    public void initviews(){

        edt_phone =findViewById(R.id.txt_login_phone);

        prg_login=findViewById(R.id.spn_login);
        txt_mailinvalid=findViewById(R.id.txt_login_mailinvalidd);
        btn_login=findViewById(R.id.btn_login_submit);
    }

}
