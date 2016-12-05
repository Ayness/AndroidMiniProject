package com.example.ramzi.sharpytodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    EditText login;
    EditText psswd;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login = (EditText) findViewById(R.id.login);

        name = (EditText) findViewById(R.id.name);

        psswd = (EditText) findViewById(R.id.psswd);
    }
}
