package com.example.app_02.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.app_02.R;
import com.example.app_02.fragment.MessagesFragment;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.message_container, new MessagesFragment())
                .commitNow();
        }
    }
} 