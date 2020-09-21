package project.mapobed.notbuk.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import project.mapobed.notbuk.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout reset_email;
    private Button reset;
    private ConstraintLayout constraintLayout;
    private String email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth=FirebaseAuth.getInstance();
        constraintLayout = (ConstraintLayout) findViewById(R.id.forgot_constrainLayout);
        reset_email = (TextInputLayout) findViewById(R.id.reset_email);
        reset = (Button) findViewById(R.id.reset_submit);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= reset_email.getEditText().getText().toString().trim();
                if (email.isEmpty()||email.length()==0||!Patterns.EMAIL_ADDRESS.matcher(email).matches()||!mAuth.getCurrentUser().getEmail().equals(email)){
                    reset_email.setError(getString(R.string.email_empty_error));
                    reset_email.requestFocus();
                    return;
                }
                reset_email.setErrorEnabled(false);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, R.string.reset_email_link, Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            snackbarShow(task.getException());
                        }
                    }
                });
            }
        });

    }
    private void snackbarShow(Object text){
        if (text.toString().equals("com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
            text=getString(R.string.check_internet);
        if (text.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password."))
            text=getString(R.string.email_password_mismatch);
        if (text.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted."))
            text=getString(R.string.register_before_login);
        Snackbar snackbar = Snackbar.make(constraintLayout, text.toString(), Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
        snackbar.setTextColor(getResources().getColor(R.color.secondColor));
        snackbar.show();
    }
}