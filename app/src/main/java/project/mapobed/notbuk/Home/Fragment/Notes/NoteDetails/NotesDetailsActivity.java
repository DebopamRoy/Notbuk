package project.mapobed.notbuk.Home.Fragment.Notes.NoteDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import project.mapobed.notbuk.R;

public class NotesDetailsActivity extends AppCompatActivity {
    private ImageView back;
    private TextView title;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);
        back = findViewById(R.id.back_button_details);
        title = findViewById(R.id.title_button);
        content = findViewById(R.id.content_button);
        content.setMovementMethod(new ScrollingMovementMethod());
        title.setText(getIntent().getStringExtra("noteTitle"));
        content.setText(getIntent().getStringExtra("noteContent"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}