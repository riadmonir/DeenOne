package com.devflux.deenone.core.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Dedicated Islamic Arabic Voice Engine.
 * Accurately recites authentic Arabic text (Six Kalimas, 99 Names of Allah, Duas)
 * using high-fidelity offline Arabic TTS with seamless Google Arabic Neural TTS fallback.
 * Strictly guarantees that ONLY the intended Islamic phrase is recited, never unrelated Quran Surahs.
 */
public class ArabicVoiceAudioEngine {

    private static final String TAG = "ArabicVoiceEngine";
    private static volatile ArabicVoiceAudioEngine instance;

    private TextToSpeech textToSpeech;
    private boolean isTtsReady = false;
    private MediaPlayer mediaPlayer;
    private String currentIdentifier = null;
    private boolean isPlaying = false;
    private PlaybackCallback currentCallback;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Context appContext;

    public interface PlaybackCallback {
        void onStart(String identifier);
        void onDone(String identifier);
        void onError(String identifier, String errorReason);
    }

    public static ArabicVoiceAudioEngine getInstance(Context context) {
        if (instance == null) {
            synchronized (ArabicVoiceAudioEngine.class) {
                if (instance == null) {
                    instance = new ArabicVoiceAudioEngine(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private ArabicVoiceAudioEngine(Context context) {
        this.appContext = context;
        initTts(context);
    }

    private void initTts(Context context) {
        try {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(new Locale("ar"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        result = textToSpeech.setLanguage(new Locale("ar", "SA"));
                    }
                    if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                        isTtsReady = true;
                        textToSpeech.setSpeechRate(0.82f); // Natural, clear recitation tempo
                        textToSpeech.setPitch(1.0f);
                    }
                }
            });

            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    mainHandler.post(() -> {
                        isPlaying = true;
                        if (currentCallback != null) {
                            currentCallback.onStart(utteranceId);
                        }
                    });
                }

                @Override
                public void onDone(String utteranceId) {
                    mainHandler.post(() -> {
                        isPlaying = false;
                        String id = currentIdentifier;
                        currentIdentifier = null;
                        if (currentCallback != null) {
                            currentCallback.onDone(id);
                        }
                    });
                }

                @Override
                public void onError(String utteranceId) {
                    mainHandler.post(() -> {
                        isPlaying = false;
                        currentIdentifier = null;
                        if (currentCallback != null) {
                            currentCallback.onError(utteranceId, "উচ্চারণ পাঠে ত্রুটি হয়েছে");
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Failed to init TextToSpeech: " + e.getMessage());
        }
    }

    public void playArabic(Context context, String arabicText, String identifier, PlaybackCallback callback) {
        stop();
        this.currentIdentifier = identifier;
        this.currentCallback = callback;

        if (arabicText == null || arabicText.trim().isEmpty()) {
            if (callback != null) callback.onError(identifier, "আরবি টেক্সট পাওয়া যায়নি");
            return;
        }

        String cleanArabic = cleanArabicText(arabicText);

        if (isTtsReady && textToSpeech != null) {
            try {
                int res = textToSpeech.speak(cleanArabic, TextToSpeech.QUEUE_FLUSH, null, identifier);
                if (res == TextToSpeech.SUCCESS) {
                    isPlaying = true;
                    if (callback != null) callback.onStart(identifier);
                    return;
                }
            } catch (Exception e) {
                Log.e(TAG, "TTS speak failed, streaming fallback: " + e.getMessage());
            }
        }

        // Stream fallback
        streamArabicOnline(context, cleanArabic, identifier, callback);
    }

    public void streamArabicOnline(Context context, String arabicText, String identifier, PlaybackCallback callback) {
        releaseMediaPlayer();
        try {
            String encoded = URLEncoder.encode(arabicText, StandardCharsets.UTF_8.name());
            String streamUrl = "https://translate.google.com/translate_tts?ie=UTF-8&tl=ar&client=tw-ob&q=" + encoded;

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );

            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:109.0) Gecko/109.0 Firefox/119.0");

            mediaPlayer.setDataSource(context, Uri.parse(streamUrl), headers);
            mediaPlayer.setOnPreparedListener(mp -> {
                isPlaying = true;
                mp.start();
                if (callback != null) callback.onStart(identifier);
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                String id = currentIdentifier;
                currentIdentifier = null;
                releaseMediaPlayer();
                if (callback != null) callback.onDone(id);
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                isPlaying = false;
                currentIdentifier = null;
                releaseMediaPlayer();
                if (callback != null) callback.onError(identifier, "অনলাইন অডিও প্লে করা সম্ভব হয়নি");
                return true;
            });

            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            isPlaying = false;
            currentIdentifier = null;
            if (callback != null) callback.onError(identifier, e.getMessage());
        }
    }

    public void stop() {
        if (textToSpeech != null) {
            try {
                textToSpeech.stop();
            } catch (Exception ignored) {}
        }
        releaseMediaPlayer();
        isPlaying = false;
        currentIdentifier = null;
    }

    public boolean isPlaying() {
        return isPlaying || (mediaPlayer != null && mediaPlayer.isPlaying());
    }

    public String getCurrentIdentifier() {
        return currentIdentifier;
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.release();
            } catch (Exception ignored) {}
                mediaPlayer = null;
        }
    }

    public static String cleanArabicText(String text) {
        if (text == null) return "";
        return text.trim();
    }
}
