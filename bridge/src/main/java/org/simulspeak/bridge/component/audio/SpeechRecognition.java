package org.simulspeak.bridge.component.audio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class SpeechRecognition {
    public void speechRecognition(String audioPath) {
        Configuration configuration = new Configuration();

        // Set the path to the acoustic model
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");

        // Set the path to the language model
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        // configuration.setDictionaryPath("path/to/dictionary");
        // configuration.setLanguageModelPath("path/to/language/model");

        StreamSpeechRecognizer recognizer = null;
        try {
            recognizer = new StreamSpeechRecognizer(configuration);
            InputStream inputStream = new FileInputStream(audioPath);
            
            recognizer.startRecognition(inputStream);
            
            SpeechResult result;
            while ((result = recognizer.getResult()) != null) {
                String transcript = result.getHypothesis();
                System.out.println(transcript);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (recognizer != null) {
                recognizer.stopRecognition();
            }
        }

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

    public static void main(String[] args) {
        String audioPath = "E:\\CP\\Arboretum\\video.mp3";
        SpeechRecognition speechRecognition = new SpeechRecognition();
        speechRecognition.speechRecognition(audioPath);
    }
}

