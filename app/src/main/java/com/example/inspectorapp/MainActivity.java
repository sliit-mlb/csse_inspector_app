package com.example.inspectorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inspectorapp.Common.CommonConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private EditText username, password;

    /**
     * This is onCreate for MainActivity
     * <p>
     * Assign Toolbar
     * variable assign
     * login onClick Implemented
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(CommonConstants.TOOLBAR_HEADING_FOR_MAIN_ACTIVITY);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        login = findViewById(R.id.btnLogin);
        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                if (un.equals("") || pwd.equals("")) {

                    Toast.makeText(getApplicationContext(), CommonConstants.TOAST_ENTER_USERNAME_AND_PASSWORD, Toast.LENGTH_SHORT).show();

                } else {

                    isInspector(un, pwd);

                }
            }
        });
    }

    /**
     * validate the inspector using entered username password
     * <p>
     * First, Check the username
     * then, check password
     * <p>
     * If username and password valid, Intent change with some String Values{
     * name of the Inspector
     * id of the Inspector
     * His bus no
     * }
     *
     * @param username
     * @param password
     */
    private void isInspector(final String username, final String password) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(CommonConstants.COLLECTION_NAME_INSPECTOR);

        Query checkInspector = reference.orderByChild(CommonConstants.INSPECTOR_KEY_ID).equalTo(username);

        checkInspector.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordFromDB = dataSnapshot.child(username).child(CommonConstants.INSPECTOR_KEY_PASSWORD).getValue(String.class);

                    if (passwordFromDB.equals(password)) {

                        String nameFromDB = dataSnapshot.child(username).child(CommonConstants.INSPECTOR_KEY_NAME).getValue(String.class);
                        String busNoFromDB = dataSnapshot.child(username).child(CommonConstants.INSPECTOR_KEY_BUS_NO).getValue(String.class);
                        String idFromDB = dataSnapshot.child(username).child(CommonConstants.INSPECTOR_KEY_ID).getValue(String.class);

                        Intent home = new Intent(MainActivity.this, Home.class);
                        home.putExtra(CommonConstants.INSPECTOR_KEY_NAME, nameFromDB);
                        home.putExtra(CommonConstants.INSPECTOR_KEY_BUS_NO, busNoFromDB);
                        home.putExtra(CommonConstants.INSPECTOR_KEY_ID, idFromDB);
                        startActivity(home);

                        Toast.makeText(getApplicationContext(), CommonConstants.TOAST_LOGIN_SUCCESS, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), CommonConstants.TOAST_PASSWORD_WRONG, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), CommonConstants.TOAST_USERNAME_NOT_AVAILABLE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}