package com.discovertodo.phone.android.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoViewUtil extends VideoView {

    private PlayPauseListener mListener;

    public CustomVideoViewUtil(Context context) {
        super(context);
    }

    public CustomVideoViewUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoViewUtil(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }

    @Override
    public void pause() {
        super.pause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void start() {
        super.start();
        if (mListener != null) {
            mListener.onPlay();
        }
    }

    public static interface PlayPauseListener {
        void onPlay();
        void onPause();
    }

}

