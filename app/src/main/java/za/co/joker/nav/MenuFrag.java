package za.co.joker.nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import za.co.joker.MainActivity;
import za.co.joker.R;
import za.co.joker.utils.FragmentUtils;

public class MenuFrag extends Fragment
{
    private LinearLayoutCompat favouritesOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu, container, false);

        favouritesOptionListener(view);

        return view;
    }

    private void favouritesOptionListener(View view)
    {
        this.favouritesOption = (LinearLayoutCompat) view.findViewById(R.id.linearLayoutFavouritesOption);
        this.favouritesOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FavouritesFrag favouritesFrag = new FavouritesFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), favouritesFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Favourites",true, false, true, null);
            }
        });
    }
}
