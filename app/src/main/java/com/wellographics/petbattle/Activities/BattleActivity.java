package com.wellographics.petbattle.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wellographics.petbattle.Logic.BattleLogic;
import com.wellographics.petbattle.Managers.BattleManager;
import com.wellographics.petbattle.R;

public class BattleActivity extends Activity {

    public static final String IN_STAGE_IDENTIFIER = "STAGE_ID";

    private BattleLogic baBattleLogic;
    private TextView blPlayerStats, blEnemyStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        blPlayerStats = (TextView) findViewById(R.id.playerStats);
        blEnemyStats = (TextView) findViewById(R.id.enemyStats);

        baBattleLogic = new BattleLogic(this, getIntent().getIntExtra(IN_STAGE_IDENTIFIER, 0), new BattleManager(this));
    }

    public void onSpellButtonClick(View v) {
        baBattleLogic.useSpell(Integer.parseInt((String)v.getTag()));
    }

    public TextView getStatsView(boolean enemy) {
        if(enemy) return blEnemyStats;
        return blPlayerStats;
    }

    public void onTestClick(View v) {
        baBattleLogic.testButtonDEBUG();
    }

}
