package project.mapobed.notbuk.Onboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import project.mapobed.notbuk.R;

public class OnboardAdapter extends PagerAdapter {
    private List<OnboardModelclass> introModelClasses;
    Context context;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.onboard_items,null);
        //declaration
        TextView desc=view.findViewById(R.id.onboard_item_subtitle);
        ImageView image=view.findViewById(R.id.onboard_item_image);
        TextView title=view.findViewById(R.id.onboard_item_title);
        title.setText(introModelClasses.get(position).getTitle());
        desc.setText(introModelClasses.get(position).getDesc());
        image.setImageResource(introModelClasses.get(position).getImage());
        container.addView(view);
        return view;
    }

    public OnboardAdapter(List<OnboardModelclass> introModelClasses, Context context) {
        this.introModelClasses = introModelClasses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return introModelClasses.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
