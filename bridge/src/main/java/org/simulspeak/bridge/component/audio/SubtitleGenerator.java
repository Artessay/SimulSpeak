package org.simulspeak.bridge.component.audio;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SubtitleGenerator {
    public static void generateSubtitleFile(String subtitleFilePath, String transcription) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(subtitleFilePath))) {
            JSONObject response = new JSONObject(transcription);
            JSONArray segments = response.getJSONArray("segments");

            int segmentCount = segments.length();
            for (int i = 0; i < segmentCount; i++) {
                JSONObject segment = segments.getJSONObject(i);
                JSONObject alternatives = segment.getJSONArray("alternatives").getJSONObject(0);

                double startTime = segment.getDouble("start_time");
                double endTime = segment.getDouble("end_time");
                String text = alternatives.getString("text");

                // Convert start time and end time to subtitle format (e.g., 00:00:10,000)
                String startTimeStr = formatTime(startTime);
                String endTimeStr = formatTime(endTime);

                // Write the subtitle entry to the file
                writer.write((i + 1) + "\n");
                writer.write(startTimeStr + " --> " + endTimeStr + "\n");
                writer.write(text + "\n");
                writer.write("\n");
            }

            System.out.println("Subtitle file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatTime(double time) {
        int hours = (int) (time / 3600);
        int minutes = (int) ((time % 3600) / 60);
        int seconds = (int) (time % 60);
        int milliseconds = (int) ((time % 1) * 1000);

        return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, milliseconds);
    }
}
