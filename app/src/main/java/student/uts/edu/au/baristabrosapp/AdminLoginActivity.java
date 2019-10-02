package student.uts.edu.au.baristabrosapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private ProgressDialog progressDialog;

    private EditText Email;
    private EditText Password;
    private Button Login;
    private Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnSubmit);
        Back = findViewById(R.id.btnBack);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {
                    validate(Email.getText().toString(), Password.getText().toString());

                }

            }
        });
    }

    private void validate(String userName, String userPassword) {

        progressDialog.setMessage("Please Wait Patiently");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Boolean emailFlag = user.isEmailVerified();

                    if (emailFlag) {

                        DatabaseReference checkAdmin = firebaseDatabase.child("users").child(user.getUid()).child("accountType");
                        checkAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                //change accountType to "admin" in firebase database to gain access
                                if (dataSnapshot.getValue(String.class).equals("admin")) {

                                    Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(AdminLoginActivity.this, AdminHomePageActivity.class));

                                } else {

                                    Toast.makeText(AdminLoginActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {

                        Toast.makeText(AdminLoginActivity.this, "Please verify you email", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();

                    }

                } else {

                    Toast.makeText(AdminLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
