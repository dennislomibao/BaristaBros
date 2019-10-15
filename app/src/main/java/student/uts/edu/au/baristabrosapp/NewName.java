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

public class NewName extends AppCompatActivity {
    private EditText newName;
    private Button newChangedName;
    private Button SP;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_name);

        newName = findViewById(R.id.etNewName);
        newChangedName = findViewById(R.id.btnNewName);
        SP = findViewById(R.id.btnSp);
        SP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewName.this, SettingsActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = firebaseDatabase.child("users").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                newName.setText(dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewName.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        newChangedName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {


                    DatabaseReference db = firebaseDatabase.child("users").child(user.getUid()).child("name");
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Boolean check = false;

                            if (dataSnapshot.getValue().equals(newName.getText().toString())) {
                                Toast.makeText(NewName.this, "No Changes Detected, Please Enter A Different Name then One Already Existing", Toast.LENGTH_LONG).show();
//                                check = true;
                            } else {
                                newchangedName();
                                Toast.makeText(NewName.this, "Name Changed", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(NewName.this, SettingsActivity.class));
                            }
//                            if(!check){
//                                newchangedName();
//                                Toast.makeText(NewName.this, "Name Is Same", Toast.LENGTH_SHORT).show();
//                                finish();
//                                startActivity(new Intent(NewName.this, SettingsActivity.class));
//                            } else{
//                                 Toast.makeText(NewName.this, "Name Changed", Toast.LENGTH_SHORT).show();
//
//                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(NewName.this, "Name Format Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    private Boolean isValid() {
        return (!newName.getText().toString().trim().isEmpty());
    }
    private void newchangedName() {
        String Name = newName.getText().toString().trim();
        Map<String, Object> data = new HashMap<>();
        data.put("name", Name);
        firebaseDatabase.child("users").child(user.getUid()).updateChildren(data);
    }

}
