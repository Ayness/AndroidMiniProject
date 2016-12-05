package com.example.ramzi.sharpytodo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    EditText login;
    EditText psswd;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        login = (EditText) findViewById(R.id.login);

        psswd = (EditText) findViewById(R.id.password);

        db = new DBHelper(this);

        Button mEmailSignInButton = (Button) findViewById(R.id.btnLogin);
        TextView signUp = (TextView) findViewById(R.id.signUp);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((login.getText().toString().equals(""))||(psswd.getText().toString().equals(""))){
                    Toast.makeText(LoginActivity.this,"Login or Password empty ! " +
                            "Try Again",Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = db.getUser(login.getText().toString());
                    if(user==null){
                        Toast.makeText(LoginActivity.this,"Login incorrect ! " +
                                "Try Again",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!user.getPasswd().equals(psswd.getText().toString())){
                            Toast.makeText(LoginActivity.this,"Password incorrect ! " +
                                    "Try Again",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent i = new Intent(LoginActivity.this,ListEvents.class);
                            startActivity(i);
                            SharedPreferences prefs = LoginActivity.this.getSharedPreferences(
                                    "com.example.ramzi.sharpytodo", Context.MODE_PRIVATE);
                            String login = user.getLogin();
                            Log.e("LoginActivity login", login);
                            boolean b = prefs.edit().putString("login",login).commit();
                            Log.e("bool commit", Boolean.toString(b));
                            String name = user.getLogin();
                            prefs.edit().putString("name", name).commit();
                            login = prefs.getString("login","dummy");
                            Log.e("login act pref login","le "+login);
                        }
                    }
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View popupLayout = inflater.inflate(R.layout.activity_sign_up, null);
                final AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Add New User")
                        .setView(popupLayout)
                        .setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name =((EditText) popupLayout.findViewById(R.id.name)).getText().toString();
                                String login =((EditText) popupLayout.findViewById(R.id.login)).getText().toString();
                                String psswd =((EditText) popupLayout.findViewById(R.id.psswd)).getText().toString();
                                User user = new User(login, name, psswd);
                                DBHelper db = new DBHelper(LoginActivity.this);
                                db.addUser(user);
                                Intent in = new Intent(LoginActivity.this,ListEvents.class);
                                SharedPreferences prefs = LoginActivity.this.getSharedPreferences(
                                        "com.example.ramzi.sharpytodo", Context.MODE_PRIVATE);
                                String login1 = user.getLogin();
                                prefs.edit().putString("login", login1).apply();
                                String name1 = user.getLogin();
                                prefs.edit().putString("name", name1).apply();
                                startActivity(in);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

    }

}

