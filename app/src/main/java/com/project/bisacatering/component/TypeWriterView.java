package com.project.bisacatering.component;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

public class TypeWriterView extends AppCompatTextView {

    private CharSequence mText;
    private String mPrintingText;
    private int mIndex;
    private long mDelay = 100; //Default 500ms delay

    private Context mContext;
    private TypeWriterListener mTypeWriterListener;

    private boolean mWithMusic = true;
    private boolean animating = false;

    private Runnable mBlinker;
    private int i = 0;
    private Handler mHandler = new Handler();
    private Runnable mCharacterAdder = new Runnable() {
        @Override
        public void run() {
            if (animating) {
                setText(mText.subSequence(0, mIndex++));
                //typing typed
                if (mTypeWriterListener != null)
                    mTypeWriterListener.onCharacterTyped(mPrintingText, mIndex);

                if (mIndex <= mText.length()) {
                    mHandler.postDelayed(mCharacterAdder, mDelay);
                } else {
                    //typing end
                    if (mTypeWriterListener != null)
                        mTypeWriterListener.onTypingEnd(mPrintingText);

                    animating = false;
                }
            }
        }
    };

    public TypeWriterView(Context context) {
        super(context);
        mContext = context;
    }

    public TypeWriterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void callBlink() {
        mBlinker = new Runnable() {
            @Override
            public void run() {
                if (i <= 10) {
                    if (i++ % 2 != 0) {
                        setText(String.format("%s _", mText));
                        mHandler.postDelayed(mBlinker, 150);
                    } else {
                        setText(String.format("%s   ", mText));
                        mHandler.postDelayed(mBlinker, 150);
                    }
                } else
                    i = 0;
            }
        };
        mHandler.postDelayed(mBlinker, 150);
    }


    /**
     * Call this function to display
     *
     * @param text attribute
     */
    public void animateText(String text) {
        if (!animating) {
            animating = true;
            mText = text;
            mPrintingText = text;
            mIndex = 0;
            setText("");
            mHandler.removeCallbacks(mCharacterAdder);
            //typing start
            if (mTypeWriterListener != null)
                mTypeWriterListener.onTypingStart(mPrintingText);
            mHandler.postDelayed(mCharacterAdder, mDelay);
        } else {
            //CAUTION: Already typing something..
            Toast.makeText(mContext, "Typewriter busy typing: " + mText, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Call this function to set delay in MILLISECOND [20..150]
     *
     * @param delay
     */
    public void setDelay(int delay) {
        if (delay >= 20 && delay <= 150)
            this.mDelay = delay;
    }

    /**
     * Whether to play music or not while animating
     *
     * @param music
     */
    public void setWithMusic(boolean music) {
        mWithMusic = music;
    }

    /**
     * Call this to remove animation at any time
     */
    public void removeAnimation() {
        mHandler.removeCallbacks(mCharacterAdder);

        animating = false;
        setText(mPrintingText);

        //typing removed
        if (mTypeWriterListener != null)
            mTypeWriterListener.onTypingRemoved(mPrintingText);
    }

    /**
     * Set listener to receive typing effects
     *
     * @param typeWriterListener
     */
    public void setTypeWriterListener(TypeWriterListener typeWriterListener) {
        this.mTypeWriterListener = typeWriterListener;
    }
}
