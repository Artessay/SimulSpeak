package com.example.test1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class communication {
    public final String Server_Url="http://192.168.137.1:8080";
    private final String USER_AGENT = "Mozilla/5.0";
    //public long user_id=Long.getLong(MainActivity.User_id);
    public long user_id=2;
    public void communication()
    {
        user_id=-1;
    }
    public String GetUserId(String user,String password) throws IOException {
        String url = Server_Url;

        url=url+"/user/login?identifier="+user+"&credential="+password;
        //url=url+"/user/login";
        System.out.println(url);
        URL obj = new URL(url);


        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);



        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //打印结果
        System.out.println(response.toString());
        return  response.toString();
    }
    public String RegUserId(String user,String password) throws IOException {
        String url = Server_Url;

        url=url+"/user/register?identifier="+user+"&credential="+password;
        //url=url+"/user/login";
        System.out.println(url);
        URL obj = new URL(url);


        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);



        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //打印结果
        System.out.println(response.toString());
        return  response.toString();
    }
    public List<String> GetUserVideo()throws IOException {
        String url = Server_Url;

        url=url+"/video/searchByUser?userId="+MainActivity.User_id;
        System.out.println(url);
        URL obj = new URL(url);
        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println("debug<0>");
        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        List<String> urls=new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            urls.add(inputLine);
            System.out.println(inputLine);
        }
        in.close();

        return urls;
    }
    public List<String> GetVideo(String vid)throws IOException {
        String url = Server_Url;

        url=url+"/video/request?videoId="+vid;
        System.out.println(url);
        URL obj = new URL(url);
        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println("debug<0.0000>");
        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);


        int responseCode=0 ;
        try{
            responseCode= con.getResponseCode();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        List<String> cont=new ArrayList<>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            cont.add(inputLine);
        }
        System.out.println(cont.get(0));
        System.out.println(cont.get(1));

        return cont;
    }
    public List<String> GetRecommend()throws IOException {
        String url = Server_Url;

        url=url+"/video/recommend";
        System.out.println(url);
        URL obj = new URL(url);
        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println("debug<0>");
        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        List<String> urls=new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            urls.add(inputLine);
            System.out.println(inputLine);
        }
        in.close();

        return urls;
    }
    public List<String> GetSearchResult(String keyword)throws IOException {
        System.out.println("GetSearchResult");
        String url = Server_Url;
        System.out.println(keyword);
        url=url+"/video/search?videoName="+keyword;
        System.out.println(url);
        URL obj = new URL(url);
        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //默认值我GET
        con.setRequestMethod("GET");
        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        List<String> urls=new ArrayList<>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            urls.add(inputLine);
            System.out.println(inputLine);
        }
        in.close();

        return urls;
    }
    public List<String> GetUrl(String title)throws IOException {
        String url = Server_Url;

        url=url+"/video/upload?videoName="+title+"&userId="+user_id;
        System.out.println(url);
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //默认值我GET
        con.setRequestMethod("GET");
        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();


        List<String> values=new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            values.add(inputLine);
        }
        in.close();

        return values;
    }
    public Drawable GetImage(String url)throws IOException {

        String Url=url;
        URL obj = new URL(Url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        InputStream in =con.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len=0;
        while ((len = in.read(buff)) != -1) {
            os.write(buff, 0, len);
        }
        byte[] image=os.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        Drawable drawable = new BitmapDrawable(bmp);
        return drawable;
    }
    public void SendComment(String comment,String vid)throws IOException {
        String url = Server_Url;

        url=url+"/video/pushComment?videoId="+vid+"&userId="+user_id+"&comment="+comment;
        System.out.println(url);
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //默认值我GET
        con.setRequestMethod("GET");
        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

    }
    public List<String> GetComment(String vid)throws IOException {
        String url = Server_Url;

        url=url+"/video/pullComment?videoId="+vid;
        System.out.println(url);
        URL obj = new URL(url);
        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println("debug<0>");
        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        List<String> urls=new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            urls.add(inputLine);
            System.out.println(inputLine);
        }
        in.close();

        return urls;
    }
    public Map<Integer,String> GetSubTitle(String vid)throws IOException {
        String url = Server_Url;

        url=url+"/video/SubTitle?videoId="+vid;
        System.out.println(url);
        URL obj = new URL(url);
        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println("debug<0>");
        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        List<String> urls=new ArrayList<>();
        int begin=0,end=0;
        int i=0;
        String word;
        Map<Integer,String> map=new HashMap<>();
        System.out.println("subtitle begin");
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
            if(i%3==0)begin=Integer.parseInt(inputLine);
            else if(i%3==1) end=Integer.parseInt(inputLine);
            else {
                for(int j=begin;j<end;j++)
                {
                    map.put(j,inputLine);
                }
            }
            i++;
            urls.add(inputLine);
            System.out.println(inputLine);
        }
        in.close();
        return map;
    }


}
