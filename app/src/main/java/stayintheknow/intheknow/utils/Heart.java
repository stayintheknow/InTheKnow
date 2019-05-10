package stayintheknow.intheknow.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class Heart {
    private static final String TAG = "Heart";

    private static final DecelerateInterpolator DECELERATOR_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    public ImageView heart, heartRed;

    public Heart(ImageView ivHeart, ImageView ivHeartRed) {
        this.heart = ivHeart;
        this.heartRed = ivHeartRed;
    }

    public void toggleLike() {
        Log.d(TAG, "toggleLike: toggling heart");

        AnimatorSet animatorSet = new AnimatorSet();

        if(heartRed.getVisibility() == View.VISIBLE) {
            Log.d(TAG, "toggleLike: toggling red heart off");
            heartRed.setScaleX(0.1f);
            heartRed.setScaleY(0.1f);

            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(heartRed, "scaleY", 1f, 0f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(heartRed, "scaleX", 1f, 0f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

            heartRed.setVisibility(View.GONE);
            heart.setVisibility(View.VISIBLE);

            animatorSet.playTogether(scaleDownY, scaleDownX);
        }

        else if(heartRed.getVisibility() == View.GONE) {
            Log.d(TAG, "toggleLike: toggling red heart on");
            heartRed.setScaleX(0.1f);
            heartRed.setScaleY(0.1f);

            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(heartRed, "scaleY", 0.1f, 1f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(DECELERATOR_INTERPOLATOR);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(heartRed, "scaleX", 0.1f, 1f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(DECELERATOR_INTERPOLATOR);

            heartRed.setVisibility(View.VISIBLE);
            heart.setVisibility(View.GONE);

            animatorSet.playTogether(scaleDownY, scaleDownX);
        }

        animatorSet.start();
    }
}
