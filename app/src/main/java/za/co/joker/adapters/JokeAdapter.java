package za.co.joker.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import za.co.joker.MainActivity;
import za.co.joker.R;
import za.co.joker.nav.JokeFrag;
import za.co.joker.objs.Joke;
import za.co.joker.utils.FragmentUtils;


import org.json.JSONObject;

import java.util.List;

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder>
{
    private final String TAG = "JokeAdapter";

    private List<Joke> jokes;
    private Activity activity;

    public JokeAdapter()
    {

    }

    public JokeAdapter(Activity activity, List<Joke> jokes)
    {
        this.jokes = jokes;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgCoverImage;
        TextView txtValue;

        public ViewHolder(View view)
        {
            super(view);

            this.imgCoverImage = view.findViewById(R.id.imgCoverImage);
            this.txtValue = view.findViewById(R.id.txtValue);

            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) this.imgCoverImage.getLayoutParams();
            marginParams.setMargins(10, 0, 10, 0);
            this.imgCoverImage.setLayoutParams(marginParams);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_joke_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    private void openJoke(int position)
    {
        JSONObject joke = jokes.get(position).toJSONObject();

        Bundle bundle = new Bundle();
        bundle.putString("joke", joke.toString());

        JokeFrag jokeFrag = new JokeFrag();
        jokeFrag.setArguments(bundle);

        FragmentUtils.startFragment(((MainActivity)activity).getSupportFragmentManager(), jokeFrag, R.id.fragContainer, ((MainActivity)activity).getSupportActionBar(), "Chuck Norris", true, false, true, null);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {

        Picasso.get()
                .load(this.jokes.get(position).getUrlIcon())
                .into(holder.imgCoverImage);

        holder.txtValue.setText(this.jokes.get(position).getValue());
        holder.txtValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJoke(position);
            }
        });

        holder.imgCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openJoke(position);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        int toReturn = 0;

        if(this.jokes != null)
        {
            toReturn = this.jokes.size();
        }
        return toReturn;
    }
}
