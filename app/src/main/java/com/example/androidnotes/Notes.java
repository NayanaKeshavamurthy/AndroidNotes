package com.example.androidnotes;

import android.util.JsonWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import androidx.annotation.NonNull;

public class Notes implements Serializable{
    private String notesTitle;
    private String notesText;
    private String notesLastSave;


    // parameterized constructor
    public Notes(String notesTitle, String notesText, String notesLastSave)
    {
       this.notesTitle = notesTitle;
       this.notesText = notesText;
       this.notesLastSave = notesLastSave;
    }

    // getter function for notesTitle member variable
    public String getNotesTitle()
    {
        return notesTitle;
    }

    // setter function for notesTitle member variable
    public void setNotesTitle(String notesTitle)
    {
        this.notesTitle = notesTitle;
    }

    // getter function for notesText member variable
    public String getNotesText()
    {
        return notesText;
    }

    // setter function for notesText member variable
    public void setNotesText(String notesText)
    {
        this.notesText = notesText;
    }

    // getter function for notesLastSave member variable
    public String getNotesLastSave()
    {
        return notesLastSave;
    }

    // setter function for notesLastSave member variable
    public void setNotesLastSave(String notesLastSave)
    {
        this.notesLastSave = notesLastSave;
    }

    @NonNull
    @Override
    public String toString()
    {
        // Used to get the content into a json file format
        try
        {
            // Initialise a json writer
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonFormatter = new JsonWriter(stringWriter);

            // set the indent as space
            jsonFormatter.setIndent("  ");

            // save the data as key value pair
            jsonFormatter.beginObject();
            jsonFormatter.name("notes_title").value(getNotesTitle());
            jsonFormatter.name("notes_text").value(getNotesText());
            jsonFormatter.name("notes_last_save").value(getNotesLastSave());
            jsonFormatter.endObject();
            jsonFormatter.close();

            // return the toString of the string writer to the calling object
            return stringWriter.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

}
