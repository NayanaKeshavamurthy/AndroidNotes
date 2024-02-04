package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder>
{
    // member variables of the NotesAdapter class : notes list and main activity
    private final List<Notes> notesList;
    private final MainActivity mainActivity;


    public NotesAdapter(List<Notes> notesList, MainActivity mainActivity)
    {
        this.notesList = notesList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // inflating the notes list row layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list , parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new NotesViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position)
    {
        Notes noteObject = notesList.get(position);

        //holder for the note's title
        // check if the length of the title is more than 80 characters, if so, display only 80 characters with three dots else display entire title
        if (noteObject.getNotesTitle().length() >= 80)
            holder.notesTitle.setText(noteObject.getNotesTitle().substring(0,80) + "...");
        else
            holder.notesTitle.setText(noteObject.getNotesTitle());

        //holder for the note's text
        // check if the length of the text is more than 80 characters, if so, display only 80 characters with three dots else display entire content
        if (noteObject.getNotesText().length() >= 80)
            holder.notesText.setText(noteObject.getNotesText().substring(0,80) + "...");
        else
            holder.notesText.setText(noteObject.getNotesText());

        //holder for the note's last save date
        holder.notesLastSave.setText(noteObject.getNotesLastSave());
    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
