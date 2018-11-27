package pro.valentinlacour.actuandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListFragment.OnFragmentInteractionListener {

    public static final String API_URI_DOMAIN = "http://api-android.valentin-lacour.pro/public/index.php";
    public static final String SHARED_PREFERENCE_PATH = "com.example.android.hellosharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences mPreferences = getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE);

        //mPreferences.edit().clear().apply();

        if (mPreferences.getString("USER-TOKEN", null) == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.generateList(null);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            this.generateList(null);
        } else if (id == R.id.nav_economy) {
            this.generateList("economy");
        } else if (id == R.id.nav_politic) {
            this.generateList("politic");
        } else if (id == R.id.nav_high_tech) {
            this.generateList("high-tech");
        }

        SharedPreferences mPreferences = getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE);
        TextView navigationName = findViewById(R.id.navigationName);
        navigationName.setText(mPreferences.getString("Name", "Anonymus User"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void generateList(String theme)
    {
        String url = API_URI_DOMAIN + '/';

        if (theme != null) {
            url += theme;
        }

        SharedPreferences mPreferences = getSharedPreferences(SHARED_PREFERENCE_PATH, MODE_PRIVATE);

        if (mPreferences.getString("USER-TOKEN", null) == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization-token", mPreferences.getString("USER-TOKEN", ""))
                .build();
        client
                .newCall(request)
                .enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        try {
                            String responseData = response.body().string();
                            final JSONArray json = new JSONArray(responseData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView recyclerView = findViewById(R.id.recyclerview);
                                    ArticleAdapter articleAdapter = new ArticleAdapter(getApplicationContext(), json);
                                    recyclerView.setAdapter(articleAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                }
                            });

                        } catch (JSONException e) {
                            Log.d("TEST21", e.getMessage());
                        }
                    }
                });

    }
}
