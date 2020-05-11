package id.ac.umn.talkster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NameActivity extends AppCompatActivity {
    EditText username;
    FirebaseAuth auth;
    DatabaseReference ref;

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        auth=FirebaseAuth.getInstance();

        username=findViewById(R.id.dispName);
        btnNext=findViewById(R.id.btnNext);



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username1=username.getText().toString();
                addDb(username1);
                Intent i = new Intent(NameActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void addDb(String username){
        FirebaseUser fbUser=auth.getCurrentUser();
        String userID=fbUser.getUid();

        ref= FirebaseDatabase.getInstance().getReference("Users").child(userID);

        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("id",userID);
        hashMap.put("username",username);
        hashMap.put("imageURL","default");

        ref.setValue(hashMap);

        Toast.makeText(NameActivity.this,username,Toast.LENGTH_SHORT).show();


    }
}
