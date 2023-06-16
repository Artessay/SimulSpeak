package com.example.test1;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RecommendActivity extends Activity
{
    private RecyclerView mRV;
    public String User_id=MainActivity.User_id;

    public communication com=new communication();
    private SearchAdapter searchAdapter;
    private final String USER_AGENT = "Mozilla/5.0";
    private final String Server_Url="http://192.168.137.78:8888/?uid=1&vid=1&type=applyPhoto";
    String user_id;
    private List<Drawable> images=new ArrayList<>();
    private List<String> contents=new ArrayList<>();
    private  List<String> times=new ArrayList<>();
    private List<String> videos=new ArrayList<>();
    private List<String> uid=new ArrayList<>();
    private List<String> vid=new ArrayList<>();

    private Drawable GetImage(int id) throws Exception{
        String url = Server_Url;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //默认值我GET
        con.setRequestMethod("GET");

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
        return null;
    }

    private void generateList(List<Drawable> images,List<String> contents,List<String> times)
    {
        this.images=images;
        this.contents=contents;
        this.times=times;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        setContentView(R.layout.recommend);

        Thread Refresh=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    try {
                        sleep(100);
                        searchAdapter.set_items(images,contents,times);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

        class GetRecommendThread extends Thread{
            @Override
            public void run() {
                try {
                    List<String> Urls=com.GetRecommend();

                    List<Drawable> images_=new ArrayList<>();
                    List<String> contents_=new ArrayList<>();
                    List<String> times_=new ArrayList<>();
                    List<String> uid_=new ArrayList<>();
                    List<String> vid_=new ArrayList<>();

                    for(int i=0;i<Urls.size();i+=5)
                    {
                        images_.add(i/5,com.GetImage(Urls.get(i)));
                        contents_.add(i/5,Urls.get(i+1));
                        times_.add(i/5,Urls.get(i+2));
                        uid_.add(i/5,Urls.get(i+3));
                        vid_.add(i/5,Urls.get(i+4));
                    }
                    images=images_;
                    contents=contents_;
                    times=times_;
                    uid=uid_;
                    vid=vid_;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        GetRecommendThread recommendThread=new GetRecommendThread();
        recommendThread.start();

        generateList(images,contents,times);

//利用LinearLayout控件的id获得此控件的对象
        mRV = findViewById(R.id.rv);

        mRV.setLayoutManager(new LinearLayoutManager(RecommendActivity.this));

        searchAdapter=
                (new SearchAdapter(RecommendActivity.this,new SearchAdapter.OnItemClickListener(){
                    @Override
                    public void onClick(int pos){
                        Toast.makeText(RecommendActivity.this,"click..."+pos,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RecommendActivity.this,VideoPlay.class);

                        //ntent.putExtra("videoid",vid.get(pos));
                        String videoid=vid.get(pos);
                        intent.putExtra("videoid",vid.get(pos));
                        String userid=uid.get(pos);
                        intent.putExtra("userid",userid);
                        intent.putExtra("title",contents.get(pos));

                        if(videoid!="")startActivity(intent);
                        else Toast.makeText(RecommendActivity.this,"该视频不存在",Toast.LENGTH_SHORT).show();

                    }
                },images,contents,times));
        mRV.setAdapter(searchAdapter);


        Button refresh_btn=(Button) findViewById(R.id.refreshbtn);
        Button personal_btn=(Button) findViewById(R.id.uploadvideo);
        ImageButton searchbtn = (ImageButton) findViewById(R.id.searchbtn);

        personal_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecommendActivity.this,personal.class);

                startActivity(intent);
            }
        });
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                GetRecommendThread recommendThread=new GetRecommendThread();
                try {
                    recommendThread.start();
                    recommendThread.join();
                } catch (Exception e) {
                    System.out.println(e);
                }
                searchAdapter.set_items(images,contents,times);
            }
        });
        searchbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent(RecommendActivity.this,search_result.class);

                EditText search=findViewById(R.id.search);
                String searchcontent=search.getText().toString();

                intent.putExtra("search",searchcontent);
                System.out.println("searchcontent is "+searchcontent.isEmpty());
                if(!searchcontent.isEmpty())startActivity(intent);
                else Toast.makeText(RecommendActivity.this,"请输入内容",Toast.LENGTH_SHORT).show();
            }
        });
        try {
            recommendThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        searchAdapter.set_items(images,contents,times);
    }

}
