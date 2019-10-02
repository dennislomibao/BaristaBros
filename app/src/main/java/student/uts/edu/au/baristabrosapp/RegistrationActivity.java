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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, email, password, confirmPassword;
    private Button createAccount;
    private Button loginActivity;
    private Button nextPage;
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
        progressDialog = new ProgressDialog(this);

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    Intent intent = new Intent();
                    intent.putExtra("name", userName.getText().toString().trim());
                    intent.putExtra("email", email.getText().toString().trim());
                    intent.putExtra("password", password.getText().toString().trim());
                    intent.setClass(RegistrationActivity.this, Registration2Activity.class);
                    startActivity(intent);

                }
            }
        });
    }

    private void setupUIViews() {
        userName = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfPassword);
        createAccount = findViewById(R.id.btnCreate);
        loginActivity = findViewById(R.id.btnCreates);
        nextPage = findViewById(R.id.btnNext);

        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    private Boolean validate() {
        Boolean result = false;

        String name = userName.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String cPassword = confirmPassword.getText().toString();


        //sign up criteria
        if (name.isEmpty() || Email.isEmpty() || Password.isEmpty() || cPassword.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else if (!Email.contains("@")) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
        } else if (!Password.equals(cPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (Password.length() < 8 || cPassword.length() < 8) {
            Toast.makeText(this, "Password must contain a minimum of 8 characters", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }
}
