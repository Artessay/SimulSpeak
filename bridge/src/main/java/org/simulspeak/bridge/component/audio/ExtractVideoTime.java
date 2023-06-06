package org.simulspeak.bridge.component.audio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractVideoTime {
    // public static void main(String[] args) {
        
    //     String timeLength = extractVideoTime("E:\\CP\\Arboretum\\audio.mp3");
    //     if(timeLength.length()>0){//字符串截取
    //         timeLength =timeLength.substring(0,timeLength.indexOf("."));
    //     }
    //     System.out.println("视频时长:"+timeLength);
       
    // }

    public static int extractVideoSeconds(String video_path) {
        String times = extractVideoTime(video_path);
        if (times.equals("")) {
            return 0;
        }

        String[] time = times.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        double secondReal = Double.parseDouble(time[2]);
        int second = (int)secondReal;

        return hour * 3600 + minute * 60 + second;
    }

     /**
     *获取视频时间
     * @param video_path  视频路径
     * @param ffmpeg_path ffmpeg安装路径
     * @return
     */
    public static String extractVideoTime(String video_path) {
        String ffmpeg_path = "ffmpeg";
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpeg_path);
        commands.add("-i");
        commands.add(video_path);
        System.out.println("命令行:"+ffmpeg_path+" -i "+video_path);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            final Process p = builder.start();

            //从输入流中读取视频信息
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            //从视频信息中解析时长
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
            Pattern pattern = Pattern.compile(regexDuration);
            Matcher m = pattern.matcher(sb.toString());
            if (m.find()) {
                System.out.println(video_path+",视频时长："+m.group(1)+", 开始时间："+m.group(2)+",比特率："+m.group(3)+"kb/s");
                return m.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
