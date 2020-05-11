package id.ac.umn.talkster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout em,pw;
    EditText email,password;
    TextView forgotPassword;
    private Button btnLogin;

    FirebaseAuth auth;
    ProgressDialog mLoginProgress;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        em=findViewById(R.id.email);
        pw=findViewById(R.id.password);

        email=em.getEditText();
        password=pw.getEditText();

        mLoginProgress = new ProgressDialog(this);

        Toolbar tb=findViewById(R.id.loginToolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnLogin=findViewById(R.id.btnLogin);
        forgotPassword=findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });

        auth=FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1=email.getText().toString();
                String password1=password.getText().toString();


                if(email1.isEmpty() || password1.isEmpty()){
                    Toast.makeText(LoginActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
                else{
                    mLoginProgress.setTitle("Log In");
                    mLoginProgress.setMessage("Please wait while we log you in :)");
                    mLoginProgress.show();

                    login(email1,password1);
                }

            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh
                = getSharedPreferences("MySharedPref",
                0);

        String s1 = sh.getString("email", "");


        // Setting the fetched data
        // in the EditTexts
        email.setText(s1);
        //age.setText(String.valueOf(a));
    }
    @Override
    protected void onPause()
    {
        super.onPause();

        // Creating a shared pref object
        // with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString("email",
                email.getText().toString());
        myEdit.commit();
    }

    protected void login(String email,String password){
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(auth.getCurrentUser().isEmailVerified()){
                                Intent i = new Intent(LoginActivity.this,NameActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                mLoginProgress.hide();
                                Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_SHORT).show();

                            }

                        }
                        else{
                            mLoginProgress.hide();
                            Toast.makeText(LoginActivity.this,"Incorrect email or password",Toast.LENGTH_SHORT).show();
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


}
