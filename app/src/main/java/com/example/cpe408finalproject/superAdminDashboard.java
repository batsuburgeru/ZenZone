package com.example.cpe408finalproject;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class superAdminDashboard extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView nameGreeting;
    private EditText addUsername;
    private EditText addPassword;
    private RadioGroup userTypeRad;
    private RadioButton adminRad;
    private RadioButton userRad;
    private Button addSubmit;
    private EditText updateCurrentUsername;
    private EditText updateNewUsername;
    private EditText updateNewPass;
    private Button updateSubmit;
    private EditText delUser;
    private Button delSubmit;
    private Button logoutBtn;
    private Button viewUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_super_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nameGreeting = findViewById(R.id.nameGreetingText);
        addUsername = findViewById(R.id.usernameInput);
        addPassword = findViewById(R.id.passwordInput);
        userTypeRad = findViewById(R.id.userTypeRadGroup);
        userRad = findViewById(R.id.userTypeRadio);
        adminRad = findViewById(R.id.adminTypeRadio);
        addSubmit = findViewById(R.id.addUserBtn);
        updateCurrentUsername = findViewById(R.id.currentUsernameInput);
        updateNewUsername = findViewById(R.id.newUsernameInput);
        updateNewPass = findViewById(R.id.newPassInput);
        updateSubmit = findViewById(R.id.updateUserBtn);
        delUser = findViewById(R.id.delUserInput);
        delSubmit = findViewById(R.id.delUserBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        viewUserBtn = findViewById(R.id.viewUserBtn);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        nameGreeting.setText(username);

        viewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewUser = new Intent(superAdminDashboard.this, UserListPage.class);
                startActivity(viewUser);
            }
        });

        addSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = addUsername.getText().toString();
                String password = addPassword.getText().toString();
                String role = "";
                int userType = userTypeRad.getCheckedRadioButtonId();

                if (username.isEmpty() || password.isEmpty() || userType == -1) {
                    showDialog("Please enter the required information.");
                }
                else {
                if (userType == R.id.adminTypeRadio) { // Assuming your admin radio button ID
                    role = "admin";
                } else if (userType == R.id.userTypeRadio) { // Assuming your user radio button ID
                    role = "user";}


                Map<String, Object> users = new HashMap<>();
                users.put("username", username);
                users.put("password", password);
                users.put("role", role);

                db.collection("users")
                        .whereEqualTo("username", username)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        db.collection("users")
                                                .add(users);
                                        Log.w(TAG, "User added");
                                        showDialog("User has been added successfully");
                                    } else {
                                        showDialog("Username has already been taken");
                                    }
                                }
                            }
                        });
            }
        }
    });

        updateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentUser = updateCurrentUsername.getText().toString();
                String newUser = updateNewUsername.getText().toString();
                String password = updateNewPass.getText().toString();

                if (currentUser.isEmpty() || newUser.isEmpty() || password.isEmpty()) {
                    showDialog("Please enter the required information.");
                }
                else {
                    db.collection("users")
                            .whereEqualTo("username", currentUser)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Log.w(TAG, "Updating Failed");
                                            showDialog("User does not exist");
                                        }
                                        else
                                        {
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                            DocumentReference documentReference = document.getReference();
                                            Map<String,Object> users = new HashMap<>();
                                            users.put("username", newUser);
                                            users.put("password", password);
                                            documentReference.update(users)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.w(TAG, "Updated user");
                                                            showDialog("Success! User information updated.");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Updated Failed");
                                                            showDialog("Failed! User information not updated.");
                                                        }
                                                    });}
                                    } else { showDialog("User Information Error");}
                                }
                            });

                }
            }
        });

        delSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = delUser.getText().toString();

                if (username.isEmpty()) {
                    showDialog("Username cannot be blank!");
                }
                else {
                    db.collection("users")
                            .whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult().isEmpty()){
                                            Log.w(TAG, "User nonexistent");
                                            showDialog("User does not exist");
                                        }
                                        else {
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                            DocumentReference documentReference = document.getReference();
                                            documentReference.delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.w(TAG, "Delete success");
                                                            showDialog("Success! User " + username+ " successfully deleted.");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Delete Failed");
                                                            showDialog("Deletion unsuccessful");
                                                        }
                                                    });
                                        }
                                    } else { Log.w(TAG, "Unknown Error"); showDialog("Error Detected");}
                                }
                            });
                }}});

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(superAdminDashboard.this, MainActivity.class));
                finish();
            }
        });
    }
    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(superAdminDashboard.this);
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