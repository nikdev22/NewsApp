package com.example.nik.jsonfour;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
     private static final String URL = "https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=YOUR_API_KEY";
     //private RequestQueue requestQueue;
    ListView listView;
    ProgressBar progressBar;
    ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE  = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URL2IMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt\"";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestQueue = Volley.newRequestQueue(this);
        //getJsonObject(URL);
        listView = findViewById(R.id.listNews);
        progressBar = findViewById(R.id.loader);
        listView.setEmptyView(progressBar);


        if(Function.isNetworkAvailable(getApplicationContext())){
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        }else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }


    }
    /*private void getJsonObject(String URL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response",response.getString("status"));
                    JSONArray jsonArray = response.getJSONArray("articles");
                    Log.d("articles",jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
     requestQueue.add(jsonObjectRequest);

    } //getJsonFunction Ends
*/

    class DownloadNews extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String xml = "";
            String urlParameters = "";
            xml = Function.executeGet(URL,urlParameters);
            return xml;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.length()>10){
                try {
                    JSONObject jsonResponse = new JSONObject(s);
                    JSONArray jsonArray = jsonResponse.getJSONArray("articles");
                    for(int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put(KEY_AUTHOR,jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_TITLE,jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_DESCRIPTION,jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_URL,jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URL2IMAGE,jsonObject.optString(KEY_URL2IMAGE).toString());
                        map.put(KEY_PUBLISHEDAT,jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected Error" ,Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

                ListRow listRow = new ListRow(MainActivity.this,dataList);
                listView.setAdapter(listRow);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this,ActivityDetail.class);
                        intent.putExtra("url",dataList.get(+i).get(KEY_URL));
                        startActivity(intent);
                    }
                });

            }else{
                Toast.makeText(getApplicationContext(),"No news found",Toast.LENGTH_LONG).show();
            }
        }
    }

}//end of class

