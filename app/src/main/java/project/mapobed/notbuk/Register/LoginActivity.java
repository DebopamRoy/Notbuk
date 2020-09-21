package project.mapobed.notbuk.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import project.mapobed.notbuk.Home.HomeActivity;
import project.mapobed.notbuk.R;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout login_email, login_password;
    private TextView login_forgot_password, login_register, login_skip;
    private Button login_submit;
    private ConstraintLayout constrainLayout;
    private String email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        login_email = (TextInputLayout) findViewById(R.id.login_email);
        login_password = (TextInputLayout) findViewById(R.id.login_password);
        login_forgot_password = (TextView) findViewById(R.id.login_forgot_password);
        login_register = (TextView) findViewById(R.id.login_register);
        login_skip = (TextView) findViewById(R.id.login_skip);
        login_submit = (Button) findViewById(R.id.login_submit);
        constrainLayout = (ConstraintLayout) findViewById(R.id.login_constrainLayout);

        login_email.getEditText().setText(getIntent().getStringExtra("userEmail"));

        login_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startHomeActivity();
            }
        });

        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = login_email.getEditText().getText().toString().trim();
                password = login_password.getEditText().getText().toString().trim();

                if (email.isEmpty() || email.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    login_email.setError(getString(R.string.email_empty_error));
                    login_email.requestFocus();
                    return;
                }
                login_email.setErrorEnabled(false);

                if (password.isEmpty() || email.length() == 0 || password.length() < 6 || password.length() > 22) {
                    login_password.setError(getString(R.string.password_empty_error));
                    login_password.requestFocus();
                    return;
                }
                login_password.setErrorEnabled(false);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).putExtra("userName",getIntent().getStringExtra("userName")));
                                LoginActivity.this.finish();
                            } else
                                snackbarShow(getString(R.string.verify_login));
                        } else {
                            snackbarShow(task.getException());
                            Log.d("ERROR", task.getException() + "");
                        }
                    }
                });

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        LoginActivity.this.finish();
    }

    private void startHomeActivity() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        LoginActivity.this.finish();
    }

    private void snackbarShow(Object text) {
        if (text.toString().equals("com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
            text = getString(R.string.check_internet);
        if (text.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password."))
            text = getString(R.string.email_password_mismatch);
        if (text.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted."))
            text = getString(R.string.register_before_login);
        Snackbar snackbar = Snackbar.make(constrainLayout, text.toString(), Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
        snackbar.setTextColor(getResources().getColor(R.color.secondColor));
        snackbar.show();
    }
}