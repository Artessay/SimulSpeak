package org.simulspeak.bridge.component.audio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioRecognitionTest {
    @Test
    void testRecognize() {
        AudioRecognition audioRecognition = new AudioRecognition();
        // String audioFilePath = "src/test/resources/audio/1.mp3";
        String audioFilePath = "http://192.168.137.87:80/sounds/e16ITPo4c.mp3";
        String result = audioRecognition.recognize(audioFilePath);
        Assertions.assertNotNull(result);
        // Assertions.assertFalse(result.equals(""));
        // System.out.println(result);
    }
}
