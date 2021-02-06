package taarak.greedygames.musicwiki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

public class Splash_screen extends AppCompatActivity {

    Button btn_login;
    LinearLayout lay_splash;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);

        setContentView(R.layout.activity_splashscreen);

        btn_login =findViewById(R.id.btn_splash_login);
        lay_splash=findViewById(R.id.lay_splash);
        lay_splash.setVisibility(View.GONE);
        sp=getApplicationContext().getSharedPreferences("Key",Splash_screen.MODE_PRIVATE);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        new CountDownTimer(1000, 500) {

            @Override
            public void onTick(long millsUntilFinished) {

            }
            @Override
            public void onFinish() {
                FirebaseUser use= FirebaseAuth.getInstance().getCurrentUser();
                if(isNetworkconnected() && use!=null) {
                    Intent log = new Intent(Splash_screen.this, HomePage.class);
                    startActivity(log);

                }else if(use==null){
                    lay_splash.setVisibility(View.VISIBLE);
                }else{
                    Intent log = new Intent(Splash_screen.this, NoInternet.class);
                    startActivity(log);
                }
            }
        }.start();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Splash_screen.this,Login.class));
            }
        });


    }
    private boolean isNetworkconnected(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null;
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }
}
