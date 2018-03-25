package com.blog.chhun.learnblog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewPostActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar newPostActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        newPostActionBar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostActionBar);
        getSupportActionBar().setTitle("Add New Post");
    }
}
