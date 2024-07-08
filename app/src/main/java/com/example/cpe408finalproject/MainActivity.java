package com.example.cpe408finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText loginInputUsername;
    private EditText loginInputPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loginInputUsername = findViewById(R.id.loginInputUsername);
        loginInputPassword = findViewById(R.id.loginInputPassword);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username = loginInputUsername.getText().toString();
                String password = loginInputPassword.getText().toString();
                String role = "user";

                if (username.isEmpty()) {
                    showDialog("Username Required!");
                }
                else if (password.isEmpty()) {
                    showDialog("Password Required!");
                } else if (role.equals("superAdmin")){
                    showDialog("Welcome, Super Admin!");
                    Intent superAdminIntent = new Intent(MainActivity.this, superAdminDashboard.class);
                    superAdminIntent.putExtra("username", username);
                    superAdminIntent.putExtra("role", role);
                    startActivity(superAdminIntent);
                } else if (role.equals("admin")){
                    showDialog("Welcome, Admin!");
                    Intent adminIntent = new Intent(MainActivity.this, adminDashboard.class);
                    adminIntent.putExtra("username", username);
                    adminIntent.putExtra("role", role);
                    startActivity(adminIntent);
                } else if (role.equals("user")) {
                    showDialog("Welcome, User!");
                    Intent userIntent = new Intent(MainActivity.this, userDashboard.class);
                    userIntent.putExtra("username", username);
                    userIntent.putExtra("role", role);
                    startActivity(userIntent);
                }
            }
        });
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}