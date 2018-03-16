package com.company.zicure.baseapplication.models;

/**
 * Created by macintosh on 16/3/18.
 */

public class AnimationItem {
    private final String mName;
    private final int mResourceId;

    public AnimationItem(String name, int resourceId) {
        mName = name;
        mResourceId = resourceId;
    }

    public String getName() {
        return mName;
    }

    public int getResourceId() {
        return mResourceId;
    }
}
