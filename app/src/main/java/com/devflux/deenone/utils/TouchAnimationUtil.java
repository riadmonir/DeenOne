package com.devflux.deenone.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Build;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.Locale;

/**
 * High-performance, lightweight touch animation utility.
 *
 * MANDATORY PROJECT RULE (AGENTS.md & GEMINI.md Rule 7):
 * - টাচ অ্যানিমেশন শুধুমাত্র এবং একান্তভাবেই বাটনের (Buttons) মধ্যে সীমাবদ্ধ থাকবে।
 * - কোনো কার্ড ভিউ (CardView / Content Card / তালিকা আইটেম কার্ড / ফিচার কার্ড)-এ কোনো টাচ অ্যানিমেশন থাকা সম্পূর্ণ নিষিদ্ধ।
 */
public final class TouchAnimationUtil {

  private TouchAnimationUtil() {
    // Utility class
  }

  /**
   * Attaches a subtle dynamic scale-down on press and spring-up on release/cancel via native StateListAnimator.
   * STRICTLY PERMITTED ONLY ON BUTTONS.
   * If the view is a CardView or card container, touch animation is strictly skipped.
   */
  public static void attachTouchSpring(View view) {
    if (view == null) return;

    // Strict Mandatory Rule: Zero touch animation on any card view or card container
    if (isCardView(view)) {
      return;
    }

    view.setClickable(true);
    view.setFocusable(true);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      StateListAnimator stateListAnimator = new StateListAnimator();

      // Pressed state: scale down to 0.95f smoothly in 70ms
      AnimatorSet pressedSet = new AnimatorSet();
      ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(view, "scaleX", 0.95f).setDuration(70);
      ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(view, "scaleY", 0.95f).setDuration(70);
      pressedSet.playTogether(scaleXDown, scaleYDown);
      stateListAnimator.addState(new int[]{android.R.attr.state_pressed}, pressedSet);

      // Released / Default state: spring back to 1.0f with subtle overshoot
      AnimatorSet normalSet = new AnimatorSet();
      ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(view, "scaleX", 1.0f).setDuration(140);
      ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(view, "scaleY", 1.0f).setDuration(140);
      scaleXUp.setInterpolator(new OvershootInterpolator(1.5f));
      scaleYUp.setInterpolator(new OvershootInterpolator(1.5f));
      normalSet.playTogether(scaleXUp, scaleYUp);
      stateListAnimator.addState(new int[]{}, normalSet);

      view.setStateListAnimator(stateListAnimator);
    }
  }

  /**
   * Checks whether the provided view is a CardView or card-based container.
   * Returns true if the view is a card, in which case touch animation is strictly blocked.
   */
  public static boolean isCardView(View view) {
    if (view == null) return false;

    // 1. AndroidX CardView check
    if (view instanceof androidx.cardview.widget.CardView) {
      return true;
    }

    // 2. Class name check
    String className = view.getClass().getName().toLowerCase(Locale.US);
    if (className.contains("cardview") || className.endsWith("card")) {
      return true;
    }

    // 3. Resource ID entry name check
    try {
      int id = view.getId();
      if (id != View.NO_ID && view.getResources() != null) {
        String entryName = view.getResources().getResourceEntryName(id).toLowerCase(Locale.US);
        // Exclude if entry name designates a card container, card view, or list item root
        if (entryName.contains("card")
            || (entryName.startsWith("item_") && !entryName.contains("btn") && !entryName.contains("button") && !entryName.contains("icon"))
            || (entryName.startsWith("item") && !entryName.contains("btn") && !entryName.contains("button") && !entryName.contains("icon"))) {
          return true;
        }
      }
    } catch (Exception ignored) {
    }

    return false;
  }

  /**
   * Applies a one-shot bounce micro-animation on view click (buttons only).
   */
  public static void animateBounce(View view) {
    if (view == null || isCardView(view)) return;
    view.animate()
        .scaleX(0.92f)
        .scaleY(0.92f)
        .setDuration(80)
        .withEndAction(() ->
            view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(140)
                .setInterpolator(new OvershootInterpolator(1.6f))
                .start()
        )
        .start();
  }
}


