package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public communication com=new communication();
    public static String User_id="";
    public static String User_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText edt0 = (EditText) findViewById(R.id.et0);
        EditText edt1 = (EditText) findViewById(R.id.et1);
        edt0.getText();
        Button btn1 = (Button) findViewById(R.id.btn_1);
        Button btn2 = (Button) findViewById(R.id.btn_2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent(MainActivity.this,RecommendActivity.class);
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面

                class LoginThread extends Thread
                {
                    public String User;
                    public String Password;
                    LoginThread(String user,String password){
                        User=user;
                        Password=password;
                    }
                    @Override
                    public void run()
                    {
                        System.out.println("login thread!");
                        try {
                            User_name=User;
                            User_id=com.GetUserId(User,Password);
                            //User_id="2";
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
                System.out.println(edt0.getText().toString()+edt1.getText().toString());
                LoginThread loginThread=new LoginThread(edt0.getText().toString(),edt1.getText().toString());
                System.out.println("Login!");
                loginThread.start();
                try {
                    loginThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                intent.putExtra("User_id",User_id);
                if(Integer.parseInt(User_id)!=-1)startActivity(intent);
                else Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecommendActivity.class);
                class registerThread extends Thread
                {
                    public String User;
                    public String Password;
                    registerThread(String user,String password){
                        User=user;
                        Password=password;
                    }
                    @Override
                    public void run()
                    {
                        System.out.println("register thread!");
                        try {
                            User_name=User;
                            User_id=com.RegUserId(User,Password);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
                System.out.println(edt0.getText().toString()+edt1.getText().toString());
                registerThread regThread=new registerThread(edt0.getText().toString(),edt1.getText().toString());
                System.out.println("Register!");
                regThread.start();
                try {
                    regThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                intent.putExtra("User_id",User_id);
                if(Integer.parseInt(User_id)!=-1)startActivity(intent);
                else Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void  myClick(View v){
        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_LONG).show();
    }
}