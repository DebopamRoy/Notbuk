package project.mapobed.notbuk.Home.Fragment.Notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.mapobed.notbuk.Home.Fragment.Notes.NoteDetails.NotesDetailsActivity;
import project.mapobed.notbuk.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {
    private List<String> title_list;
    private List<String> content_list;
    private List<String> title_list_one;
    private List<String> content_list_one;
    private NoteClicked noteClicked;
    private Context context;

    public NotesAdapter(List<String> title_list, List<String> content_list, NoteClicked noteClicked, Context context) {
        this.title_list = title_list;
        this.content_list = content_list;
        this.noteClicked = noteClicked;
        this.context = context;
        title_list_one=new ArrayList<>(title_list);
        content_list_one=new ArrayList<>(content_list);
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesHolder holder, final int position) {
        holder.content.setText(title_list.get(position));
        holder.title.setText(content_list.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.inflate(R.menu.menu_more);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_more:
                                v.getContext().startActivity(new Intent(context, NotesDetailsActivity.class)
                                        .putExtra("noteTitle",content_list.get(position)).
                                        putExtra("noteContent",title_list.get(position)));
                                //handle menu1 click
                                return true;
                            case R.id.rename_more:
                                //handle menu2 click
                                return true;
                            case R.id.delete_more:
                                title_list.remove(position);
                                content_list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, content_list.size());
                                //handle menu3 click
                                return true;
                            case R.id.upload_more:
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup icon
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    popup.setForceShowIcon(true);

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return title_list.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, content;
        ConstraintLayout constraintLayout;
        ImageView imageView;

        public NotesHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_item);
            content = itemView.findViewById(R.id.content_item);
            constraintLayout = itemView.findViewById(R.id.title_holder);
            imageView = itemView.findViewById(R.id.more_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            noteClicked.name_of_pos(getAdapterPosition());
        }
    }

    public interface NoteClicked {
        public void name_of_pos(int pos);
    }
}
