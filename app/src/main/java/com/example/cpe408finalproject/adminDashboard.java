package com.example.cpe408finalproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class adminDashboard extends AppCompatActivity {

    private TextView nameGreeting;
    private EditText addUsername;
    private EditText addPassword;
    private Button addSubmit;
    private EditText updateCurrentUsername;
    private EditText updateNewUsername;
    private EditText updateNewPass;
    private Button updateSubmit;
    private EditText delUser;
    private Button delSubmit;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameGreeting = findViewById(R.id.nameGreetingText);
        addUsername = findViewById(R.id.usernameInput);
        addPassword = findViewById(R.id.passwordInput);
        addSubmit = findViewById(R.id.addUserBtn);
        updateCurrentUsername = findViewById(R.id.currentUsernameInput);
        updateNewUsername = findViewById(R.id.newUsernameInput);
        updateNewPass = findViewById(R.id.newPassInput);
        updateSubmit = findViewById(R.id.updateUserBtn);
        delUser = findViewById(R.id.delUserInput);
        delSubmit = findViewById(R.id.delUserBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
    }
    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(adminDashboard.this);
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