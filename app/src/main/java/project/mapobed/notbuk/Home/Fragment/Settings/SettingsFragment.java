package project.mapobed.notbuk.Home.Fragment.Settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import project.mapobed.notbuk.Home.HomeActivity;
import project.mapobed.notbuk.R;
import project.mapobed.notbuk.Register.RegisterActivity;


public class SettingsFragment extends Fragment {
    private TextView name, email, password;
    private Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View c = inflater.inflate(R.layout.fragment_settings, container, false);
        name = c.findViewById(R.id.name_settings);
        email = c.findViewById(R.id.email_settings);
        password = c.findViewById(R.id.password_settings);
        logout = c.findViewById(R.id.logout_settings);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            name.setVisibility(View.VISIBLE);
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            email.setVisibility(View.VISIBLE);
            email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            password.setVisibility(View.VISIBLE);

        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logout.getText().toString().trim().equals(getString(R.string.logout))){
                    logoutInit();
                }
                else       {
                    startActivity(new Intent(getContext(), RegisterActivity.class));
                }
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return c;
    }
    private void logoutInit() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            logout.setText(getString(R.string.sign_in));
        }
    }
}