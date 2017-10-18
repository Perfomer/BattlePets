package com.wellographics.petbattle.Preferences.Data;

import android.content.Context;
import android.content.SharedPreferences;

class DataOperator {

    public final static char SEPARATOR = '#', SUBSEPARATOR = ';', MARKER = '!';
    public final static String
            STATS_MONEY = "Money", STATS_LEVEL = "Level", PETS_ACTUAL = "ActualPets",
            STAGES_COMPLETED = "CompletedStages";
    protected final String PREFERENCES_NAME = "Preferences";

    protected Context pContext;
    protected SharedPreferences pDataOperator;

    protected DataOperator(Context context) {
        pContext = context;
        pDataOperator = pContext.getSharedPreferences(PREFERENCES_NAME, pContext.MODE_PRIVATE);
    }
}