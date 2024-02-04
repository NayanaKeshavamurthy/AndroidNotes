package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder
{
    // member variables
    TextView notesTitle;
    TextView notesLastSave;
    TextView notesText;

    public NotesViewHolder(View view)
    {
        super(view);

        // resource variables
        notesTitle = view.findViewById(R.id.noteTitle);
        notesLastSave = view.findViewById(R.id.noteLastSave);
        notesText = view.findViewById(R.id.noteText);
    }
}
