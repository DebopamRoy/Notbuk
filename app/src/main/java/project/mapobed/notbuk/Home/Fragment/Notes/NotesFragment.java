package project.mapobed.notbuk.Home.Fragment.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import project.mapobed.notbuk.Home.Fragment.Notes.NoteDetails.NotesDetailsActivity;
import project.mapobed.notbuk.R;

public class NotesFragment extends Fragment implements NotesAdapter.NoteClicked {
    private RecyclerView recyclerView;
    private List<String> title, content;
    private NotesAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private SearchView search_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = v.findViewById(R.id.notes_recycle);
        floatingActionButton=v.findViewById(R.id.add_fab_home);
        search_text = v.findViewById(R.id.search_text_home);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),NotesDetailsActivity.class));
            }
        });

        search_text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(s);
                return false;
            }
        });


        title = new ArrayList<>();
        content = new ArrayList<>();

        title.add("Malaria");
        content.add("Malaria is a mosquito-borne infectious disease that affects humans and other animals. Malaria causes symptoms that typically include fever, tiredness, vomiting, and headaches. In severe cases it can cause yellow skin, seizures, coma, or death. Symptoms usually begin ten to fifteen days after being bitten by an infected mosquito. If not properly treated, people may have recurrences of the disease months later. In those who have recently survived an infection, reinfection usually causes milder symptoms.This partial resistance disappears over months to years if the person has no continuing exposure to malaria.");

        title.add("Dengue");
        content.add("Dengue fever is a mosquito-borne tropical disease caused by the dengue virus. Symptoms typically begin three to fourteen days after infection.[2] These may include a high fever, headache, vomiting, muscle and joint pains, and a characteristic skin rash. Recovery generally takes two to seven days. In a small proportion of cases, the disease develops into severe dengue, also known as dengue hemorrhagic fever, resulting in bleeding, low levels of blood platelets and blood plasma leakage, or into dengue shock syndrome, where dangerously low blood pressure occurs.");

        title.add("Football");
        content.add("Football is a family of team sports that involve, to varying degrees, kicking a ball to score a goal. Unqualified, the word football normally means the form of football that is the most popular where the word is used. Sports commonly called football include association football (known as soccer in some countries); gridiron football (specifically American football or Canadian football); Australian rules football; rugby football (either rugby union or rugby league); and Gaelic football.");

        title.add("Malaria");
        content.add("Malaria is a mosquito-borne infectious disease that affects humans and other animals. Malaria causes symptoms that typically include fever, tiredness, vomiting, and headaches. In severe cases it can cause yellow skin, seizures, coma, or death. Symptoms usually begin ten to fifteen days after being bitten by an infected mosquito. If not properly treated, people may have recurrences of the disease months later. In those who have recently survived an infection, reinfection usually causes milder symptoms.This partial resistance disappears over months to years if the person has no continuing exposure to malaria.");

        title.add("Dengue");
        content.add("Dengue fever is a mosquito-borne tropical disease caused by the dengue virus. Symptoms typically begin three to fourteen days after infection.[2] These may include a high fever, headache, vomiting, muscle and joint pains, and a characteristic skin rash. Recovery generally takes two to seven days. In a small proportion of cases, the disease develops into severe dengue, also known as dengue hemorrhagic fever, resulting in bleeding, low levels of blood platelets and blood plasma leakage, or into dengue shock syndrome, where dangerously low blood pressure occurs.");

        title.add("Football");
        content.add("Football is a family of team sports that involve, to varying degrees, kicking a ball to score a goal. Unqualified, the word football normally means the form of football that is the most popular where the word is used. Sports commonly called football include association football (known as soccer in some countries); gridiron football (specifically American football or Canadian football); Australian rules football; rugby football (either rugby union or rugby league); and Gaelic football.");

        adapter = new NotesAdapter(content, title, this, getContext());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void name_of_pos(int pos) {
        startActivity(new Intent(getActivity(), NotesDetailsActivity.class).putExtra("noteTitle", title.get(pos)).putExtra("noteContent", content.get(pos)));
    }
}