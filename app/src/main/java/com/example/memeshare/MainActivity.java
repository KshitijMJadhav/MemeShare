package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button share,next;
    ImageView meme;
    ProgressBar progressBar;
    String currentUrl = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        share = findViewById(R.id.sharebutton);
        next = findViewById(R.id.nextbutton);
        meme = findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressbar);
    }

    private void loadmeme(){
        // Instantiate the RequestQueue.

        String url ="https://meme-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            currentUrl = response.getString("url");
                            progressBar.setVisibility(View.VISIBLE);
                            Glide.with(MainActivity.this)
                                    .load(currentUrl)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                            progressBar.setVisibility(View.GONE);

                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            progressBar.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })
                                    .into(meme);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
                MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void sharememe(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("Text/Plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! , Check out this cool meme " + currentUrl);
        Intent chooserTarget = Intent.createChooser(intent, "Share this with");
        startActivity(chooserTarget);

    }

    public void nextmeme(View view) {
        loadmeme();
    }
}