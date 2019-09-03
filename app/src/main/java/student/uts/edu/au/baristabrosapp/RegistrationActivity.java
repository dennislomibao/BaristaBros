package student.uts.edu.au.baristabrosapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, email, password, confirmPassword;
    private Button createAccount;
    private Button loginActivity;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog =  new ProgressDialog(this);


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //upload to database
                    String user_email = email.getText().toString().trim();
                    String user_password = password.getText().toString().trim();

                    progressDialog.setMessage("Please Wait Patiently");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Successful",Toast.LENGTH_SHORT).show();

                                //Store user's details in firebase database
                                user = task.getResult().getUser();
                                firebaseDatabase.child("users").child(user.getUid()).child("name").setValue(userName.getText().toString().trim());
                                firebaseDatabase.child("users").child(user.getUid()).child("email").setValue(email.getText().toString().trim());
                                firebaseDatabase.child("users").child(user.getUid()).child("password").setValue(password.getText().toString().trim());

                                finish();
                                startActivity(new Intent(RegistrationActivity.this, HomePageActivity.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Account Already Exists",Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
                }
            }
        });


    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etName);
        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        confirmPassword = (EditText) findViewById(R.id.etConfPassword);
        createAccount = (Button) findViewById(R.id.btnCreate);
        loginActivity = (Button) findViewById(R.id.btnCreates);

        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    private Boolean validate () {
        Boolean result = false;

        String name = userName.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String cPassword = confirmPassword.getText().toString();


        //sign up criteria
        if(name.isEmpty() || Email.isEmpty() || Password.isEmpty() || cPassword.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }else if (!Email.contains("@")){
            Toast.makeText(this,"Invalid email",Toast.LENGTH_SHORT).show();
        } else if (!Password.equals(cPassword)){
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        } else if (Password.length() < 8 || cPassword.length() < 8) {
            Toast.makeText(this,"Password must contain a minimum of 8 characters",Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }
}
