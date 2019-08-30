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

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, email, password, confirmPassword;
    private Button createAccount;
    private Button loginActivity;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
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
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Failed",Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
                }
            }
        });


    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etFName);
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


        if(name.isEmpty() && Email.isEmpty() && Password.isEmpty() && cPassword.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }
}
