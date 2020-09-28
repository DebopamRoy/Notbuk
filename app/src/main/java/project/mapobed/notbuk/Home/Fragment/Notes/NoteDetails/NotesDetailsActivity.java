package project.mapobed.notbuk.Home.Fragment.Notes.NoteDetails;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import project.mapobed.notbuk.R;

public class NotesDetailsActivity extends AppCompatActivity {
    private ImageView back, save;
    private EditText title;
    private EditText content;
    private ConstraintLayout nda;
    private Boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);
        back = findViewById(R.id.back_button_details);
        nda = findViewById(R.id.nda_cons);
        save = findViewById(R.id.check_button_details);
        title = findViewById(R.id.title_button);
        content = findViewById(R.id.content_button);
        content.setMovementMethod(new ScrollingMovementMethod());
        title.setText(getIntent().getStringExtra("noteTitle"));
        content.setText(getIntent().getStringExtra("noteContent"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved)
                    finish();
                else {
                    if (title.getText().toString().trim().length() >0 && content.getText().toString().trim().length() >0) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(NotesDetailsActivity.this);
                        dialog.setTitle("Alert!");
                        dialog.setMessage("Do you want to save before exit?");
                        dialog.setIcon(R.drawable.ic_notbuk);
                        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.show();

                    }
                    else
                        finish();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TODO:Save note*/
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}