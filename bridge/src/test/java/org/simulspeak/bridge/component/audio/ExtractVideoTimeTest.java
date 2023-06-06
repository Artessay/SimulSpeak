package org.simulspeak.bridge.component.audio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExtractVideoTimeTest {
    @Test
    void testExtractVideoTime() {
        String timeLength = ExtractVideoTime.extractVideoTime("E:\\CP\\Arboretum\\audio.mp3");
        if(timeLength.length()>0){//字符串截取
            timeLength =timeLength.substring(0,timeLength.indexOf("."));
        }
        System.out.println("视频时长:"+timeLength);
    }

    @Test
    void testExtractVideoSeconds() {
        int seconds = ExtractVideoTime.extractVideoSeconds("E:\\CP\\Arboretum\\audio.mp3");
        Assertions.assertEquals(11, seconds);
    }
}
