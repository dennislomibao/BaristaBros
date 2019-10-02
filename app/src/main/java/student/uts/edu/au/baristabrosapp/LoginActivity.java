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

public class LoginActivity extends AppCompatActivity {
    private Button singUp;
    private FirebaseAuth firebaseAuth;
    private EditText Email;
    private EditText Password;
    private Button resetPassword;
    private Button Login;
    private Button admin;
    private EditText validate;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUIViews();

        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        resetPassword = findViewById(R.id.btnResetPassword);
        admin = findViewById(R.id.btnAdmin);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        Login = findViewById(R.id.btnSubmit);
        /*Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(LoginActivity.this,HomePageActivity.class));

            }
        });*/

        singUp = findViewById(R.id.btnSignUp);
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationActivity();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {
                    validate(Email.getText().toString(), Password.getText().toString());

                }
            }
        });
    }

    public void openRegistrationActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);

    }

    private void validate(String userName, String userPassword) {

        progressDialog.setMessage("Please Wait Patiently");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    checkEmailVerification();

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void setupUIViews() {

        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);

    }

    //Phone back button closes app
    @Override
    public void onBackPressed() {

        //Back button goes to home screen instead of previous activity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if (emailFlag) {

            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));

        } else {
            Toast.makeText(LoginActivity.this, "Please verify you email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
}






