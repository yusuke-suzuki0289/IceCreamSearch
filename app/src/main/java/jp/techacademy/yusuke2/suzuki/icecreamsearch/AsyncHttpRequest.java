package jp.techacademy.yusuke2.suzuki.icecreamsearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static jp.techacademy.yusuke2.suzuki.icecreamsearch.R.id.map;

/**
 * Created by yusus on 2016/10/18.
 */

public class AsyncHttpRequest extends AsyncTask<Uri, Void, String> {

    private String json = "";
    private JSONObject jObj = null;
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

            final InputStream in = con.getInputStream();
            final InputStreamReader inReader = new InputStreamReader(in);
            final BufferedReader bufReader = new BufferedReader(inReader);

            String line;
            StringBuilder strResult = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                strResult.append(line);
            }

            json = strResult.toString();
            try {
                jObj = new JSONObject(json);
                        JSONArray array = (JSONArray) jObj.getJSONArray("results");
//                        for (初期化式; 条件式; 変化式) {
                        JSONObject obj0 = (JSONObject) array.get(0);
                        JSONObject geometry0 = (JSONObject) obj0.getJSONObject("geometry");
                        JSONObject location0 = (JSONObject) geometry0.getJSONObject("location");
                        double lat0 = location0.getDouble("lat");
                        double lng0 = location0.getDouble("lng");
                        LatLng latLng0 = new LatLng(lat0, lng0);
//                        JSONObject icon0 = (JSONObject) obj0.getJSONObject("icon");
//                        JSONObject id0 = (JSONObject) obj0.getJSONObject("id");
//                        JSONObject name0 = (JSONObject) obj0.getJSONObject("name");

                        Intent intent0 = new Intent();
                        intent0.setClassName("jp.techacademy.yusuke2.suzuki.icecreamsearch", "jp.techacademy.yusuke2.suzuki.icecreamsearch.MapsActivity");
                        intent0.putExtra("latLng0",latLng0);


            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return "Success";

//            BufferedInputStream is = new BufferedInputStream(con.getInputStream());
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();

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

//            // JSONのパース with Jackson
//            ObjectMapper mapper = new ObjectMapper();
//            Object root = mapper.readValue(json, Object.class);
//            Map<?, ?> rootMap = mapper.readValue(json, Map.class);
//            ArrayList nextArray = (ArrayList) rootMap.get("results");
//            ArrayList mArrayList = new ArrayList<LatLngName>();
//
//            for (int i = 0; i < nextArray.size(); i++) {
//                Map<?, ?> thirdMap = (Map<?, ?>) nextArray.get(i);
//                Map<?, ?> forthMap = (Map<?, ?>) ((Map<?, ?>) thirdMap.get("geometry")).get("location");
//                Double lat2 = (Double) forthMap.get("lat");
//                Double lng2 = (Double) forthMap.get("lng");
//                String name = (String) thirdMap.get("name");
//                Log.d("TAG", "lat=" + lat2 + " lng=" + lng2 + " name=" + name);
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

//        } catch (JSONException e) {
//            e.printStackTrace();
        }

        return "Success";
    }

    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {

    }
}
