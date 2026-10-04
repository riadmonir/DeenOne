package com.devflux.deenone.core.audio;

import com.devflux.deenone.core.quran.service.QuranSleepService;
import com.devflux.deenone.features.audio.service.IslamicAudioPlayerService;

import org.junit.Assert;
import org.junit.Test;

public class GlobalAudioCoordinatorComprehensiveTest {

    @Test
    public void testCoordinatorSingletonAndInitialState() {
        GlobalAudioCoordinator coordinator = GlobalAudioCoordinator.getInstance();
        Assert.assertNotNull(coordinator);
    }

    @Test
    public void testAudioSourceMutualExclusivity() {
        GlobalAudioCoordinator coordinator = GlobalAudioCoordinator.getInstance();

        coordinator.requestPlayback(null, GlobalAudioCoordinator.AudioSource.QURAN_SLEEP_MODE);
        Assert.assertEquals(GlobalAudioCoordinator.AudioSource.QURAN_SLEEP_MODE, coordinator.getCurrentActiveSource());

        coordinator.requestPlayback(null, GlobalAudioCoordinator.AudioSource.ISLAMIC_AUDIO_HUB);
        Assert.assertEquals(GlobalAudioCoordinator.AudioSource.ISLAMIC_AUDIO_HUB, coordinator.getCurrentActiveSource());

        coordinator.stopAll(null);
        Assert.assertEquals(GlobalAudioCoordinator.AudioSource.NONE, coordinator.getCurrentActiveSource());
    }

    @Test
    public void testQuranSleepServiceConstants() {
        Assert.assertEquals("channel_quran_sleep_mode", QuranSleepService.NOTIF_CHANNEL_ID);
        Assert.assertEquals(9902, QuranSleepService.NOTIF_ID);
        Assert.assertEquals("com.devflux.deenone.quran.sleep.PLAY_SURAH", QuranSleepService.ACTION_PLAY_SURAH);
        Assert.assertEquals("com.devflux.deenone.quran.sleep.PAUSE", QuranSleepService.ACTION_PAUSE);
        Assert.assertEquals("com.devflux.deenone.quran.sleep.RESUME", QuranSleepService.ACTION_RESUME);
        Assert.assertEquals("com.devflux.deenone.quran.sleep.STOP", QuranSleepService.ACTION_STOP);
    }

    @Test
    public void testIslamicAudioPlayerServiceConstants() {
        Assert.assertEquals("com.devflux.deenone.audio.PLAY", IslamicAudioPlayerService.ACTION_PLAY);
        Assert.assertEquals("com.devflux.deenone.audio.PAUSE", IslamicAudioPlayerService.ACTION_PAUSE);
        Assert.assertEquals("com.devflux.deenone.audio.STOP", IslamicAudioPlayerService.ACTION_STOP);
    }
}
