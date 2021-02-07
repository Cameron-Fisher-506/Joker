package za.co.joker.nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import za.co.joker.MainActivity;
import za.co.joker.R;
import za.co.joker.objs.Joke;
import za.co.joker.utils.FragmentUtils;
import za.co.joker.utils.GeneralUtils;
import za.co.joker.utils.SQLiteUtils;

public class FavouritesFrag extends Fragment
{
    private ListView lvFavourites;
    private List<Joke> jokes;
    private FavouritesAdapter favouritesAdapter;

    private SQLiteUtils sqLiteUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_favourites, container, false);

        this.sqLiteUtils = new SQLiteUtils(getContext());

        wireUI(view);

        return view;
    }

    private void wireUI(View view)
    {
        this.lvFavourites = (ListView) view.findViewById(R.id.lvFavourties);

        List<Joke> jokes = this.sqLiteUtils.getAllFavourites();
        if(jokes != null)
        {
            setFavouritesAdapter(jokes);
            setLvFavourites(this.favouritesAdapter);
        }

        addLvFavouritesListener();

    }

    private void addLvFavouritesListener()
    {
        this.lvFavourites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int index, long id) {

                Joke joke = (Joke) adapter.getItemAtPosition(index);
                JSONObject jsonObjectJoke = joke.toJSONObject();

                if (jsonObjectJoke != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("joke", jsonObjectJoke.toString());

                    JokeFrag jokeFrag = new JokeFrag();
                    jokeFrag.setArguments(bundle);

                    FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), jokeFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Chuck Norris", true, false, true, null);
                }else
                {
                    GeneralUtils.makeToast(getContext(), "Favourite joke not available!");
                }
            }
        });
    }


    private class FavouritesAdapter extends BaseAdapter
    {
        private List<Joke> jokes;

        private ImageView imgCoverImage;
        private TextView txtValue;

        public FavouritesAdapter(List<Joke> jokes)
        {
            this.jokes = jokes;
        }

        @Override
        public int getCount() {

            int toReturn = 0;

            if(this.jokes != null)
            {
                toReturn = this.jokes.size();
            }

            return toReturn;
        }

        @Override
        public Joke getItem(int index) {

            Joke toReturn = null;

            if(this.jokes != null )
            {
                toReturn = this.jokes.get(index);
            }

            return toReturn;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.favourites_list_item, null);
            }

            wireUI(convertView, position);

            return convertView;
        }

        private void wireUI(View view, final int index)
        {
            ImageView imgCoverImage = view.findViewById(R.id.imgCoverImage);
            Picasso.get()
                    .load(this.jokes.get(index).getUrlIcon())
                    .resize(150, 200)
                    .into(imgCoverImage);

            this.txtValue = (TextView) view.findViewById(R.id.txtValue);
            this.txtValue.setText(this.jokes.get(index).getValue());
        }
    }


    public void setFavouritesAdapter(List<Joke> jokes) {
        this.favouritesAdapter = new FavouritesAdapter(jokes);
    }

    public void setLvFavourites(FavouritesAdapter favouritesAdapter) {
        this.lvFavourites.setAdapter(favouritesAdapter);
    }

    public List<Joke> getPositions() {
        return jokes;
    }

    public void setPositions(List<Joke> jokes) {
        this.jokes = jokes;
    }

    public FavouritesAdapter getFavouritesAdapter() {
        return favouritesAdapter;
    }

    public ListView getLvFavourites() {
        return lvFavourites;
    }
}
