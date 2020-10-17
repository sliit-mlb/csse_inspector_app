package com.example.inspectorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle("Inspector Login");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        login = findViewById(R.id.btnLogin);
        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                if(un.equals("") || pwd.equals("")){

                    Toast.makeText(getApplicationContext(),"Please enter username and password",Toast.LENGTH_SHORT).show();

                }else {

                    isInspector(un, pwd);

                }
            }
        });
    }

    private void isInspector(final String username, final String password) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Inspector");

        Query checkInspector = reference.orderByChild("id").equalTo(username);

        checkInspector.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String passwordFromDB = dataSnapshot.child(username).child("password").getValue(String.class);

                    if(passwordFromDB.equals(password)){

                        String nameFromDB = dataSnapshot.child(username).child("name").getValue(String.class);
                        String busNoFromDB = dataSnapshot.child(username).child("busNo").getValue(String.class);
                        String idFromDB = dataSnapshot.child(username).child("id").getValue(String.class);

                        Intent home = new Intent(MainActivity.this, Home.class);
                        home.putExtra("name",nameFromDB);
                        home.putExtra("busNo",busNoFromDB);
                        home.putExtra("id", idFromDB);
                        startActivity(home);

                        Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Password wrong",Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Username not available",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}