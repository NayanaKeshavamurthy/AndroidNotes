package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlin.reflect.KType;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener
{

    private static final String TAG = "MainActivity";
    private final List<Notes> notesList = new ArrayList<>();

    private RecyclerView recyclerView;

    private NotesAdapter noteAdapter;

    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating a reference to the recycler view
        recyclerView = findViewById(R.id.notesRecycler);

        //Data to recyclerview adapter
        noteAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(noteAdapter);

        //set a layout manager for the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initiating activity launcher to expect results back from other activities
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::handleResult);

        loadJSONFile();
    }

    public void handleResult(ActivityResult result)
    {
        // check for null condition
        if (result == null || result.getData() == null)
        {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        // get data result from the intended class
        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK)
        {
            // get serializable data from key saved
            Notes notesObj = (Notes) data.getSerializableExtra("NOTE_INFO");
            // get position from saved key as well
            int notePosition = data.getIntExtra("NOTE_POSITION",-1);

            // if the object in the list not set for position then set new Notes obj for that position
            Notes newNotesObj = new Notes(notesObj.getNotesTitle(), notesObj.getNotesText(), notesObj.getNotesLastSave());
            if (notePosition != -1)
            {
                notesList.set(notePosition, newNotesObj);
                noteAdapter.notifyItemChanged(notePosition);
            }

            else    // else add the object at the end
            {
                notesList.add(newNotesObj);
                noteAdapter.notifyItemInserted(notesList.size());
            }

            setTitle("AndroidNotes ("+ notesList.size()+")");
            sortNotesList();
            saveNotes();

        }
    }

    @Override
    public void onClick(View v)
    {
        //get the position of the note that was clicked on
        int position = recyclerView.getChildLayoutPosition(v);
        //retrieve the note object from the position observed
        Notes noteObj = notesList.get(position);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("NOTE_INFO", noteObj);
        intent.putExtra("NOTE_POSITION", position);

        //launch the activity
        activityResultLauncher.launch(intent);
    }


    @Override
    public boolean onLongClick(View v)
    {
        // get the position of the note clicked on
        int positionClicked = recyclerView.getChildLayoutPosition(v);
        Notes noteClicked = notesList.get(positionClicked);

        // get the object in that position
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // if user clicks Yes -> remove selected notes from list and reset recycle view
        builder.setPositiveButton("Yes", (dialog, which) -> { dialog.dismiss();
            notesList.remove(positionClicked);
            setTitle("AndroidNotes ("+ notesList.size()+")");
            sortNotesList();
            saveNotes();
            noteAdapter.notifyItemRemoved(positionClicked);
        });

        // if user clicks No -> dismiss and go back to the present screen
        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        // display the dialog and get response from user
        builder.setTitle("Delete Note");
        builder.setMessage("Delete Note '" + noteClicked.getNotesTitle() + "'?");
        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }

    public void saveNotes()
    {
        try
        {
            // Save the notes list in the json file using file output stream
            FileOutputStream fOutputStream = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            // use print writer api to write to the file
            PrintWriter printWriter = new PrintWriter(fOutputStream);
            printWriter.print(notesList);
            printWriter.close();
            fOutputStream.close();

            Toast.makeText(this, "Note Successfully Updated", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        // check the id is from which menu
        if (item.getItemId() == R.id.info)
        {
            // get the activity of the about menu to launch that activity
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

            return true;
        }
        else if (item.getItemId() == R.id.addNewNote)
        {
            // get the activity of the add menu to launch that activity
            // it is an option to create a new note
            Intent intent = new Intent(this, EditActivity.class);

            // use result launcher to launch the intent
            activityResultLauncher.launch(intent);
            return true;
        }
        else
        {
            Log.d(TAG, "onOptionsItemSelected: Unknown Menu Item: " + item.getTitle());
            return super.onOptionsItemSelected(item);
        }
    }

    public void loadJSONFile()
    {
        // setting the title with number of notes in the list
        setTitle("Android Notes ("+ notesList.size()+")");
        try
        {
            // read the json file using input stream and buffer reader
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            //read all the lines
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }

            // create a JSON array for all the lines read
            // build new notes objects from the read data
            JSONArray jsonArray = new JSONArray(sb.toString());
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // fetch the data using key as saved
                String title = jsonObject.getString("notes_title");
                String text = jsonObject.getString("notes_text");
                String lastSave = jsonObject.getString("notes_last_save");

                // create notes object from each line
                // add the new note into the notes list
                Notes newNote = new Notes(title, text, lastSave);
                notesList.add(newNote);
                // setting the title with number of notes in the list
                setTitle("Android Notes ("+ notesList.size()+")");
                noteAdapter.notifyItemInserted(notesList.size());
            }
            sortNotesList();
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            notesList.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            notesList.clear();
        }
    }

    public void sortNotesList()
    {

        // Use collections for sorting the notes according to the last modified date and time
        Collections.sort(notesList, (note1, note2) -> {
            if (note1.getNotesLastSave() == null || note2.getNotesLastSave() == null)
                return 0;
            return note1.getNotesLastSave().compareTo(note2.getNotesLastSave());
        });

        // reverse the elements of the list to get the order of descending date and time
        Collections.reverse(notesList);
        //notifying the adapter regarding the change in the note's order in the list
        noteAdapter.notifyDataSetChanged();
    }
}