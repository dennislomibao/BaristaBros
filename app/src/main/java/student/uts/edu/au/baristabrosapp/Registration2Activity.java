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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration2Activity extends AppCompatActivity {

    private EditText addressLine, suburb, country, state, postcode;
    private Button createAccount;
    private Button loginActivity;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    private String name;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        addressLine = findViewById(R.id.etAddress);
        suburb = findViewById(R.id.etSuburb);
        country = findViewById(R.id.etCountry);
        state = findViewById(R.id.etState);
        postcode = findViewById(R.id.etPostcode);
        createAccount = findViewById(R.id.btnCreate);
        loginActivity = findViewById(R.id.btnLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //upload to database
                    String user_email = email;
                    String user_password = password;

                    progressDialog.setMessage("Please Wait Patiently");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();
                                //Store user's details in firebase database
                                user = task.getResult().getUser();
                                firebaseDatabase.child("users").child(user.getUid()).child("name").setValue(name);
                                firebaseDatabase.child("users").child(user.getUid()).child("email").setValue(email);
                                firebaseDatabase.child("users").child(user.getUid()).child("password").setValue(password);
                                firebaseDatabase.child("users").child(user.getUid()).child("address").setValue(addressLine.getText().toString().trim());
                                firebaseDatabase.child("users").child(user.getUid()).child("suburb").setValue(suburb.getText().toString().trim());
                                firebaseDatabase.child("users").child(user.getUid()).child("country").setValue(country.getText().toString().trim());
                                firebaseDatabase.child("users").child(user.getUid()).child("state").setValue(state.getText().toString().trim());
                                firebaseDatabase.child("users").child(user.getUid()).child("postcode").setValue(postcode.getText().toString().trim());
                                firebaseDatabase.child("users").child(user.getUid()).child("accountType").setValue("standard");

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Registration2Activity.this, "Account Already Exists", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });

        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration2Activity.this, LoginActivity.class));
            }
        });
    }

    private Boolean validate() {

        String Address = addressLine.getText().toString();
        String Suburb = suburb.getText().toString();
        String Country = country.getText().toString();
        String State = state.getText().toString();
        String Postcode = postcode.getText().toString();

        if (Address.isEmpty() || Suburb.isEmpty() || Country.isEmpty() || State.isEmpty() || Postcode.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Registration2Activity.this, "Successfully registered, please verify your email", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Registration2Activity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(Registration2Activity.this, "Error sending email", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
    }
}
