package com.vincent.topzrt;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_BIG_PICTURE = 100;
    private static final int CHOOSE_SMALL_PICTURE = 101;
    private Button mBtnChoose;
    private ImageView mImageView;

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private Button mIdChooseSmall;
    private Button mBtnTest;
    private Button mIdTestPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnChoose = (Button) findViewById(R.id.id_choose);
        mIdChooseSmall = (Button) findViewById(R.id.id_choose_small);
        mBtnTest = (Button) findViewById(R.id.id_test);
        mIdTestPost = (Button) findViewById(R.id.id_test_post);

        mImageView = (ImageView) findViewById(R.id.id_iv);

        mBtnChoose.setOnClickListener(this);
        mIdChooseSmall.setOnClickListener(this);
        mBtnTest.setOnClickListener(this);
        mIdTestPost.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_test:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doGet();
                    }
                }).start();
                break;

            case R.id.id_test_post:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        doPost();
                        doPostPic();
                    }


                }).start();
                break;

        }
    }

    private void doPostPic() {
        String url = "http://192.168.1.109:8080/TopZrt/UploadTextServlet";
        Log.e("Vincent", "doPostPic: " + Environment.getExternalStorageDirectory().getPath());
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        long length = file.length();
        Log.e("Vincent", "doPostPic: " + length);
        try {
            FileInputStream fis = new FileInputStream(file);

            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 设置容许输出
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Charsert", "UTF-8");

            OutputStream outputStream = connection.getOutputStream();

            int count = 0;
            while ((count = fis.read()) != -1) {
                outputStream.write(count);
            }
            outputStream.flush();
            outputStream.close();

            // 获取返回数据
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();

                String s = UploadUtil.convertStreamToString(is);

                Log.e("Vincent", "doPostPic: " + s);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void doPost() {
        String url = "http://192.168.1.109:8080/TopZrt/UploadTextServlet";
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            //设置允许输入
            connection.setDoInput(true);
            //设置允许输出
            connection.setDoOutput(true);

            //post方式不能设置缓存，需手动设置为false
            connection.setUseCaches(false);

            //我们请求的数据
            String data = "name=" + URLEncoder.encode("Vincent", "UTF-8") +
                    "&age=" + URLEncoder.encode("18", "UTF-8");
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
//            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String s = UploadUtil.convertStreamToString(inputStream);
                Log.e("Vincent", "doPost: " + s);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void doGet() {
        String url = "http://192.168.1.109:8080/TopZrt/UploadTextServlet?name=Vincent&age=18";

        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();

                String s = UploadUtil.convertStreamToString(inputStream);
                Log.e("Vincent", "doGet: " + s);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
