package com.example.finqu.Helper;

import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SkeletonLoadingHelper {
    public static void StartSkeletonLoadingAnimation(View containerView, View shimmerLeft, View shimmerRight, int duration) {
        containerView.post(new Runnable() {
            @Override
            public void run() {
                int distance = containerView.getWidth();

                shimmerLeft.post(new Runnable() {
                    @Override
                    public void run() {
                        int shimmerWidth = shimmerLeft.getWidth() * 2;

                        AnimateSkeletonLoading(shimmerLeft, distance + shimmerWidth, duration);
                        AnimateSkeletonLoading(shimmerRight, distance + shimmerWidth, duration);
                    }
                });
            }
        });
    }

    private static void AnimateSkeletonLoading(View shimmerImg, int toX, int duration) {
        shimmerImg.animate().translationX(toX).setDuration(duration).setInterpolator(new LinearInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                shimmerImg.setTranslationX(0);
                AnimateSkeletonLoading(shimmerImg, toX, duration);
            }
        });
    }
}
