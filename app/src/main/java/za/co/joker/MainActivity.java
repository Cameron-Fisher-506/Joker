package za.co.joker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import za.co.joker.dialogs.PermissionCallback;
import za.co.joker.nav.HomeFrag;
import za.co.joker.nav.MenuFrag;
import za.co.joker.nav.SearchFrag;
import za.co.joker.utils.DialogUtils;
import za.co.joker.utils.FragmentUtils;
import za.co.joker.utils.GeneralUtils;


public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";

    private ImageButton btnHome;
    private ImageButton btnSearch;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtnHomeListener();
        addBtnSearchListener();
        addBtnMenuListener();

        wireUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    private void addBtnHomeListener()
    {
        this.btnHome = (ImageButton) findViewById(R.id.btnHome);
        this.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(true, false, false);
                HomeFrag homeFrag = new HomeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);
            }
        });
    }

    private void addBtnSearchListener()
    {
        this.btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        this.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, true, false);
                SearchFrag searchFrag = new SearchFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), searchFrag, R.id.fragContainer, getSupportActionBar(), "Search", true, false, true, null);
            }
        });
    }

    private void addBtnMenuListener()
    {
        this.btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        this.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, false, true);
                MenuFrag menuFrag = new MenuFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), menuFrag, R.id.fragContainer, getSupportActionBar(), "Menu", true, false, true, null);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.settings:
            {
                DialogUtils.createAlertPermission(this, "Permissions", "Do you want to enable permissions for Joker?", true, new PermissionCallback() {
                    @Override
                    public void checkPermission(boolean ischeckPermission) {
                        if(ischeckPermission)
                        {
                            GeneralUtils.openAppSettingsScreen(getApplicationContext());
                        }
                    }
                }).show();

                break;
            }
            default:
            {
                //unknown
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void wireUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        setNavIcons(true, false, false);
        HomeFrag homeFrag = new HomeFrag();
        FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);
    }


    public void setNavIcons(boolean home, boolean search, boolean menu)
    {
        if(home)
        {
            this.btnHome.setBackgroundResource(R.drawable.ic_home_blue_24dp);
        }else
        {
            this.btnHome.setBackgroundResource(R.drawable.ic_home_white_24dp);
        }

        if(search)
        {
            this.btnSearch.setBackgroundResource(R.drawable.ic_search_blue_24dp);
        }else
        {
            this.btnSearch.setBackgroundResource(R.drawable.ic_search_white_24dp);
        }

        if(menu)
        {
            this.btnMenu.setBackgroundResource(R.drawable.ic_menu_blue_24dp);
        }else
        {
            this.btnMenu.setBackgroundResource(R.drawable.ic_menu_white_24dp);
        }
    }

}
