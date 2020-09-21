package project.mapobed.notbuk.Home.Fragment.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import project.mapobed.notbuk.Home.Fragment.Notes.NoteDetails.NotesDetailsActivity;
import project.mapobed.notbuk.R;

public class NotesFragment extends Fragment implements NotesAdapter.NoteClicked {
    private RecyclerView recyclerView;
    private List<String> title, content;
    private NotesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = v.findViewById(R.id.notes_recycle);

        title = new ArrayList<>();
        content = new ArrayList<>();
        title.add("Appload");
        content.add("Debopam Roy is a good boy and will do good in zdfsdfnearadssssssssssssasasasasasasasasasasasasasasasasasasa future");
        title.add("Applooiulad");
        content.add("Debopam Roy is a good boy and will do good issadasdsadsddsdsasadsdadsasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasdasadn near future");
        title.add("Apkuykuyuipload");
        content.add("Debopam Roy is a goon near futuredsdsa");
        title.add("Apphgguload");
        content.add(" do good in near future");
        title.add("Applodfdfgad");
        content.add("Debopam Roy is a gfsdffffffffffffffffffffdsaaaaaffffffffnd will do good in near future");
        title.add("Apploasadd");
        content.add("Debopam Roy is a good boy and will do good in near future");

        adapter = new NotesAdapter(content, title, this, getContext());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void name_of_pos(int pos) {
        startActivity(new Intent(getActivity(), NotesDetailsActivity.class));
    }
}