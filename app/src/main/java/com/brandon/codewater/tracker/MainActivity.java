package com.brandon.codewater.tracker;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final String SHARED_USERNAME_KEY = "com.brandon.main.username";
    private TextView mTextMessage;
    private FirebaseDatabase fireDb;
    private DatabaseReference fireRef;
    private String username, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fireDb = FirebaseDatabase.getInstance();
        fireRef = fireDb.getReference();

        mTextMessage = (TextView) findViewById(R.id.message);
        if(loadUserCreds() != null) {
            username = loadUserCreds()[0];
            userid = loadUserCreds()[1];
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        createDbCreds(userid, username);
    }

    private void createDbCreds(final String userid, String username){
        fireRef.child("users").child(userid).child("email").setValue(username);

        fireRef.child("users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("firstname")){
                    final Dialog dialog = new Dialog(MainActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    UtilityFunctions util = new UtilityFunctions();
                    View v = inflater.inflate(R.layout.signup_dialog, null);
                    final EditText firstname = (EditText) v.findViewById(R.id.firstname_input);
                    final EditText lastname = (EditText) v.findViewById(R.id.lastnameinput);
                    Button submit = (Button) v.findViewById(R.id.submit);
                    dialog.setContentView(v);
                    util.setDialogAnimation(dialog,1);
                    dialog.show();

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String first = firstname.getText().toString();
                            String last = lastname.getText().toString();
                            if(first.length() > 2 && last.length() > 2) {
                                fireRef.child("users").child(userid).child("firstname").setValue(first);
                                fireRef.child("users").child(userid).child("lastname").setValue(last);

                                Toast.makeText(getApplicationContext(), "Welcome to Sales Tracker, " + first + " "+ last, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Please avoid non-alphabetic characters", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mTextMessage.setText(R.string.title_home);
                return true;
            case R.id.navigation_dashboard:
                mTextMessage.setText(R.string.title_dashboard);
                return true;
            case R.id.navigation_notifications:
                mTextMessage.setText(R.string.title_notifications);
                return true;
        }
        return false;
    }

    /**
     * Returns [0] username (email) and [1] userid that is connected to the database
     * @return
     */
    private String[] loadUserCreds(){
        String[] userCreds;
        String username, userid;
        SharedPreferences pref = getSharedPreferences(SHARED_USERNAME_KEY, MODE_PRIVATE);
        username = pref.getString("username", null);
        userid = pref.getString("userid", null);

        userCreds = new String[]{username, userid};

        return userCreds;
    }

}
