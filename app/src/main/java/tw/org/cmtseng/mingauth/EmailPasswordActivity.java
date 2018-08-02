package tw.org.cmtseng.mingauth;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    public ProgressDialog mProgressDialog;
    private static final String TAG ="vanessa" ;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);
        //Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.field_email); mEmailField.setText("m0921887912@gmail.com");
        mPasswordField = findViewById(R.id.field_password);mPasswordField.setText("p@ssw0rdm");
        //Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);
        //initialize_auth
        mAuth = FirebaseAuth.getInstance();
//        Log.d(TAG, "onCreate+mAuth 35: "+mAuth.toString());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case  R.id.email_create_account_button:
                Log.d(TAG, "onClick: email_create_account_button");
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case  R.id.email_sign_in_button:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case  R.id.sign_out_button:
                Log.d(TAG, "onClick: sign_out_button");
                signOut();
                break;
            case  R.id.verify_email_button:
                sendEmailVerification();
                Log.d(TAG, "onClick: verify_email_button");
                break;
//            case  R.id.email_create_account_button:
//                break;
        }

    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);
        //Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // [START_EXCLUDE]
                findViewById(R.id.verify_email_button).setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(EmailPasswordActivity.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(EmailPasswordActivity.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }

                // [END_EXCLUDE]
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
//        showProgressDialog();  顯示進度提示圖
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                }else{
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                }
                // [START_EXCLUDE]
//                hideProgressDialog();  關閉進度提示圖
                // [END_EXCLUDE]
            }
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:"+ email );
        if (!validateForm()) {
            Log.d(TAG, "signIn-validateForm: 65");
            return;
        }
//        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: 80 signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    Log.w(TAG, "signInWithEmail:failure ", task.getException() );
                }
                // [START_EXCLUDE]
                if (!task.isSuccessful()) {
                    mStatusTextView.setText(R.string.auth_failed);
                }
//                hideProgressDialog();
                // [END_EXCLUDE]
            }
        });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {
//        ideProgressDialog();
        if (user == null) {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }else{
            mStatusTextView.setText(
                    getString(
                            R.string.emailpassword_status_fmt
                    , user.getEmail()
                    , user.isEmailVerified()
            )
            );
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            Log.d(TAG, "getEmail: "+  user.getEmail().toString());
            Log.d(TAG, "getUid  :" +  user.getUid().toString());

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        }
    }


    private boolean validateForm() {
        boolean valid = true;

        String email =mEmailField.getText().toString();
//        Log.d(TAG, "validateForm: 73" + email );
        if (TextUtils.isEmpty(email) ) {
            mEmailField.setError("Required.");
            valid = false;
        }else{
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password) ) {
            mPasswordField.setError("密碼不可以為空值.");
            valid = false;
        }
        return valid;
    }
}
