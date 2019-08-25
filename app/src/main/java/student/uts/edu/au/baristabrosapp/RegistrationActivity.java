package student.uts.edu.au.baristabrosapp;


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

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, email, password, confirmPassword;
    private Button createAccount;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //upload to database
                    String user_email = email.getText().toString().trim();
                    String user_password = password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "Registration Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, HomePageActivity.class));
                            }else{
                                Toast.makeText(RegistrationActivity.this, "Registration Failed",Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
                }
            }
        });


    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUsername);
        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        confirmPassword = (EditText) findViewById(R.id.etConfPassword);
        createAccount = (Button) findViewById(R.id.btnCreate);
    }

    private Boolean validate () {
        Boolean result = false;

        String name = userName.getText().toString();
        String Password = password.getText().toString();
        String Email = email.getText().toString();

        if(name.isEmpty() && Password.isEmpty() && Email.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }
}
