package tw.org.cmtseng.mingauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button

        findViewById(R.id.btn_TangCode).setOnClickListener(this);
        findViewById(R.id.btn_Anonymous).setOnClickListener(this);
        findViewById(R.id.btn_Chooser).setOnClickListener(this);
        findViewById(R.id.btn_CustomAuth).setOnClickListener(this);
        findViewById(R.id.btn_EmailPassword).setOnClickListener(this);
        findViewById(R.id.btn_FacebookLogin).setOnClickListener(this);
        findViewById(R.id.btn_FirebaseUI).setOnClickListener(this);
        findViewById(R.id.btn_GoogleSignIn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
switch(view.getId()) {
    case  R.id.btn_TangCode:
        startActivity(new Intent(this, EmailPasswordActivity.class));
        break;
    case  R.id.btn_EmailPassword:
        startActivity(new Intent(this, EmailPasswordActivity.class));
        return;
}
    }
}
