package za.co.joker.nav;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import za.co.joker.R;
import za.co.joker.objs.Joke;
import za.co.joker.utils.ConstantUtils;
import za.co.joker.utils.DTUtils;

public class JokeFrag extends Fragment
{
    private ImageView imgCoverImage;
    private TextView txtValue;
    private TextView txtCreatedAt;
    private TextView txtUpdatedAt;
    private TextView txtCategories;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_joke, container, false);

        wireUI(view);

        String strjoke = getArguments().getString("joke");
        if(strjoke != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(strjoke);
                Joke joke = new Joke();
                joke.populate(jsonObject);

                display(joke);
            }catch(JSONException e)
            {
                Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                        + "\nMethod: JokeFrag - onCreateView"
                        + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
            }
        }

        return view;
    }

    private void wireUI(View view)
    {
        this.imgCoverImage = (ImageView) view.findViewById(R.id.imgCoverImage);
        this.txtValue = (TextView) view.findViewById(R.id.txtValue);
        this.txtCreatedAt = (TextView) view.findViewById(R.id.txtCreatedAt);
        this.txtUpdatedAt = (TextView) view.findViewById(R.id.txtUpdatedAt);
        this.txtCategories =(TextView) view.findViewById(R.id.txtCategories);
    }

    private void display(Joke joke)
    {
        if(joke != null)
        {
            if(this.imgCoverImage != null)
            {
                Picasso.get()
                        .load(joke.getUrlIcon())
                        .into(this.imgCoverImage);
            }else
            {
                this.imgCoverImage.setBackgroundResource(R.drawable.chucknorris);
            }

            if(joke.getValue() != null)
            {
                this.txtValue.setText(joke.getValue());
            }else
            {
                this.txtValue.append(" --");
            }

            if(joke.getCreatedAt() != null)
            {
                this.txtCreatedAt.append(" " + joke.getCreatedAt());
            }else
            {
                this.txtCreatedAt.append(" --");
            }

            if(joke.getUpdatedAt() != null)
            {
                this.txtUpdatedAt.append(" " + joke.getUpdatedAt());
            }else
            {
                this.txtUpdatedAt.append(" --");
            }

            if(joke.getCategories() != null && joke.getCategories().size() > 0)
            {
                this.txtCategories.setText("");
                for(int i = 0; i < joke.getCategories().size(); i++)
                {
                    this.txtCategories.append(joke.getCategories().get(i).getName().concat(" - "));
                }
            }else
            {
                this.txtCategories.setText("");
            }
        }else
        {
            this.txtValue.setText("--");
            this.txtCreatedAt.append(" --");
            this.txtUpdatedAt.append(" --");
        }
    }
}
