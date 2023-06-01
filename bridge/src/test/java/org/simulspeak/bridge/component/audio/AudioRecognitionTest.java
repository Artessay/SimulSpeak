package org.simulspeak.bridge.component.audio;

import org.junit.jupiter.api.Test;

public class AudioRecognitionTest {
    @Test
    void testRecognize() {
        AudioRecognition audioRecognition = new AudioRecognition();
        String audioFilePath = "src/test/resources/audio/1.mp3";
        String result = audioRecognition.recognize(audioFilePath);
        System.out.println(result);
    }
}
