package com.wellographics.petbattle.Managers.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wellographics.petbattle.Objects.Battle.Effect;
import com.wellographics.petbattle.R;
import com.wellographics.petbattle.Views.FontView;
import com.wellographics.petbattle.Views.RoundedCornerImageView;

public class EffectAdapter extends BaseAdapter {

    private Activity mActivity;
    public Effect[] mEffects;

    public EffectAdapter(Activity activity, Effect[] effects) {
        mActivity = activity;
        mEffects = effects;
    }

    @Override
    public int getCount() {
        return mEffects.length;
    }

    @Override
    public Effect getItem(int position) {
        return mEffects[position];
    }

    @Override
    public long getItemId(int position) {
        return mEffects[position].getIdentifier();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View itemView = mActivity.getLayoutInflater().inflate(R.layout.item_effect, null);

        RoundedCornerImageView image = (RoundedCornerImageView) itemView.findViewById(R.id.Effect_Picture);
        FontView text = (FontView) itemView.findViewById(R.id.Effect_Text);
        text.setTag(position);

        Effect effect = getItem(position);

        image.setTag(getItemId(position));

        image.setBackgroundResource(effect.getSpell().getIconResource());
        text.setText(effect.getActualDuration());

        return itemView;
    }
}