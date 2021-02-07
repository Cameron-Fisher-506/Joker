package za.co.joker.nav;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import za.co.joker.MainActivity;
import za.co.joker.R;
import za.co.joker.objs.Joke;
import za.co.joker.utils.ConstantUtils;
import za.co.joker.utils.DTUtils;
import za.co.joker.utils.FragmentUtils;
import za.co.joker.utils.GeneralUtils;
import za.co.joker.utils.SQLiteUtils;
import za.co.joker.utils.StringUtils;
import za.co.joker.utils.WSCallsUtils;
import za.co.joker.utils.WSCallsUtilsTaskCaller;

public class SearchFrag extends Fragment implements WSCallsUtilsTaskCaller
{

    private final int REQ_CODE_SEARCH = 100;

    private EditText edTxtSearch;
    private ImageButton imgBtnSearch;
    private ListView lvJokes;
    private JokeAdapter jokeAdapter;
    private List<Joke> jokes;

    private SQLiteUtils sqLiteUtils;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search, container, false);

        this.sqLiteUtils = new SQLiteUtils(getContext());

        wireUI(view);

        return view;
    }

    private void setJokeAdapter(List<Joke> jokes)
    {
        this.jokeAdapter = new JokeAdapter(jokes);
        this.lvJokes.setAdapter(this.jokeAdapter);

        addLvJokesListener();
    }

    private void wireUI(View view)
    {
        this.lvJokes = view.findViewById(R.id.lvJokes);

        this.edTxtSearch = (EditText) view.findViewById(R.id.edtxtSearch);
        this.edTxtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    jokeSearch(edTxtSearch.getText().toString());
                }

                return false;
            }
        });

        this.imgBtnSearch = (ImageButton) view.findViewById(R.id.imgBtnSearch);
        this.imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                jokeSearch(edTxtSearch.getText().toString());
            }
        });
    }

    private void viewJoke(Joke joke)
    {
        if(joke != null)
        {
            JSONObject jsonObjectJoke = joke.toJSONObject();

            Bundle bundle = new Bundle();
            bundle.putString("joke", jsonObjectJoke.toString());

            JokeFrag jokeFrag = new JokeFrag();
            jokeFrag.setArguments(bundle);

            FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), jokeFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Chuck Norris", true, false, true, null);
        }else
        {
            GeneralUtils.makeToast(getContext(), "Can not view joke.");
        }

    }

    private void addLvJokesListener()
    {
        this.lvJokes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, final View view, int position, long id) {
                Joke joke = (Joke) adapter.getItemAtPosition(position);
                viewJoke(joke);

            }
        });
    }

    private void jokeSearch(String query)
    {
        if(query != null && !query.equals(""))
        {
            WSCallsUtils.get(this, StringUtils.CHUCKNORRIS_URL + "/jokes/search?query=" + query, REQ_CODE_SEARCH);
        }else
        {
            GeneralUtils.makeToast(getContext(), "Please enter a word to search.");
        }

    }

    private class JokeAdapter extends BaseAdapter {

        private List<Joke> jokes;

        public JokeAdapter(List<Joke> jokes)
        {
            this.jokes = jokes;

        }

        @Override
        public int getCount() {
            return this.jokes.size();
        }

        @Override
        public Object getItem(int position) {
            return this.jokes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView =  getLayoutInflater().inflate(R.layout.search_joke_list_item, null);
            }

            ImageView imgCoverImage = convertView.findViewById(R.id.imgCoverImage);
            Picasso.get()
                    .load(this.jokes.get(position).getUrlIcon())
                    .resize(150, 200)
                    .into(imgCoverImage);
            imgCoverImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewJoke(jokes.get(position));
                }
            });


            TextView txtValue = convertView.findViewById(R.id.txtValue);
            txtValue.setText(this.jokes.get(position).getValue());
            txtValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewJoke(jokes.get(position));
                }
            });

            ImageButton imgBtnFavourite = convertView.findViewById(R.id.imgBtnFavourite);
            imgBtnFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GeneralUtils.makeToast(getContext(), "Added to favourites");
                    Joke joke = jokes.get(position);
                    sqLiteUtils.cacheFavourites(joke.getValue(), joke.getUrl(), joke.getUpdatedAt(), joke.getId(), joke.getUrlIcon(), joke.getCreatedAt());
                }
            });

            return convertView;
        }
    }

    @Override
    public Context getCallingContext() {
        return getContext();
    }

    @Override
    public void taskCompleted(String response, int reqCode, boolean isOffline)
    {

        if(response != null)
        {
            if(reqCode == REQ_CODE_SEARCH)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("result"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if(jsonArray != null && jsonArray.length() > 0)
                        {
                            this.jokes = new ArrayList<>();
                            for(int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jokeJSONObject = jsonArray.getJSONObject(i);
                                Joke joke = new Joke();
                                joke.populate(jokeJSONObject);

                                this.jokes.add(joke);
                            }

                            setJokeAdapter(this.jokes);
                        }else
                        {
                            GeneralUtils.makeToast(getContext(), "No jokes found.");
                        }
                    }else if(jsonObject.has("error"))
                    {
                        if(jsonObject.has("message"))
                        {
                            String message = jsonObject.getString("message");
                            GeneralUtils.makeToast(getContext(), message);
                        }else
                        {
                            GeneralUtils.makeToast(getContext(), "Invalid search.");
                        }
                    }else
                    {
                        GeneralUtils.makeToast(getContext(), "Invalid search.");
                    }
                }catch(JSONException e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: SearchFrag - taskCompleted"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }
        }else
        {
            GeneralUtils.makeToast(getContext(), "No connection");
        }

    }
}
