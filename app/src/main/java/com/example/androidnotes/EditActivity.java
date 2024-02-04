package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class EditActivity extends AppCompatActivity
{

    private EditText noteTitle;
    private EditText noteText;
    private Notes note;

    private int position = -1;

    private static final String TAG = "EditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitle = findViewById(R.id.noteTitle);
        noteText = findViewById(R.id.noteText);

        // get data if there is existing
        Intent intent = getIntent();

        if(intent.hasExtra("NOTE_INFO"))
        {
            // retrieve the notes class object from the saved intent
            note = (Notes) intent.getSerializableExtra("NOTE_INFO");
            if(note == null)
            {
                Toast.makeText(this, "Null value returned", Toast.LENGTH_SHORT).show();
                return;
            }
            position = intent.getIntExtra("NOTE_POSITION", -1);
            noteTitle.setText(note.getNotesTitle());
            noteText.setText(note.getNotesText());
        }
        else
        {
            // set empty values for the data
            noteTitle.setText("");
            noteText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.save)
            TitleCheckDialog();
        else
        {
            Log.d(TAG, "onOptionsItemSelected: Unknown Menu Item: " + item.getTitle());
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void TitleCheckDialog()
    {
        if(TextUtils.isEmpty(noteTitle.getText().toString()))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Ok", (dialog, which) -> {
                dialog.dismiss();
                EditActivity.super.onBackPressed();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setTitle("You Cannot save a note without Title!\n Do you want to exit?");
            AlertDialog dialog = builder.create();
            dialog.show();
            //return;
        }
        else
        {
            String noteTitleValue = noteTitle.getText().toString();
            String noteTextValue = noteText.getText().toString();
            String noteLastSaveValue = new Date().toString();
            if (note != null)
            {
                //if there was no change in the note
                if (note.getNotesTitle().equals(noteTitleValue) &&
                        note.getNotesText().equals(noteTextValue))
                {
                    noteLastSaveValue = note.getNotesLastSave();
                }
            }
            Notes note=new Notes(noteTitleValue,noteTextValue,noteLastSaveValue);

            // create a new intent and save the object and its position
            Intent intent = new Intent();
            intent.putExtra("NOTE_INFO", note);
            intent.putExtra("NOTE_POSITION", position);

            setResult(RESULT_OK, intent);
            finish();

        }
    }

    public void onBackPressed()
    {
        String noteTitleValue = noteTitle.getText().toString().trim();
        String noteTextValue = noteText.getText().toString().trim();
        if (note != null)
        {
            // if the data is not changed
            if (note.getNotesTitle().trim().equals(noteTitleValue.trim()) &&
                    note.getNotesText().trim().equals(noteTextValue.trim()))
            {
                EditActivity.super.onBackPressed();
                return;
            }
        }
        //if the fields empty
        else if (noteTitleValue.isEmpty() && noteTextValue.isEmpty()){
            EditActivity.super.onBackPressed();
            return;
        }

        // creating builder to display message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set two buttons to confirm the choice of the user
        // for Yes -> save the changes from the user
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TitleCheckDialog();
            }
        });

        // for No -> dismiss the changes
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditActivity.super.onBackPressed();
            }
        });

        builder.setTitle("Unsaved Changes");
        builder.setMessage("Would you like to save your changes before exiting?");
        AlertDialog dialog = builder.create();
        dialog.show();


    }

}

