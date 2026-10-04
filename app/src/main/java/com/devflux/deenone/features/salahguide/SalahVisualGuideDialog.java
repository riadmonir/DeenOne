package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.core.ui.loading.DeenOneLoadingDrawable;
import com.devflux.deenone.databinding.PageSalahVisualGuideBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

/**
 * Fullscreen Interactive Visual Salah Guide Dialog.
 * Powered by Glide with automatic disk caching and smart vertical cropping.
 */
public class SalahVisualGuideDialog {

    private static boolean isMaleSelected = true;
    private static int currentStepIndex = 0;

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahVisualGuideBinding binding = PageSalahVisualGuideBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        currentStepIndex = 0;
        isMaleSelected = true;

        binding.btnBackVisualGuide.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackVisualGuide);

        binding.tabGenderMale.setOnClickListener(v -> {
            if (!isMaleSelected) {
                isMaleSelected = true;
                currentStepIndex = 0;
                updateGenderTabs(binding, activity);
                renderStep(binding, activity);
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.tabGenderMale);

        binding.tabGenderFemale.setOnClickListener(v -> {
            if (isMaleSelected) {
                isMaleSelected = false;
                currentStepIndex = 0;
                updateGenderTabs(binding, activity);
                renderStep(binding, activity);
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.tabGenderFemale);

        binding.btnPrevStep.setOnClickListener(v -> {
            if (currentStepIndex > 0) {
                currentStepIndex--;
                renderStep(binding, activity);
                binding.scrollVisualContent.smoothScrollTo(0, 0);
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnPrevStep);

        binding.btnNextStep.setOnClickListener(v -> {
            List<SalahVisualGuideManager.StepItem> steps = isMaleSelected
                    ? SalahVisualGuideManager.getMaleSteps(activity)
                    : SalahVisualGuideManager.getFemaleSteps(activity);
            if (currentStepIndex < steps.size() - 1) {
                currentStepIndex++;
                renderStep(binding, activity);
                binding.scrollVisualContent.smoothScrollTo(0, 0);
            } else {
                dialog.dismiss();
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnNextStep);

        updateGenderTabs(binding, activity);
        renderStep(binding, activity);

        binding.ivStepPhoto.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (right - left != oldRight - oldLeft || bottom - top != oldBottom - oldTop) {
                List<SalahVisualGuideManager.StepItem> steps = isMaleSelected
                        ? SalahVisualGuideManager.getMaleSteps(activity)
                        : SalahVisualGuideManager.getFemaleSteps(activity);
                if (!steps.isEmpty() && currentStepIndex >= 0 && currentStepIndex < steps.size()) {
                    SalahVisualGuideManager.StepItem step = steps.get(currentStepIndex);
                    Drawable d = binding.ivStepPhoto.getDrawable();
                    if (d != null) {
                        applySmartCropMatrix(binding.ivStepPhoto, d, step.verticalBias);
                    }
                }
            }
        });

        dialog.show();
    }

    private static void updateGenderTabs(PageSalahVisualGuideBinding binding, Activity activity) {
        boolean isBn = LocaleManager.isBengali(activity);
        binding.tabGenderMale.setText(isBn ? "পুরুষের নামাজ" : "Men's Prayer");
        binding.tabGenderFemale.setText(isBn ? "মহিলাদের নামাজ" : "Women's Prayer");

        if (isMaleSelected) {
            binding.tabGenderMale.setBackgroundResource(R.drawable.bg_tab_visual_active);
            binding.tabGenderMale.setTextColor(Color.WHITE);
            binding.tabGenderMale.setTypeface(null, android.graphics.Typeface.BOLD);

            binding.tabGenderFemale.setBackgroundResource(R.drawable.bg_tab_visual_inactive);
            binding.tabGenderFemale.setTextColor(ContextCompat.getColor(activity, R.color.text_secondary));
            binding.tabGenderFemale.setTypeface(null, android.graphics.Typeface.NORMAL);
        } else {
            binding.tabGenderFemale.setBackgroundResource(R.drawable.bg_tab_visual_active);
            binding.tabGenderFemale.setTextColor(Color.WHITE);
            binding.tabGenderFemale.setTypeface(null, android.graphics.Typeface.BOLD);

            binding.tabGenderMale.setBackgroundResource(R.drawable.bg_tab_visual_inactive);
            binding.tabGenderMale.setTextColor(ContextCompat.getColor(activity, R.color.text_secondary));
            binding.tabGenderMale.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
    }

    private static void renderStep(PageSalahVisualGuideBinding binding, Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);
        List<SalahVisualGuideManager.StepItem> steps = isMaleSelected
                ? SalahVisualGuideManager.getMaleSteps(activity)
                : SalahVisualGuideManager.getFemaleSteps(activity);

        if (currentStepIndex < 0) currentStepIndex = 0;
        if (currentStepIndex >= steps.size()) currentStepIndex = steps.size() - 1;

        SalahVisualGuideManager.StepItem step = steps.get(currentStepIndex);

        String genderTitle = isMaleSelected
                ? (isBn ? "পুরুষের নামাজ" : "Men's Prayer")
                : (isBn ? "মহিলাদের নামাজ" : "Women's Prayer");
        String stepText = isBn
                ? ("ধাপ " + BengaliNumberUtil.toBengali(currentStepIndex + 1))
                : ("Step " + (currentStepIndex + 1));
        binding.tvHeaderTitle.setText(genderTitle + " - " + stepText);

        // Load Image dynamically via Glide with DiskCacheStrategy.ALL and DeenOne Syncing loader
        if (step.imageUrl != null && !step.imageUrl.isEmpty()) {
            if (binding.loadingVisualStep != null) {
                binding.loadingVisualStep.show();
            }
            com.devflux.deenone.core.ui.loading.DeenOneLoadingDrawable placeholderDrawable =
                    new com.devflux.deenone.core.ui.loading.DeenOneLoadingDrawable(activity, true, true);

            Glide.with(activity)
                    .load(step.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholderDrawable)
                    .error(R.drawable.bg_card_secondary)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            if (binding.loadingVisualStep != null) {
                                binding.loadingVisualStep.hide();
                            }
                            binding.ivStepPhoto.setImageDrawable(resource);
                            applySmartCropMatrix(binding.ivStepPhoto, resource, step.verticalBias);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            binding.ivStepPhoto.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            if (binding.loadingVisualStep != null) {
                                binding.loadingVisualStep.hide();
                            }
                        }
                    });
        }

        binding.tvStepBadge.setText(isBn
                ? (stepText + " / " + BengaliNumberUtil.toBengali(steps.size()))
                : (stepText + " / " + steps.size()));

        binding.tvCardInstructionTitle.setText(isBn ? "📌 নিয়ম ও করণীয়" : "📌 Rules & Posture");
        binding.tvCardDuaTitle.setText(isBn ? "📖 পঠিতব্য দোয়া ও তাসবীহ" : "📖 Recited Dua & Tasbeeh");
        binding.tvCardHadithTitle.setText(isBn ? "💡 অর্থ ও জরুরি তথ্য" : "💡 Meaning & Context");

        binding.tvStepDescription.setText(step.description);

        if (step.dua != null && !step.dua.trim().isEmpty()) {
            binding.cardDua.setVisibility(View.VISIBLE);
            binding.tvStepDua.setText(step.dua);
        } else {
            binding.cardDua.setVisibility(View.GONE);
        }

        if (step.notes != null && !step.notes.trim().isEmpty()) {
            binding.cardHadith.setVisibility(View.VISIBLE);
            binding.tvStepHadith.setText(step.notes);
        } else {
            binding.cardHadith.setVisibility(View.GONE);
        }

        if (currentStepIndex == 0) {
            binding.btnPrevStep.setVisibility(View.INVISIBLE);
        } else {
            binding.btnPrevStep.setVisibility(View.VISIBLE);
            binding.tvPrevText.setText(isBn ? "পূর্ববর্তী ধাপ" : "Previous Step");
        }

        if (currentStepIndex == steps.size() - 1) {
            binding.tvNextText.setText(isBn ? "সমাপ্ত" : "Finish");
            binding.ivNextIcon.setImageResource(R.drawable.ic_check_circle);
            binding.tvNextText.setTextColor(ContextCompat.getColor(activity, R.color.accent_teal));
            binding.ivNextIcon.setColorFilter(ContextCompat.getColor(activity, R.color.accent_teal));
        } else {
            binding.tvNextText.setText(isBn ? "পরবর্তী ধাপ" : "Next Step");
            binding.ivNextIcon.setImageResource(R.drawable.ic_chevron_right);
            int teal = ContextCompat.getColor(activity, R.color.accent_teal);
            binding.tvNextText.setTextColor(teal);
            binding.ivNextIcon.setColorFilter(teal);
        }
    }

    /**
     * Applies high-accuracy smart vertical crop using matrix transformation.
     */
    private static void applySmartCropMatrix(ImageView imageView, Drawable drawable, float verticalBias) {
        if (imageView == null || drawable == null) return;

        Runnable updateMatrix = () -> {
            int vWidth = imageView.getWidth();
            int vHeight = imageView.getHeight();
            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();

            if (vWidth <= 0 || vHeight <= 0 || dWidth <= 0 || dHeight <= 0) {
                return;
            }

            float scale;
            float dx = 0f;
            float dy;

            if (dWidth * vHeight > vWidth * dHeight) {
                scale = (float) vHeight / (float) dHeight;
                dx = (vWidth - dWidth * scale) * 0.5f;
                dy = 0f;
            } else {
                scale = (float) vWidth / (float) dWidth;
                float diffY = vHeight - dHeight * scale;
                dy = diffY * verticalBias;
            }

            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            imageView.setImageMatrix(matrix);
        };

        if (imageView.getWidth() > 0 && imageView.getHeight() > 0) {
            updateMatrix.run();
        } else {
            imageView.post(updateMatrix);
        }
    }
}
