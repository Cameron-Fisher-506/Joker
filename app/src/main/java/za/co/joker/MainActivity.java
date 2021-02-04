package za.co.joker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import za.co.joker.utils.WSCallsUtilsTaskCaller;


public class MainActivity extends AppCompatActivity implements WSCallsUtilsTaskCaller
{
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.settings:
            {


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

    }


    public void setNavIcons(boolean home, boolean menu)
    {
        if(home)
        {

        }else
        {

        }

        if(menu)
        {

        }else
        {

        }
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public Context getCallingContext() {
        return null;
    }

    @Override
    public void taskCompleted(String response, int reqCode, boolean isOffline) {
        if(response != null)
        {

        }
    }
}
