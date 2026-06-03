package com.example.notdefterim;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public static final int[] NOTE_COLORS = {
            0xFF1E1E2E,
            0xFF2D1B33,
            0xFF1A2F1A,
            0xFF1A1A2F,
            0xFF2F1A1A,
            0xFF2F2A1A,
    };

    private List<Note> notes;
    private OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
    }

    public NoteAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDate.setText(note.getDate());

        String content = note.getContent();
        if (content != null && !content.isEmpty()) {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(content.length() > 120 ?
                    content.substring(0, 120) + "…" : content);
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }

        int colorIndex = note.getColorIndex();
        if (colorIndex >= 0 && colorIndex < NOTE_COLORS.length) {
            holder.cardView.setCardBackgroundColor(NOTE_COLORS[colorIndex]);
        }

        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onNoteLongClick(note);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvContent, tvDate;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView  = itemView.findViewById(R.id.cardView);
            tvTitle   = itemView.findViewById(R.id.tvNoteTitle);
            tvContent = itemView.findViewById(R.id.tvNoteContent);
            tvDate    = itemView.findViewById(R.id.tvNoteDate);
        }
    }
}