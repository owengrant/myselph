package my.selph.domain.ai;

import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

import javax.enterprise.context.ApplicationScoped;
import javax.sound.sampled.AudioFileFormat;

@ApplicationScoped
public class Speaker {

    public void toAudio(String text) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        var voice = voiceManager.getVoice("kevin16");
        voice.allocate();
        var audioPlayer = new SingleFileAudioPlayer("output"+Math.random(), AudioFileFormat.Type.WAVE);
        voice.setAudioPlayer(audioPlayer);
        voice.setRate(100);
        voice.setPitch(150);
        voice.setVolume(3);
        voice.speak(text);
        voice.deallocate();
        audioPlayer.close();
    }

}
