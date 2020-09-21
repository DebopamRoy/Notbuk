package project.mapobed.notbuk.Onboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import project.mapobed.notbuk.R;
import project.mapobed.notbuk.Register.RegisterActivity;

public class OnboardActivity extends AppCompatActivity {
    private TextView next, skip;
    private ViewPager viewPager;
    private List<OnboardModelclass> list;
    private OnboardAdapter introductionAdapter;
    private TabLayout tabLayout;
    private Button getStarted;
    private int position = 0;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        //full screen activity
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        preferences = this.getSharedPreferences("project.mapobed.notbuk.user", MODE_PRIVATE);

        if (declearPrefData()) {
            LoadNextActivity();
        }


        //declaration
        getStarted = (Button) findViewById(R.id.onboard_get_started);
        viewPager = (ViewPager) findViewById(R.id.onboard_view_pager);
        next = (TextView) findViewById(R.id.onboard_next);
        skip = (TextView) findViewById(R.id.onboard_skip);
        tabLayout = (TabLayout) findViewById(R.id.onboard_tab);

        //creation of array list for on board screens
        list = new ArrayList<>();
        list.add(new OnboardModelclass(getString(R.string.onboard_one), getString(R.string.onboard_one_title), R.drawable.onboard_screen_one));
        list.add(new OnboardModelclass(getString(R.string.onboard_two), getString(R.string.onboard_two_title), R.drawable.onboard_screen_two));
        list.add(new OnboardModelclass(getString(R.string.onboard_three), getString(R.string.onboard_three_title), R.drawable.onboard_screen_three));

        introductionAdapter = new OnboardAdapter(list, this);
        viewPager.setAdapter(introductionAdapter);
        tabLayout.setupWithViewPager(viewPager);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (next.getVisibility() == View.VISIBLE) {
                    position = viewPager.getCurrentItem();
                    if (position < list.size()) {
                        position++;
                        LoadAPage(position);
                    }
                    if (position == list.size()) {
                        LoadLastScreen();
                    }
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skip.getVisibility() == View.VISIBLE) {
                    LoadNextActivity();
                    sharedPref();
                }
            }
        });
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getStarted.getVisibility() == View.VISIBLE) {
                    LoadNextActivity();
                    sharedPref();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == list.size() - 1) {
                    LoadLastScreen();
                } else {
                    LoadOtherScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void LoadAPage(int position) {
        viewPager.setCurrentItem(position);
    }

    private void LoadOtherScreen() {
        skip.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        getStarted.setVisibility(View.INVISIBLE);
    }

    private void LoadLastScreen() {
        skip.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        getStarted.setVisibility(View.VISIBLE);
    }

    private void LoadNextActivity() {
        startActivity(new Intent(OnboardActivity.this, RegisterActivity.class));
        finish();
    }

    private void sharedPref() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();

    }

    private boolean declearPrefData() {
        return preferences.getBoolean("isIntroOpened", false);
    }
}