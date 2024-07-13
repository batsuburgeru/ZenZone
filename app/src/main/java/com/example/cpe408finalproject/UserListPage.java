package com.example.cpe408finalproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class UserListPage extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userListPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button userNextButton = findViewById(R.id.userNextButton);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String userRole = intent.getStringExtra("role");

        userNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String username = document.getString("username");
                                String password = document.getString("password");
                                String role = document.getString("role");

                                assert userRole != null;
                                assert role != null;

                                if (role.equals("user")){
                                    role = "User";
                                    addTableRow(tableLayout, username, password, role);
                                } else if (role.equals("admin")){
                                    role = "Admin";
                                    addTableRow(tableLayout, username, password, role);
                                } else if (userRole.equals("superAdmin")){
                                    role = "Super Admin";
                                    addTableRow(tableLayout, username, password, role);
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    public void addTableRow(TableLayout tableLayout, String username, String password, String role) {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Create TextViews for username, password, and action
        TextView usernameText = new TextView(this);
        usernameText.setText(username);
        usernameText.setBackgroundResource(R.drawable.user_list_divider); // Assuming user_list_divider is a drawable resource
        usernameText.setPadding(0, 30, 0, 30); // Set padding in dp
        usernameText.setTextColor(Color.BLACK); // Use Color class for color constants
        usernameText.setGravity(Gravity.CENTER); // Use TextPaint for gravity constants
        usernameText.setTextSize(16); // Set text size in sp
        usernameText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Set weight and other properties

        TextView passwordText = new TextView(this);
        passwordText.setText(password);
        passwordText.setBackgroundResource(R.drawable.user_list_divider); // Assuming user_list_divider is a drawable resource
        passwordText.setPadding(0, 30, 0, 30); // Set padding in dp
        passwordText.setTextColor(Color.BLACK); // Use Color class for color constants
        passwordText.setGravity(Gravity.CENTER); // Use TextPaint for gravity constants
        passwordText.setTextSize(16);
        passwordText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        TextView roleText = new TextView(this);
        roleText.setText(role);
        roleText.setBackgroundResource(R.drawable.user_list_divider); // Assuming user_list_divider is a drawable resource
        roleText.setPadding(0, 30, 0, 30); // Set padding in dp
        roleText.setTextColor(Color.BLACK); // Use Color class for color constants
        roleText.setGravity(Gravity.CENTER); // Use TextPaint for gravity constants
        roleText.setTextSize(16);
        roleText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        // Add TextViews to the table row
        tableRow.addView(usernameText);
        tableRow.addView(passwordText);
        tableRow.addView(roleText);

        // Add the table row to the table layout
        //ViewGroup tableLayout;
        tableLayout.addView(tableRow);
    }


}