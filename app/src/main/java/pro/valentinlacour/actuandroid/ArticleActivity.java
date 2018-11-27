package pro.valentinlacour.actuandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String id = getIntent().getStringExtra("id");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(MainActivity.API_URI_DOMAIN+"/"+id)
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
                            final JSONObject json = new JSONObject(responseData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView title = findViewById(R.id.titleArticle);
                                    TextView chapo = findViewById(R.id.chapoArticle);
                                    TextView content = findViewById(R.id.contentArticle);
                                    TextView copyrights = findViewById(R.id.copyrights);
                                    try {
                                        title.setText(json.getString("title"));
                                        chapo.setText(json.getString("header"));
                                        content.setText(json.getString("body"));
                                        copyrights.setText("published by " + json.getString("author") + " the " + json.getString("publishDate"));

                                        /*Picasso.get()
                                                .load("http://i.imgur.com/DvpvklR.png")
                                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                                .into(imageView);*/
                                    } catch (JSONException e) {

                                    }
                                }
                            });

                        } catch (JSONException e) {

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
