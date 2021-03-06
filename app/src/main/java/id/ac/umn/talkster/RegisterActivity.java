package id.ac.umn.talkster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout un,em,pw,pn;
    EditText username,email,password,phoneNum;
    private Button btnRegister;
    ProgressDialog mRegisterProgress;
    String number;

    FirebaseAuth auth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //un=findViewById(R.id.username);
        em=findViewById(R.id.email);
        pw=findViewById(R.id.password);

        //username=un.getEditText();
        email=em.getEditText();
        password=pw.getEditText();

        btnRegister=findViewById(R.id.btnRegis);
        mRegisterProgress = new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();

        Toolbar tb=findViewById(R.id.regisToolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String username1=username.getText().toString();
                String email1=email.getText().toString();
                String password1=password.getText().toString();

                if(TextUtils.isEmpty(email1) || TextUtils.isEmpty(password1)){
                    Toast.makeText(RegisterActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
                else if(password1.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password must be at least 6 characters long",Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                    Toast.makeText(RegisterActivity.this,"Invalid email format",Toast.LENGTH_SHORT).show();
                }
                else{
                    mRegisterProgress.setTitle("Registering User");
                    mRegisterProgress.setMessage("Please wait while we create your account :)");
                    mRegisterProgress.show();
                    register(email1,password1);
                }

            }
        });
    }


    private void register(final String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            auth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                mRegisterProgress.hide();
                                                Toast.makeText(RegisterActivity.this, "Registered succesfully. Please check your email for verification", Toast.LENGTH_LONG).show();
//                                                FirebaseUser fbUser=auth.getCurrentUser();
//                                                String userID=fbUser.getUid();
//
//                                                ref= FirebaseDatabase.getInstance().getReference("Users").child(userID);
//
//                                                HashMap<String, String> hashMap=new HashMap<>();
//                                                hashMap.put("id",userID);
//                                                hashMap.put("username",username);
//                                                hashMap.put("imageURL","default");
//
//                                                ref.setValue(hashMap);


                                            }
                                            else{
                                                mRegisterProgress.hide();
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        }
                        else{
                            mRegisterProgress.hide();
                            Toast.makeText(RegisterActivity.this,"Register failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(RegisterActivity.this,StartActivity.class);
        startActivity(i);
        finish();
    }
}
