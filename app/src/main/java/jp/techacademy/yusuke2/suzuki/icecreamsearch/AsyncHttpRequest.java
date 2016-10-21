package jp.techacademy.yusuke2.suzuki.icecreamsearch;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yusus on 2016/10/18.
 */

public class AsyncHttpRequest extends AsyncTask<Uri, Void, String> {

    private Activity MapsActivity;

    public AsyncHttpRequest(Activity activity) {

        // 呼び出し元のアクティビティ
        this.MapsActivity = activity;
    }

    // このメソッドは必ずオーバーライドする必要があるよ
    // ここが非同期で処理される部分みたいたぶん。
    @Override
    public String doInBackground(Uri... params) {

        Uri urib = params[0];
        String str1 = urib.toString();

        try {
            // URLの作成
            URL url = new URL(str1);
            // 接続用HttpURLConnectionオブジェクト作成
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // リクエストメソッドの設定
            con.setRequestMethod("GET");
            // 接続
            con.connect();

            BufferedInputStream is = new BufferedInputStream(con.getInputStream());
            InputStream bufIn = con.getInputStream();
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Json読み込み
            String json = new String(buffer);
            JSONObject jsonObject = new JSONObject(json);

//            String path = Environment.getDataDirectory() + "/sample/";
//            String fileName = "sample.json";
//            File dir = new File(path);
//            dir.mkdirs();
//            File outputFile = new File(dir, fileName);
//            FileOutputStream fos = new FileOutputStream(outputFile);

//            int bytesRead = -1;
//            byte[] buffer = new byte[1024];
//            while ((bytesRead = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//            fos.flush();
//            fos.close();
//            is.close();

//            // ファイル読み込み
//            FileInputStream fileInputStream;
//            String path2 = Environment.getExternalStorageDirectory() + "/simple/";
//            String fileName2 = "simple.json";
//            File dir2 = new File(path2);
//            File inputFile = new File(dir2, fileName2);
//            fileInputStream = new FileInputStream(inputFile);
//            byte[] readBytes = new byte[fileInputStream.available()];
//            fileInputStream.read(readBytes);
//            String json = new String(readBytes);

            // JSONのパース with Jackson
            ObjectMapper mapper = new ObjectMapper();
            Object root = mapper.readValue(json, Object.class);
            Map<?, ?> rootMap = mapper.readValue(json, Map.class);
            ArrayList nextArray = (ArrayList) rootMap.get("results");
            ArrayList mArrayList = new ArrayList<LatLngName>();

            for (int i = 0; i < nextArray.size(); i++) {
                Map<?, ?> thirdMap = (Map<?, ?>) nextArray.get(i);
                Map<?, ?> forthMap = (Map<?, ?>) ((Map<?, ?>) thirdMap.get("geometry")).get("location");
                Double lat2 = (Double) forthMap.get("lat");
                Double lng2 = (Double) forthMap.get("lng");
                String name = (String) thirdMap.get("name");
                Log.d("TAG", "lat=" + lat2 + " lng=" + lng2 + " name=" + name);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Success";
    }

    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {

    }
}
