package student.uts.edu.au.baristabrosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Email extends AppCompatActivity {

    private EditText newEmail;
    private Button newChangedEmail;
    private Button settingsPage;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        newEmail = findViewById(R.id.etEmail);
        newChangedEmail = findViewById(R.id.btnEmail);
        settingsPage = findViewById(R.id.btnGobackToSettings);
        settingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Email.this,SettingsActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = firebaseDatabase.child("users").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                newEmail.setText(dataSnapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Email.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        newChangedEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()) {
                    DatabaseReference db = firebaseDatabase.child("users");
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean check = false;
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                if (user.child("email").getValue().equals(newEmail.getText().toString().trim()) && !check) {
                                    check = true;
                                }
                            }
                            if (!check) {
                                newchangedEmail();
                                Toast.makeText(Email.this, "Email Changed", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Email.this, SettingsActivity.class));
                            } else {
                                Toast.makeText(Email.this, "No Changes Detected, Please Enter A Different Email then One Already Existing", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(Email.this, "Email Format Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean isValid() {
        return (!newEmail.getText().toString().trim().isEmpty());
    }

    private void newchangedEmail() {
        String Email = newEmail.getText().toString().trim();
        Map<String, Object> data = new HashMap<>();
        data.put("email", Email);
        firebaseDatabase.child("users").child(user.getUid()).updateChildren(data);
    }
}
