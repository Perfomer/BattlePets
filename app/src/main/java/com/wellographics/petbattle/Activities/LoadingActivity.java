package com.wellographics.petbattle.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.wellographics.petbattle.R;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        for(int i = 0; i < 1000; i++) {
            int a = 100 * 999;
        }
       // startActivity(new Intent(this, BattleActivity.class));
    }

}
