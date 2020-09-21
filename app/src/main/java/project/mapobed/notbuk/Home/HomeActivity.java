package project.mapobed.notbuk.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import project.mapobed.notbuk.Home.Fragment.About.AboutDeveloperFragment;
import project.mapobed.notbuk.Home.Fragment.Cloud.CloudFragment;
import project.mapobed.notbuk.Home.Fragment.Help.HelpFragment;
import project.mapobed.notbuk.Home.Fragment.Notes.NotesFragment;
import project.mapobed.notbuk.Home.Fragment.Settings.SettingsFragment;
import project.mapobed.notbuk.R;
import project.mapobed.notbuk.Register.RegisterActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView menu, search, logout;
    private NavigationView navigationView;
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private View headerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView profile_name_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        menu = (ImageView) findViewById(R.id.home_menu);
        search = (ImageView) findViewById(R.id.hpme_search);
        frameLayout = (FrameLayout) findViewById(R.id.fame_layout_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer);
        navigationView = (NavigationView) findViewById(R.id.home_nav);
        headerLayout = navigationView.getHeaderView(0);
        profile_name_header = headerLayout.findViewById(R.id.profile_name_header);
        logout = headerLayout.findViewById(R.id.logout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.setDrawerIndicatorEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.notes);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            profile_name_header.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().trim());
            logout.setVisibility(View.VISIBLE);
        } else {
            profile_name_header.setText(getString(R.string.sign_in));
            logout.setVisibility(View.INVISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutInit();
            }
        });

        profile_name_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile_name_header.getText().equals(getString(R.string.sign_in))) ;
                {
                    startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fame_layout_home, new NotesFragment()).commit();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void logoutInit() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            profile_name_header.setText(getString(R.string.sign_in));
            logout.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.notes:
                fragment = new NotesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fame_layout_home, fragment).commit();
                break;
            case R.id.cloud:
                fragment = new CloudFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fame_layout_home, fragment).commit();
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fame_layout_home, fragment).commit();
                break;
            case R.id.share_app:
                ShareCompat.IntentBuilder.from(this).setType("text/plain").setChooserTitle("Chooser title")
                        .setText("http://play.google.com/store/apps/details?id=" + this.getPackageName()).startChooser();
                break;
            case R.id.rate_us:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.getPackageName())));
                break;
            case R.id.help:
                fragment = new HelpFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fame_layout_home, fragment).commit();

                break;
            case R.id.about_developer:
                fragment = new AboutDeveloperFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fame_layout_home, fragment).commit();
                break;
            case R.id.suggest:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"debo.roy79@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Attention Please, this message has been sent from Notbuk android application");
                email.putExtra(Intent.EXTRA_TEXT, "Type your suggestion here...");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
            case R.id.pay_me:
                break;
            case R.id.exit:
                System.exit(0);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}