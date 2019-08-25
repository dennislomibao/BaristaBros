package student.uts.edu.au.baristabrosapp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, email, password, confirmPassword;
    private Button createAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);
        setupUIViews();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //upload to database
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
