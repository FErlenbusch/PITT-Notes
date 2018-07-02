package com.frederlen.android.pittnotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frederlen.android.pittnotes.models.NoteSet;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder> {

    private List<NoteSet> mNoteData;

    private final NoteAdapterOnClickHandler mClickHandler;

    public interface NoteAdapterOnClickHandler {
        void onClick(NoteSet note);
    }

    public NoteAdapter(NoteAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class NoteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNoteTextView;

        public NoteAdapterViewHolder(View view) {
            super(view);
            mNoteTextView = (TextView) view.findViewById(R.id.tv_note_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            NoteSet note = mNoteData.get(adapterPosition);
            mClickHandler.onClick(note);
        }
    }


    @Override
    public NoteAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.note_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new NoteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapterViewHolder noteAdapterViewHolder, int position) {
        String noteInfo = "Note #" + mNoteData.get(position).getId();
        noteAdapterViewHolder.mNoteTextView.setText(noteInfo);
    }

    @Override
    public int getItemCount() {
        if (null == mNoteData) return 0;
        return mNoteData.size();
    }

    public void setNoteData(List<NoteSet> noteData) {
        mNoteData = noteData;
        notifyDataSetChanged();
    }
}
