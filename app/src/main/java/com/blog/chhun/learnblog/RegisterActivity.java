package com.blog.chhun.learnblog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail, regPwd, regCfmPwd;
    private Button regCreateNewAcc, regAlrHaveAcc;
    private ProgressBar regProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        regEmail = (EditText) findViewById(R.id.reg_email);
        regPwd = (EditText) findViewById(R.id.reg_pwd);
        regCfmPwd = (EditText) findViewById(R.id.reg_cfm_pwd);

        regCreateNewAcc = (Button) findViewById(R.id.reg_create_new_acc_btn);
        regAlrHaveAcc = (Button) findViewById(R.id.reg_alr_have_acc_btn);

        regProgressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);


        regAlrHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(loginIntent);
                finish();
            }
        });

        regCreateNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = regEmail.getText().toString();
                String pwd = regPwd.getText().toString();
                String cfmPwd = regCfmPwd.getText().toString();

                if (!(TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cfmPwd))){
                    if (pwd.equals(cfmPwd)) {

                        regProgressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                    startActivity(setupIntent);
                                    finish();
                                }
                                else {
                                    String errorMsg = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error : "+ errorMsg, Toast.LENGTH_LONG).show();
                                }
                                regProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }else {
                        Toast.makeText(RegisterActivity.this, "Confirm Password and Password does not match", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMain();
        }
    }
    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
