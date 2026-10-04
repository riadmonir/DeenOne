package com.devflux.deenone.features.community;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.auth.AuthDialogManager;
import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetCommunityCommentsBinding;
import com.devflux.deenone.databinding.BottomSheetCreatePostBinding;
import com.devflux.deenone.databinding.BottomSheetEditPostBinding;
import com.devflux.deenone.databinding.PageCommunityFeedBinding;
import com.devflux.deenone.features.community.adapter.CommunityCommentAdapter;
import com.devflux.deenone.features.community.adapter.CommunityPostAdapter;
import com.devflux.deenone.features.community.data.CommunityRepository;
import com.devflux.deenone.features.community.model.CommunityCommentItem;
import com.devflux.deenone.features.community.model.CommunityPostItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CommunityFeedDialog {

  public static void show(@NonNull Context context) {
    show(context, null, false);
  }

  public static void show(@NonNull Context context, String targetPostId, boolean openComments) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    PageCommunityFeedBinding binding = PageCommunityFeedBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    binding.tvCommunityTopTitle.setText(isBn ? "কমিউনিটি হাব" : "Community Hub");
    if (binding.tvCommunityPillTag != null) {
      binding.tvCommunityPillTag.setText(isBn ? "উৎসাহের বন্ধন" : "Bond of Encouragement");
    }
    if (binding.tvCommunityHeroTitle != null) {
      binding.tvCommunityHeroTitle.setText(isBn ? "কমিউনিটি হাব" : "Community Hub");
    }
    if (binding.tvCommunityHeroSubtitle != null) {
      binding.tvCommunityHeroSubtitle.setText(isBn ? "একসাথে শিখুন, একসাথে বেড়ে উঠুন" : "Learn together, grow together");
    }
    binding.btnCreatePostTop.setText(isBn ? "+ নতুন পোস্ট" : "+ New Post");

    if (binding.tvSecurityWarningTitle != null) {
      binding.tvSecurityWarningTitle.setText(isBn ? "অনলাইন নিরাপত্তা অনুস্মারক" : "Online Safety Advisory");
    }
    if (binding.tvSecurityWarningDescription != null) {
      binding.tvSecurityWarningDescription.setText(isBn
          ? "অনলাইনে অপরিচিত ব্যক্তিদের সাথে যোগাযোগের বাস্তব ঝুঁকি সম্পর্কে সচেতন থাকুন। কখনই নিজের ব্যক্তিগত তথ্য (যেমন: ফোন নম্বর, ইমেল বা ঠিকানা) শেয়ার করবেন না।"
          : "Be aware of the real risks of communicating with strangers online. Never share your personal information (such as phone number, email, or address).");
    }

    CommunityRepository repo = CommunityRepository.getInstance(context);

    final String[] currentCategory = {"all"};

    // Adapter
    final CommunityPostAdapter[] adapterHolder = new CommunityPostAdapter[1];

    CommunityPostAdapter adapter = new CommunityPostAdapter(new CommunityPostAdapter.OnPostActionListener() {
      @Override
      public void onAmeenClick(CommunityPostItem post, int position) {
        repo.toggleAmeen(post);
        dialog.findViewById(R.id.rvCommunityPosts).post(() -> {
          if (binding.rvCommunityPosts.getAdapter() != null) {
            binding.rvCommunityPosts.getAdapter().notifyItemChanged(position);
          }
        });
      }

      @Override
      public void onCommentsClick(CommunityPostItem post, int position) {
        showCommentsDialog(context, post, () -> {
          if (binding.rvCommunityPosts.getAdapter() != null) {
            binding.rvCommunityPosts.getAdapter().notifyItemChanged(position);
          }
        });
      }

      @Override
      public void onShareClick(CommunityPostItem post, int position) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, isBn ? ("উম্মাহ কমিউনিটি: " + post.getTitle()) : ("Ummah Community: " + post.getTitle()));
        shareIntent.putExtra(Intent.EXTRA_TEXT, (isBn ? "উম্মাহ কমিউনিটি পোস্ট:\n\n" : "Ummah Community Post:\n\n") + post.getTitle() + "\n\n" + post.getContent() + "\n\n— " + post.getAuthorName() + (isBn ? " (দ্বীনওয়ান অ্যাপ)" : " (DeenOne App)"));
        context.startActivity(Intent.createChooser(shareIntent, isBn ? "পোস্ট শেয়ার করুন" : "Share Post"));
      }

      @Override
      public void onEditClick(CommunityPostItem post, int position) {
        showEditPostSheet(context, repo, post, () -> {
          List<CommunityPostItem> posts = repo.getPostsByCategory(currentCategory[0]);
          if (adapterHolder[0] != null) adapterHolder[0].submitList(posts);
        });
      }

      @Override
      public void onDeleteClick(CommunityPostItem post, int position) {
        new AlertDialog.Builder(context)
            .setTitle(isBn ? "পোস্ট মুছে ফেলবেন?" : "Delete Post?")
            .setMessage(isBn ? "আপনি কি নিশ্চিতভাবে এই পোস্টটি মুছে ফেলতে চান?" : "Are you sure you want to delete this post?")
            .setPositiveButton(isBn ? "মুছে ফেলুন" : "Delete", (d, w) -> {
              repo.deletePost(post.getId());
              List<CommunityPostItem> posts = repo.getPostsByCategory(currentCategory[0]);
              if (adapterHolder[0] != null) adapterHolder[0].submitList(posts);
            })
            .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
            .show();
      }

      @Override
      public void onReportClick(CommunityPostItem post, int position) {
        showReportPostDialog(context, repo, post);
      }

      @Override
      public void onPostDeleted(CommunityPostItem post, int position) {
        List<CommunityPostItem> posts = repo.getPostsByCategory(currentCategory[0]);
        if (adapterHolder[0] != null) {
          adapterHolder[0].submitList(posts);
        }
      }
    });

    adapterHolder[0] = adapter;

    binding.rvCommunityPosts.setLayoutManager(new LinearLayoutManager(context));
    binding.rvCommunityPosts.setAdapter(adapter);

    Runnable refreshList = () -> {
      List<CommunityPostItem> posts = repo.getPostsByCategory(currentCategory[0]);
      adapter.submitList(posts);

      // Handle Direct Deep-link to Specific Target Post
      if (targetPostId != null && !targetPostId.trim().isEmpty() && !posts.isEmpty()) {
        int targetIdx = -1;
        CommunityPostItem targetPost = null;
        String cleanTarget = targetPostId.trim();
        for (int i = 0; i < posts.size(); i++) {
          CommunityPostItem p = posts.get(i);
          if (p.getId().equalsIgnoreCase(cleanTarget)
              || p.getId().equalsIgnoreCase("p_" + cleanTarget)
              || cleanTarget.equalsIgnoreCase("p_" + p.getId())
              || cleanTarget.contains(p.getId())
              || (p.getTitle() != null && !p.getTitle().isEmpty() && cleanTarget.contains(p.getTitle()))) {
            targetIdx = i;
            targetPost = p;
            break;
          }
        }
        if (targetIdx >= 0) {
          final int scrollPos = targetIdx;
          final CommunityPostItem postForComment = targetPost;
          binding.rvCommunityPosts.post(() -> {
            RecyclerView.LayoutManager lm = binding.rvCommunityPosts.getLayoutManager();
            if (lm instanceof LinearLayoutManager) {
              ((LinearLayoutManager) lm).scrollToPositionWithOffset(scrollPos, 0);
            } else {
              binding.rvCommunityPosts.scrollToPosition(scrollPos);
            }
            if (openComments && postForComment != null) {
              binding.rvCommunityPosts.postDelayed(() -> showCommentsDialog(context, postForComment, () -> {
                if (binding.rvCommunityPosts.getAdapter() != null) {
                  binding.rvCommunityPosts.getAdapter().notifyItemChanged(scrollPos);
                }
              }), 250);
            }
          });
        }
      }
    };

    refreshList.run();

    // Background auto-sync from remote server (fetches live posts, updates like & comment counts)
    repo.syncFromRemote(true, () -> {
      if (binding.rvCommunityPosts != null) {
        binding.rvCommunityPosts.post(refreshList);
      }
    });

    // Category Filter Chips
    TextView[] chips = {binding.chipCatAll, binding.chipCatDua, binding.chipCatAdvice, binding.chipCatQA, binding.chipCatAnnouncement};
    String[] catKeys = {"all", "dua_request", "islamic_advice", "qa", "announcement"};

    for (int i = 0; i < chips.length; i++) {
      final int index = i;
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(chips[i]);
      chips[i].setOnClickListener(v -> {
        currentCategory[0] = catKeys[index];
        for (int j = 0; j < chips.length; j++) {
          if (j == index) {
            chips[j].setBackgroundResource(R.drawable.bg_badge_pill_active);
            chips[j].setTextColor(context.getColor(R.color.accent_mint));
            chips[j].setTypeface(null, android.graphics.Typeface.BOLD);
          } else {
            chips[j].setBackgroundResource(R.drawable.bg_badge_pill);
            chips[j].setTextColor(context.getColor(R.color.text_secondary));
            chips[j].setTypeface(null, android.graphics.Typeface.NORMAL);
          }
        }
        refreshList.run();
      });
    }

    // Create Post Button Visibility (Only visible if logged in, otherwise hidden)
    boolean isUserLoggedIn = com.devflux.deenone.core.auth.AuthManager.isLoggedIn(context);
    binding.btnCreatePostTop.setVisibility(isUserLoggedIn ? View.VISIBLE : View.GONE);
    binding.cardQuickPost.setVisibility(isUserLoggedIn ? View.VISIBLE : View.GONE);

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCreatePostTop);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseCommunity);

    View.OnClickListener openCreatePostListener = v -> {
      if (!com.devflux.deenone.core.auth.AuthManager.isLoggedIn(context)) {
        if (context instanceof android.app.Activity) {
          com.devflux.deenone.features.profile.FullProfileDialog.show((android.app.Activity) context);
        }
        Toast.makeText(context, isBn ? "পোস্ট প্রকাশ করতে অনুগ্রহ করে প্রথমে লগইন করুন" : "Please log in first to publish a post", Toast.LENGTH_SHORT).show();
        return;
      }
      showCreatePostSheet(context, repo, () -> {
        currentCategory[0] = "all";
        for (int j = 0; j < chips.length; j++) {
          if (j == 0) {
            chips[j].setBackgroundResource(R.drawable.bg_badge_pill_active);
            chips[j].setTextColor(context.getColor(R.color.accent_mint));
          } else {
            chips[j].setBackgroundResource(R.drawable.bg_badge_pill);
            chips[j].setTextColor(context.getColor(R.color.text_secondary));
          }
        }
        refreshList.run();
        binding.rvCommunityPosts.smoothScrollToPosition(0);
      });
    };

    binding.btnCreatePostTop.setOnClickListener(openCreatePostListener);
    binding.cardQuickPost.setOnClickListener(openCreatePostListener);

    binding.btnCloseCommunity.setOnClickListener(v -> dialog.dismiss());

    // Bottom Navigation Tabs & Touch Feedback
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabHome);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabSalat);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabAmal);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabRank);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabCommunity);
    binding.tabHome.setOnClickListener(v -> dialog.dismiss());
    binding.tabSalat.setOnClickListener(v -> {
      dialog.dismiss();
      if (context instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) context).showSalahTrackerSheet();
      }
    });
    binding.tabAmal.setOnClickListener(v -> {
      dialog.dismiss();
      if (context instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) context).showAmalTrackerSheet();
      }
    });
    binding.tabRank.setOnClickListener(v -> {
      dialog.dismiss();
      if (context instanceof android.app.Activity) {
        com.devflux.deenone.features.leaderboard.LeaderboardRankPageDialog.show((android.app.Activity) context);
      }
    });
    binding.tabCommunity.setOnClickListener(v -> binding.scrollCommunityContent.smoothScrollTo(0, 0));

    dialog.show();
  }

  private static void showCreatePostSheet(Context context, CommunityRepository repo, Runnable onPostCreated) {
    BottomSheetDialog sheet = new BottomSheetDialog(context);
    BottomSheetCreatePostBinding binding = BottomSheetCreatePostBinding.inflate(LayoutInflater.from(context));
    sheet.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    if (binding.tvCreatePostHeaderTitle != null) {
      binding.tvCreatePostHeaderTitle.setText(isBn ? "দ্বীনি পোস্ট প্রকাশ করুন" : "Publish Islamic Post");
    }
    if (binding.tvCreatePostHeaderSubtitle != null) {
      binding.tvCreatePostHeaderSubtitle.setText(isBn
          ? "কোরআন, সুন্নাহ ও উম্মাহর কল্যাণে ভালো কথা প্রচার করুন"
          : "Spread beneficial knowledge for the Ummah in light of Quran and Sunnah");
    }
    if (binding.tvCreatePostCategoryLabel != null) {
      binding.tvCreatePostCategoryLabel.setText(isBn ? "বিষয়শ্রেণী নির্বাচন করুন" : "Select Category");
    }
    binding.rbCatDua.setText(isBn ? "দোয়া" : "Dua");
    binding.rbCatAdvice.setText(isBn ? "নসীহত" : "Advice");
    binding.rbCatQA.setText(isBn ? "জিজ্ঞাসা" : "Q&A");
    binding.etNewPostTitle.setHint(isBn ? "শিরোনাম লিখুন" : "Enter title");
    binding.etNewPostContent.setHint(isBn ? "বিস্তারিত বক্তব্য / দোয়া লিখুন..." : "Write content / prayer details...");
    binding.btnSubmitNewPost.setText(isBn ? "পোস্ট প্রকাশ করুন" : "Publish Post");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnSubmitNewPost);

    binding.btnSubmitNewPost.setOnClickListener(v -> {
      String title = binding.etNewPostTitle.getText() != null ? binding.etNewPostTitle.getText().toString().trim() : "";
      String content = binding.etNewPostContent.getText() != null ? binding.etNewPostContent.getText().toString().trim() : "";

      if (title.isEmpty()) {
        binding.etNewPostTitle.setError(isBn ? "অনুগ্রহ করে একটি শিরোনাম দিন" : "Please provide a title");
        return;
      }
      if (content.isEmpty()) {
        binding.etNewPostContent.setError(isBn ? "অনুগ্রহ করে বিস্তারিত বিষয়বস্তু লিখুন" : "Please provide post content");
        return;
      }

      String category = "dua_request";
      int checkedId = binding.rgPostCategory.getCheckedRadioButtonId();
      if (checkedId == R.id.rbCatAdvice) {
        category = "islamic_advice";
      } else if (checkedId == R.id.rbCatQA) {
        category = "qa";
      }

      repo.addNewPost(title, content, category);
      Toast.makeText(context, isBn ? "আপনার পোস্ট সফলভাবে প্রকাশ হয়েছে!" : "Your post has been published successfully!", Toast.LENGTH_SHORT).show();
      sheet.dismiss();
      if (onPostCreated != null) onPostCreated.run();
    });

    sheet.show();
  }

  private static void showCommentsDialog(Context context, CommunityPostItem post, Runnable onCommentAdded) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    BottomSheetCommunityCommentsBinding binding = BottomSheetCommunityCommentsBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseComments);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCancelInlineReply);

    binding.tvCommentsModalTitle.setText(isBn ? ("মন্তব্য সমূহ (" + post.getComments().size() + ")") : ("Comments (" + post.getComments().size() + ")"));
    binding.etNewComment.setHint(isBn ? "একটি মন্তব্য লিখুন..." : "Write a comment...");

    CommunityCommentAdapter commentAdapter = new CommunityCommentAdapter();
    binding.rvCommentsList.setLayoutManager(new LinearLayoutManager(context));
    binding.rvCommentsList.setAdapter(commentAdapter);
    commentAdapter.submitList(new java.util.ArrayList<>(post.getComments()));

    // Fetch fresh comments from remote server (open to everyone)
    CommunityRepository.getInstance(context).fetchCommentsForPost(post.getId(), loadedComments -> {
      if (binding.rvCommentsList != null) {
        binding.rvCommentsList.post(() -> {
          if (loadedComments != null && !loadedComments.isEmpty()) {
            post.setComments(loadedComments);
            post.setCommentCount(loadedComments.size());
          }
          binding.tvCommentsModalTitle.setText(isBn ? ("মন্তব্য সমূহ (" + post.getComments().size() + ")") : ("Comments (" + post.getComments().size() + ")"));
          commentAdapter.submitList(new java.util.ArrayList<>(post.getComments()));
          if (onCommentAdded != null) onCommentAdded.run();
        });
      }
    });

    // Comment input bar is ALWAYS visible so everyone can see and read freely
    if (binding.layoutCommentInputBar != null) {
      binding.layoutCommentInputBar.setVisibility(View.VISIBLE);
    }

    // Reply State Tracking
    final CommunityCommentItem[] activeReply = new CommunityCommentItem[]{null};

    commentAdapter.setOnCommentReplyListener(comment -> {
      if (!AuthManager.isLoggedIn(context)) {
        Activity act = getActivity(context);
        if (act != null) {
          AuthDialogManager.showLoginDialog(act, session -> {
            activeReply[0] = comment;
            binding.layoutInlineReplyChip.setVisibility(View.VISIBLE);
            binding.tvInlineReplyAuthor.setText(isBn ? ("উত্তর দিচ্ছেন @" + comment.getAuthorName()) : ("Replying to @" + comment.getAuthorName()));
            binding.etNewComment.requestFocus();
          });
        }
        return;
      }
      activeReply[0] = comment;
      binding.layoutInlineReplyChip.setVisibility(View.VISIBLE);
      binding.tvInlineReplyAuthor.setText(isBn ? ("উত্তর দিচ্ছেন @" + comment.getAuthorName()) : ("Replying to @" + comment.getAuthorName()));
      binding.etNewComment.requestFocus();
      android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      if (imm != null) {
        imm.showSoftInput(binding.etNewComment, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
      }
    });

    binding.btnCancelInlineReply.setOnClickListener(v -> {
      activeReply[0] = null;
      binding.layoutInlineReplyChip.setVisibility(View.GONE);
    });

    commentAdapter.setOnCommentDeletedListener(() -> {
      binding.tvCommentsModalTitle.setText(isBn ? ("মন্তব্য সমূহ (" + post.getComments().size() + ")") : ("Comments (" + post.getComments().size() + ")"));
      commentAdapter.submitList(new java.util.ArrayList<>(post.getComments()));
      if (onCommentAdded != null) onCommentAdded.run();
    });

    // Send Button Interactive Micro-animations & Dynamics
    binding.btnSendComment.setScaleX(0.92f);
    binding.btnSendComment.setScaleY(0.92f);
    binding.btnSendComment.setAlpha(1.0f);

    binding.etNewComment.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean hasText = s != null && s.toString().trim().length() > 0;
        if (hasText) {
          binding.btnSendComment.animate()
              .scaleX(1.06f)
              .scaleY(1.06f)
              .setDuration(220)
              .setInterpolator(new OvershootInterpolator(2.4f))
              .start();
        } else {
          binding.btnSendComment.animate()
              .scaleX(0.92f)
              .scaleY(0.92f)
              .setDuration(180)
              .setInterpolator(new AccelerateDecelerateInterpolator())
              .start();
        }
      }

      @Override
      public void afterTextChanged(Editable s) {}
    });

    binding.btnSendComment.setOnClickListener(v -> {
      if (!AuthManager.isLoggedIn(context)) {
        Activity act = getActivity(context);
        if (act != null) {
          AuthDialogManager.showLoginDialog(act, null);
        }
        return;
      }

      String text = binding.etNewComment.getText() != null ? binding.etNewComment.getText().toString().trim() : "";
      if (text.isEmpty()) {
        v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction(() ->
            v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(120).start()
        ).start();
        return;
      }

      try {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
      } catch (Throwable ignored) {}

      v.animate()
          .scaleX(0.80f)
          .scaleY(0.80f)
          .rotation(-12f)
          .setDuration(100)
          .withEndAction(() -> {
            v.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .rotation(0f)
                .setDuration(250)
                .setInterpolator(new OvershootInterpolator(2.0f))
                .start();
          })
          .start();

      long now = System.currentTimeMillis();
      String commentId = "c_" + now;
      String activeUser = CommunityRepository.getInstance(context).getActiveUserName();
      String activeAvatar = CommunityRepository.getInstance(context).getActiveUserAvatar();

      CommunityCommentItem newComment;
      if (activeReply[0] != null) {
        newComment = new CommunityCommentItem(
            commentId,
            post.getId(),
            activeUser,
            text,
            now,
            activeReply[0].getId(),
            activeReply[0].getAuthorName(),
            activeAvatar
        );
        CommunityRepository.getInstance(context).addComment(
            post.getId(),
            text,
            activeReply[0].getId(),
            activeReply[0].getAuthorName()
        );
        activeReply[0] = null;
        binding.layoutInlineReplyChip.setVisibility(View.GONE);
      } else {
        newComment = new CommunityCommentItem(
            commentId,
            post.getId(),
            activeUser,
            text,
            now,
            null,
            null,
            activeAvatar
        );
        CommunityRepository.getInstance(context).addComment(post.getId(), text);
      }

      boolean alreadyPresent = false;
      for (CommunityCommentItem existing : post.getComments()) {
        if (commentId.equals(existing.getId())) {
          alreadyPresent = true;
          break;
        }
      }
      if (!alreadyPresent) {
        post.getComments().add(newComment);
      }
      post.setCommentCount(post.getComments().size());

      binding.etNewComment.setText("");
      binding.tvCommentsModalTitle.setText(isBn ? ("মন্তব্য সমূহ (" + post.getComments().size() + ")") : ("Comments (" + post.getComments().size() + ")"));
      commentAdapter.submitList(new java.util.ArrayList<>(post.getComments()));
      binding.rvCommentsList.post(() -> binding.rvCommentsList.smoothScrollToPosition(Math.max(0, commentAdapter.getItemCount() - 1)));
      if (onCommentAdded != null) onCommentAdded.run();
    });

    binding.btnCloseComments.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
  }

  private static void showEditPostSheet(Context context, CommunityRepository repo, CommunityPostItem post, Runnable onPostUpdated) {
    BottomSheetDialog sheet = new BottomSheetDialog(context);
    BottomSheetEditPostBinding binding = BottomSheetEditPostBinding.inflate(LayoutInflater.from(context));
    sheet.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    binding.tvEditPostHeaderTitle.setText(isBn ? "পোস্ট সম্পাদনা করুন" : "Edit Post");
    binding.tvEditPostHeaderSubtitle.setText(isBn ? "আপনার পোস্টের শিরোনাম, বিবরণ ও বিষয়শ্রেণী হালনাগাদ করুন" : "Update title, content and category of your post");
    binding.tvEditPostCategoryLabel.setText(isBn ? "বিষয়শ্রেণী নির্বাচন করুন" : "Select Category");
    binding.rbEditCatDua.setText(isBn ? "দোয়া" : "Dua");
    binding.rbEditCatAdvice.setText(isBn ? "নসীহত" : "Advice");
    binding.rbEditCatQA.setText(isBn ? "জিজ্ঞাসা" : "Q&A");
    binding.layoutEditPostTitle.setHint(isBn ? "শিরোনাম লিখুন" : "Enter title");
    binding.layoutEditPostContent.setHint(isBn ? "বিস্তারিত বক্তব্য / দোয়া লিখুন..." : "Write content / prayer details...");
    binding.btnCancelEditPost.setText(isBn ? "বাতিল" : "Cancel");
    binding.btnSubmitEditPost.setText(isBn ? "আপডেট সংরক্ষণ করুন" : "Save Changes");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCancelEditPost);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnSubmitEditPost);

    binding.etEditPostTitle.setText(post.getTitle());
    binding.etEditPostContent.setText(post.getContent());

    String cat = post.getCategory();
    if ("islamic_advice".equalsIgnoreCase(cat)) {
      binding.rbEditCatAdvice.setChecked(true);
    } else if ("qa".equalsIgnoreCase(cat)) {
      binding.rbEditCatQA.setChecked(true);
    } else {
      binding.rbEditCatDua.setChecked(true);
    }

    binding.btnCancelEditPost.setOnClickListener(v -> sheet.dismiss());

    binding.btnSubmitEditPost.setOnClickListener(v -> {
      String title = binding.etEditPostTitle.getText() != null ? binding.etEditPostTitle.getText().toString().trim() : "";
      String content = binding.etEditPostContent.getText() != null ? binding.etEditPostContent.getText().toString().trim() : "";

      if (title.isEmpty()) {
        binding.etEditPostTitle.setError(isBn ? "অনুগ্রহ করে একটি শিরোনাম দিন" : "Please provide a title");
        return;
      }
      if (content.isEmpty()) {
        binding.etEditPostContent.setError(isBn ? "অনুগ্রহ করে বিস্তারিত বিষয়বস্তু লিখুন" : "Please provide content");
        return;
      }

      String updatedCat = "dua_request";
      int checkedId = binding.rgEditPostCategory.getCheckedRadioButtonId();
      if (checkedId == R.id.rbEditCatAdvice) {
        updatedCat = "islamic_advice";
      } else if (checkedId == R.id.rbEditCatQA) {
        updatedCat = "qa";
      }

      post.setTitle(title);
      post.setContent(content);
      post.setCategory(updatedCat);

      repo.editPost(post.getId(), title, content, updatedCat, new CommunityRepository.OnOperationCallback() {
        @Override
        public void onSuccess(String message) {}

        @Override
        public void onError(String error) {}
      });

      sheet.dismiss();
      if (onPostUpdated != null) onPostUpdated.run();
    });

    sheet.show();
  }

  private static void showReportPostDialog(Context context, CommunityRepository repo, CommunityPostItem post) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String[] reasons = isBn ? new String[]{
        "আপত্তিকর বা ক্ষতিকর বিষয়বস্তু",
        "ভুল বা বিভ্রান্তিকর ইসলামী তথ্য",
        "স্প্যাম বা বিজ্ঞাপন",
        "ব্যক্তিগত আক্রমণ বা অসদাচরণ",
        "অন্যান্য"
    } : new String[]{
        "Inappropriate or harmful content",
        "Incorrect or misleading Islamic information",
        "Spam or advertisement",
        "Personal harassment or misconduct",
        "Other"
    };

    final int[] selectedIndex = {0};

    new AlertDialog.Builder(context)
        .setTitle(isBn ? "পোস্ট রিপোর্ট করুন" : "Report Post")
        .setSingleChoiceItems(reasons, 0, (dialog, which) -> selectedIndex[0] = which)
        .setPositiveButton(isBn ? "রিপোর্ট পাঠান" : "Submit Report", (dialog, which) -> {
          String chosenReason = reasons[selectedIndex[0]];
          repo.reportPost(post.getId(), chosenReason, "", new CommunityRepository.OnOperationCallback() {
            @Override
            public void onSuccess(String message) {}

            @Override
            public void onError(String error) {}
          });
        })
        .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
        .show();
  }

  private static Activity getActivity(Context context) {
    if (context == null) return null;
    if (context instanceof Activity) return (Activity) context;
    if (context instanceof ContextWrapper) {
      return getActivity(((ContextWrapper) context).getBaseContext());
    }
    return null;
  }
}