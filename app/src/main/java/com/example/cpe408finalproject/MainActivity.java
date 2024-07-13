package com.example.cpe408finalproject;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
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

                if (username.isEmpty()) {
                    showDialog("Username Required!");
                } else if (password.isEmpty()) {
                    showDialog("Password Required!");
                } else {
                    db.collection("users")
                            .whereEqualTo("username", username)
                            .whereEqualTo("password", password)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Log.w(TAG, "Log in failed", task.getException());
                                            showDialog("Login failed, please try again.");
                                        }

                                        Log.d(TAG, "Login successful");
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String username = document.getString("username");
                                            String role = document.getString("role");

                                            assert username != null;
                                            assert role != null;

                                            Log.d(TAG, username);
                                            Log.d(TAG, role);

                                            switch (role) {
                                                case "superAdmin":
                                                    showDialog("Welcome, Super Admin!");
                                                    Intent superAdminIntent = new Intent(MainActivity.this, superAdminDashboard.class);
                                                    superAdminIntent.putExtra("username", username);
                                                    superAdminIntent.putExtra("role", role);
                                                    startActivity(superAdminIntent);
                                                    break;
                                                case "admin":
                                                    showDialog("Welcome, Admin!");
                                                    Intent adminIntent = new Intent(MainActivity.this, adminDashboard.class);
                                                    adminIntent.putExtra("username", username);
                                                    adminIntent.putExtra("role", role);
                                                    startActivity(adminIntent);
                                                    break;
                                                case "user":
                                                    showDialog("Welcome, User!");
                                                    Intent userIntent = new Intent(MainActivity.this, userDashboard.class);
                                                    userIntent.putExtra("username", username);
                                                    userIntent.putExtra("role", role);
                                                    startActivity(userIntent);
                                                    break;
                                            }
                                        }
                                    } else {
                                        Log.e(TAG, "Login error", task.getException());
                                    }
                                }
                            });

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