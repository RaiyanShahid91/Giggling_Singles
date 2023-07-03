package com.dil.singles.helper;

import androidx.recyclerview.widget.DiffUtil;

import com.dil.singles.fragments.home.HomeLandingModel;

import java.util.ArrayList;
import java.util.Objects;

public class CardStackCallback extends DiffUtil.Callback {

    private ArrayList<HomeLandingModel> old, baru;

    public CardStackCallback(ArrayList<HomeLandingModel> old, ArrayList<HomeLandingModel> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(old.get(oldItemPosition).getFirstPhoto(), baru.get(newItemPosition).getFirstPhoto());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}
