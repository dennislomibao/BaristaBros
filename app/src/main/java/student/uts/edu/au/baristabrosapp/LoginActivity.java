package student.uts.edu.au.baristabrosapp;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button singUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        singUp = (Button) findViewById(R.id.btnSignUp);
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationActivity();
            }
        });
    }

    public void openRegistrationActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);

    }


}

