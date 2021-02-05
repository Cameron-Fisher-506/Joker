package za.co.joker.nav;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import za.co.joker.MainActivity;
import za.co.joker.R;
import za.co.joker.adapters.JokeAdapter;
import za.co.joker.objs.Joke;
import za.co.joker.utils.ConstantUtils;
import za.co.joker.utils.DTUtils;
import za.co.joker.utils.GeneralUtils;
import za.co.joker.utils.StringUtils;
import za.co.joker.utils.WSCallsUtils;
import za.co.joker.utils.WSCallsUtilsTaskCaller;

public class HomeFrag extends Fragment implements WSCallsUtilsTaskCaller
{
    private int REQ_CODE_GET_RANDOM_JOKE = 100;

    private TextView txtNotify;
    private Button btnMore;

    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private JokeAdapter jokeAdapter;

    private List<Joke> jokes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        this.jokeAdapter = new JokeAdapter();

        wireUI(view);
        initJokeAdapter(view);
        addBtnMoreListener(view);

        return view;
    }

    private void initJokeAdapter(View view)
    {
        this.layoutManager = new GridLayoutManager(getContext(), 1);
        this.layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        this.recyclerView = view.findViewById(R.id.jokeRecyclerView);
        this.recyclerView.setLayoutManager(this.layoutManager);

        //get 10 random jokes to display on home page
        this.jokes = new ArrayList<>();
        //set list of jokes
        loadRandomJokes();

    }

    private void loadRandomJokes()
    {
        for(int i = 0; i < 10; i++)
        {
            WSCallsUtils.get(this, StringUtils.CHUCKNORRIS_PROD_URL + "/jokes/random", REQ_CODE_GET_RANDOM_JOKE);
        }
    }

    private void addBtnMoreListener(View view)
    {
        this.btnMore = view.findViewById(R.id.btnMore);
        this.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.makeToast(getContext(), "Loading more jokes...");
                loadRandomJokes();
            }
        });
    }

    private void wireUI(View view)
    {
        this.txtNotify = (TextView) view.findViewById(R.id.txtNotify);
        this.txtNotify.setVisibility(View.GONE);
    }

    @Override
    public Context getCallingContext() {
        return getContext();
    }

    @Override
    public void taskCompleted(String response, int reqCode, boolean isOffline)
    {
        if(isOffline)
        {
            this.txtNotify.setVisibility(View.VISIBLE);
        }else
        {
            this.txtNotify.setVisibility(View.GONE);
        }

        if(response != null)
        {
            if(reqCode == REQ_CODE_GET_RANDOM_JOKE)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    Joke joke = new Joke();
                    joke.populate(jsonObject);

                    this.jokes.add(joke);
                    Collections.sort(jokes);

                    this.jokeAdapter = new JokeAdapter((MainActivity)getActivity(), jokes);
                    this.recyclerView.setAdapter(this.jokeAdapter);
                }catch(JSONException e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: HomeFrag - taskCompleted"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }

            }
        }else
        {
            //offline
        }

    }
}
