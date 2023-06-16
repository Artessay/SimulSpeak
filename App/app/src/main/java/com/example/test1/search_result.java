package com.example.test1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class search_result extends Activity
{

    public communication com=new communication();
    private SearchAdapter searchAdapter;
    private RecyclerView mRV;
    private List<String> uid=new ArrayList<>();
    private  List<String> vid=new ArrayList<>();
    private List<Drawable> images=new ArrayList<>();
    private List<String> contents=new ArrayList<>();
    private  List<String> times=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String search_content;

        search_content=intent.getStringExtra("search");


        String search_print="搜索"+search_content+" 返回的结果:";

        setContentView(R.layout.search_result);
        TextView search_text=findViewById(R.id.search_hint);
        search_text.setText(search_print);

        class SearchThread extends Thread
        {
            public String Keyword;
            SearchThread(String keyword){
                Keyword=keyword;
            }
            @Override
            public void run()
            {
                try {
                    System.out.println("thread run");
                    List<String> Urls=com.GetSearchResult(Keyword);

                    for(int i=0;i<Urls.size();i+=5)
                    {
                        images.add(i/5,com.GetImage(Urls.get(i)));
                        contents.add(i/5,Urls.get(i+1));
                        times.add(i/5,Urls.get(i+2));
                        uid.add(i/5,Urls.get(i+3));
                        vid.add(i/5,Urls.get(i+4));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        SearchThread search =new SearchThread(search_content);
        search.start();
        try {
            search.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        mRV = findViewById(R.id.rv);

        mRV.setLayoutManager(new LinearLayoutManager(search_result.this));

        searchAdapter=
                (new SearchAdapter(search_result.this,new SearchAdapter.OnItemClickListener(){
                    @Override
                    public void onClick(int pos){
                        System.out.println("click on "+pos+"done11!");
                        Toast.makeText(search_result.this,"click..."+pos,Toast.LENGTH_SHORT).show();
                        System.out.println("click on "+pos+"done!");
                        Intent intent = new Intent(search_result.this,VideoPlay.class);

                        String video_id=vid.get(pos);
                        intent.putExtra("videoid",vid.get(pos));

                        System.out.println("click on "+pos+"done22!");
                        if(video_id!="")startActivity(intent);
                        else Toast.makeText(search_result.this,"该视频不存在",Toast.LENGTH_SHORT).show();

                    }
                },images,contents,times));
        mRV.setAdapter(searchAdapter);


        ImageButton search_btn = (ImageButton) findViewById(R.id.searchbtn);
        search_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent(search_result.this,search_result.class);

                EditText search=findViewById(R.id.search);
                String search_content=search.getText().toString();

                intent.putExtra("search",search_content);
                if(!search_content.isEmpty())startActivity(intent);
                else Toast.makeText(search_result.this,"请输入内容",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

