package taarak.greedygames.musicwiki;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NoInternet extends AppCompatActivity {

    Button btn_retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        btn_retry=findViewById(R.id.btn_nointernetretry);

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkconnected()) {
                    onBackPressed();

                }
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
    }
}
