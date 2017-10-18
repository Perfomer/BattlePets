package com.wellographics.petbattle.Objects.Battle;

import android.content.Context;

import com.wellographics.petbattle.R;

public class Location {
    
    public static final float PROTAGONIST_COEFF = 0.125f;
    private Context lContext;
    private int lIdentifier;

    public Location(Context context, int locationIdentifier) {
        lContext = context;
        lIdentifier = locationIdentifier;
    }

    public int getLocationProtagonistType() {
        return lContext.getResources().getIntArray(R.array.ProtagonistLocation)[lIdentifier];
    }
}
