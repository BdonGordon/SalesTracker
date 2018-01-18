package com.brandon.codewater.tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

/**
 * Created by brand on 2017-07-31.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView mUsernameInput;
    private EditText mPasswordInput;
    private Button mSignIn;
    private TextView mForgotPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        mAuth = FirebaseAuth.getInstance();

        mUsernameInput = (AutoCompleteTextView) findViewById(R.id.username_input);
        mPasswordInput = (EditText) findViewById(R.id.password_input);
        mSignIn = (Button) findViewById(R.id.continue_button);
        mForgotPass = (TextView) findViewById(R.id.forgot_password);

        mSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String username;
        final String password;
        final String[] userid = new String[1];
        if(v == mSignIn) {
            username = mUsernameInput.getText().toString();
            password = mPasswordInput.getText().toString();

            if (username.length() < 4 || mPasswordInput.length() < 5) {
                Toast.makeText(this, "Invalid password or email", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences pref = getSharedPreferences(MainActivity.SHARED_USERNAME_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            userid[0] = mAuth.getCurrentUser().getUid();
                            //username which is the user's email
                            editor.putString("username", username);
                            editor.putString("userid", userid[0]);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "Signing in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid password or email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
