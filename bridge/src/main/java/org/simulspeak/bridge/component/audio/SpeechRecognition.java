package org.simulspeak.bridge.component.audio;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class SpeechRecognition {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        // Set the path to the acoustic model
        configuration.setAcousticModelPath("path/to/acoustic/model");

        // Set the path to the language model
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        // configuration.setDictionaryPath("path/to/dictionary");
        // configuration.setLanguageModelPath("path/to/language/model");

        StreamSpeechRecognizer recognizer = null;

        // LiveSpeechRecognizer recognizer = null;
        // try {
        //     recognizer = new LiveSpeechRecognizer(configuration);
        //     recognizer.startRecognition(true);

        //     SpeechResult result;
        //     while ((result = recognizer.getResult()) != null) {
        //         String recognizedText = result.getHypothesis();
        //         // Process the recognized text as needed
        //         System.out.println("Recognized: " + recognizedText);
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // } finally {
        //     if (recognizer != null) {
        //         recognizer.stopRecognition();
        //     }
        // }
    }
}

