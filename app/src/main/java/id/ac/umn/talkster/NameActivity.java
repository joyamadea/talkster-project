package id.ac.umn.talkster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh
                = getSharedPreferences("dispName",
                0);

        String s1 = sh.getString("username", "");


        // Setting the fetched data
        // in the EditTexts
        username.setText(s1);
        //age.setText(String.valueOf(a));
    }
    @Override
    protected void onPause()
    {
        super.onPause();

        // Creating a shared pref object
        // with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences
                = getSharedPreferences("dispName",
                MODE_PRIVATE);
        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString("username",
                username.getText().toString());
        myEdit.commit();
    }
}
