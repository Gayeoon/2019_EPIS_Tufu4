package com.example.rkdus.a2019_epis_tufu4;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 *  CommunityActivity
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class CommunityActivity extends BaseActivity {
    public static final String TAG = "CommunityActivity";

    CommunityItem[] itemArray = new CommunityItem[100];

    // 사용자면 user 1
    //  병원이면 user 2
    int user_state = 0;

    // 제목 select 2
    // 작성자 select 1
    public int select = 1;

    String user = "USER";
    Bitmap bm;
    byte[] profile;
    ImageView imageView;

    ListView listView;
    MyAdapter myAdapter;

    ImageButton writebtn;
    ImageButton search;
    TextView one, two, three, four, five, search_input;

    ImageButton pre, next;
    String title, written, key;
    int count = 0;
    int num_two=16, num_three=24, num_four=32, num_five=40;

    class MyAdapter extends BaseAdapter {
        ArrayList<CommunityItem> items = new ArrayList<CommunityItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(CommunityItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            CommunityView view = new CommunityView(getApplicationContext());

            CommunityItem item = items.get(i);
            view.setTitle(item.getTitle());
            view.setWritten(item.getWritten());

            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Intent intent = getIntent();

        if (intent.hasExtra("user")){
            switch (intent.getIntExtra("user", 0)){
                case 1:
                    user_state = 1;
                    user = intent.getStringExtra("userName");
                    break;

                case 2:
                    user_state = 2;
                    user = intent.getStringExtra("hosName");

//                    profile = intent.getByteArrayExtra("profile");
//                    bm = BitmapFactory.decodeByteArray(profile, 0, profile.length);
//
//                    imageView.setImageBitmap(bm);
//                    // 프로필 사진 지정
            }
        }

        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);
        three = (TextView) findViewById(R.id.three);
        four = (TextView) findViewById(R.id.four);
        five = (TextView) findViewById(R.id.five);
        pre = (ImageButton) findViewById(R.id.pre);
        next = (ImageButton) findViewById(R.id.next);

        writebtn = (ImageButton) findViewById(R.id.write);
        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteCommunityActivity.class);
                intent.putExtra("id", user);
                intent.putExtra("count", count);
                intent.putExtra("state", user_state);
                startActivityForResult(intent, 1111);
            }
        });

        search = (ImageButton) findViewById(R.id.search);
        search_input = (TextView) findViewById(R.id.search_input);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = search_input.getText().toString();
                new SearchCommunity().execute(getResources().getString(R.string.url) + "/getSearchCommunity");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter = new MyAdapter();

                for (int i = 0; i < 8; i++) {
                    myAdapter.addItem(itemArray[i]);
                }

                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final CommunityItem item = (CommunityItem) myAdapter.getItem(i);

                        TextView oTextWritten = (TextView) view.findViewById(R.id.written);
                        TextView oTextTitle = (TextView) view.findViewById(R.id.title);

                        title = oTextTitle.getText().toString();
                        written = oTextWritten.getText().toString();

                        Intent intent = new Intent(getApplicationContext(), ShowCommunityActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("user", user);
                        intent.putExtra("written", written);
                        intent.putExtra("index", item.getIndex());
                        intent.putExtra("state", user_state);
                        startActivityForResult(intent, 1111);
                    }
                });

                myAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetInvalidated();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter = new MyAdapter();

                for (int i = 8; i < 8+num_two; i++) {
                    myAdapter.addItem(itemArray[i]);
                }
                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final CommunityItem item = (CommunityItem) myAdapter.getItem(i);

                        TextView oTextWritten = (TextView) view.findViewById(R.id.written);
                        TextView oTextTitle = (TextView) view.findViewById(R.id.title);

                        title = oTextTitle.getText().toString();
                        written = oTextWritten.getText().toString();

                        Intent intent = new Intent(getApplicationContext(), ShowCommunityActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("user", user);
                        intent.putExtra("written", written);
                        intent.putExtra("index", item.getIndex());
                        intent.putExtra("state", user_state);
                        startActivityForResult(intent, 1111);
                    }
                });

                myAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetInvalidated();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter = new MyAdapter();

                for (int i = num_two; i < num_two+num_three; i++) {
                    myAdapter.addItem(itemArray[i]);
                }

                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final CommunityItem item = (CommunityItem) myAdapter.getItem(i);

                        TextView oTextWritten = (TextView) view.findViewById(R.id.written);
                        TextView oTextTitle = (TextView) view.findViewById(R.id.title);

                        title = oTextTitle.getText().toString();
                        written = oTextWritten.getText().toString();

                        Intent intent = new Intent(getApplicationContext(), ShowCommunityActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("user", user);
                        intent.putExtra("written", written);
                        intent.putExtra("index", item.getIndex());
                        intent.putExtra("state", user_state);
                        startActivityForResult(intent, 1111);
                    }
                });

                myAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetInvalidated();
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter = new MyAdapter();

                for (int i = num_three; i < num_three+num_four; i++) {
                    myAdapter.addItem(itemArray[i]);
                }

                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final CommunityItem item = (CommunityItem) myAdapter.getItem(i);

                        TextView oTextWritten = (TextView) view.findViewById(R.id.written);
                        TextView oTextTitle = (TextView) view.findViewById(R.id.title);

                        title = oTextTitle.getText().toString();
                        written = oTextWritten.getText().toString();

                        Intent intent = new Intent(getApplicationContext(), ShowCommunityActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("user", user);
                        intent.putExtra("written", written);
                        intent.putExtra("index", item.getIndex());
                        intent.putExtra("state", user_state);
                        startActivityForResult(intent, 1111);
                    }
                });

                myAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetInvalidated();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter = new MyAdapter();

                for (int i = num_four; i < num_four+num_five; i++) {
                    myAdapter.addItem(itemArray[i]);
                }

                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final CommunityItem item = (CommunityItem) myAdapter.getItem(i);

                        TextView oTextWritten = (TextView) view.findViewById(R.id.written);
                        TextView oTextTitle = (TextView) view.findViewById(R.id.title);

                        title = oTextTitle.getText().toString();
                        written = oTextWritten.getText().toString();

                        Intent intent = new Intent(getApplicationContext(), ShowCommunityActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("user", user);
                        intent.putExtra("written", written);
                        intent.putExtra("index", item.getIndex());
                        intent.putExtra("state", user_state);
                        startActivityForResult(intent, 1111);
                    }
                });

                myAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetInvalidated();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.select, R.layout.spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    select = 1;
                } else {
                    select = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new CommunityListData().execute(getResources().getString(R.string.url) + "/getCommunityListData");

        listView = (ListView) findViewById(R.id.communityList);
//
//        myAdapter = new MyAdapter();
//
//        ArrayList<String> titles = new ArrayList<>();
//
//        myAdapter.addItem(new CommunityItem("내장형 시렁", "김가연", 1));
//        myAdapter.addItem(new CommunityItem("나의 맥북 최고 >0< ㅎㅎ", "정지원", 2));
//        myAdapter.addItem(new CommunityItem("마이크로칩 무서웡", "이해원", 3));
//        myAdapter.addItem(new CommunityItem("k 해커톤 멀다", "박지민", 4));
//
//       listView.setAdapter(myAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final CommunityItem item = (CommunityItem) myAdapter.getItem(i);
//                final String name = item.getWritten();
//
//                Toast.makeText(getApplicationContext(), item.getIndex()+"", Toast.LENGTH_SHORT).show();
//
//                // 글 보여지는 intent 만들기
//            }
//        });

    }

    /* CommunityListData : 전체 커뮤니티 글 리스트 출력
     *
     * select 1 : 작성자에서 검색
     * select 2 : 제목에서 검색
     *
     * Uri  --->   /getCommunityListData
     * Parm  --->   {"user":1} 전송
     * Result  --->   {"result":{"community":[{"TITLE":"뿡이는 뭐할까","ARTICLE_AUTHOR":"김가연","ARTICLE_INDEX":1},{"TITLE":"지원아 일어나","ARTICLE_AUTHOR":"정지원","ARTICLE_INDEX":2}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : community 넣어서!!  */

    public class CommunityListData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                jsonObject.accumulate("user", 1);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {

                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                    Log.e(TAG, jsonObject.toString());
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject json = null;
            JSONArray state = null;

            myAdapter = new MyAdapter();

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new CommunityListData().execute(getResources().getString(R.string.url) + "/getCommunityListData");
                } else {
                    JSONObject jsonObject = json.getJSONObject("result");

                    state = jsonObject.getJSONArray("community");

                    count =  0;
                    for (int i = 0; i < state.length(); i++) {
                        JSONObject jsonTemp = state.getJSONObject(i);
                        itemArray[count] = new CommunityItem(jsonTemp.getString("TITLE"), jsonTemp.getString("ARTICLE_AUTHOR"), jsonTemp.getInt("ARTICLE_INDEX"));
                        count++;
                    }

                    viewCount(count);

                    if (count < 8) {
                        for (int i = 0; i < count; i++) {
                            myAdapter.addItem(itemArray[i]);
                            one.setEnabled(false);
                        }
                    } else {
                        for (int i = 0; i < 8; i++) {
                            myAdapter.addItem(itemArray[i]);
                        }
                    }

                    listView.setAdapter(myAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final CommunityItem item = (CommunityItem) myAdapter.getItem(i);

                            TextView oTextWritten = (TextView) view.findViewById(R.id.written);
                            TextView oTextTitle = (TextView) view.findViewById(R.id.title);

                            title = oTextTitle.getText().toString();
                            written = oTextWritten.getText().toString();

                            Intent intent = new Intent(getApplicationContext(), ShowCommunityActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("user", user);
                            intent.putExtra("written", written);
                            intent.putExtra("index", item.getIndex());
                            intent.putExtra("state", user_state);
                            startActivityForResult(intent, 1111);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e(TAG, result);

        }
    }

    private void viewCount(int count) {
        if (8 < count && count <= 16) {
            two.setVisibility(View.VISIBLE);
            num_two = count - 8;
        } else if (16 < count && count <= 24) {
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);
            num_three = count - 16;
        } else if (24 < count && count <= 32) {
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);
            four.setVisibility(View.VISIBLE);
            num_four = count - 24;
        } else if (32 < count && count <= 40) {
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);
            four.setVisibility(View.VISIBLE);
            five.setVisibility(View.VISIBLE);
            num_five = count - 32;
        }
    }

    /* SearchCommunity : ID값과 key 값을 통해 값이 포함된 리스트 출력
     *
     * select 1 : 작성자에서 검색
     * select 2 : 제목에서 검색
     *
     * Uri  --->   /getSearchCommunity
     * Parm  --->   {"user":{"select":1,"key":"뿡이"}} 전송
     * Result  --->   {"result":{"community":[{"TITLE":"뿡이는 뭐할까","ARTICLE_AUTHOR":"김가연","ARTICLE_INDEX":1},{"TITLE":"지원아 일어나","ARTICLE_AUTHOR":"정지원","ARTICLE_INDEX":2}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : community 넣어서!!  */

    public class SearchCommunity extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("select", select);
                tmp.accumulate("key", key);

                jsonObject.accumulate("user", tmp);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {

                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                    Log.e(TAG, jsonObject.toString());
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject json = null;
            JSONArray state = null;

            myAdapter = new MyAdapter();

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new CommunityListData().execute(getResources().getString(R.string.url) + "/getSearchCommunity");
                } else {
                    JSONObject jsonObject = json.getJSONObject("result");

                    state = jsonObject.getJSONArray("community");
                    count =  0;
                    for (int i = 0; i < state.length(); i++) {
                        JSONObject jsonTemp = state.getJSONObject(i);
                        itemArray[count] = new CommunityItem(jsonTemp.getString("TITLE"), jsonTemp.getString("ARTICLE_AUTHOR"), jsonTemp.getInt("ARTICLE_INDEX"));
                        count++;
                    }

                    viewCount(count);

                    if (count < 8) {
                        for (int i = 0; i < count; i++) {
                            myAdapter.addItem(itemArray[i]);
                            one.setEnabled(false);
                        }
                    } else {
                        for (int i = 0; i < 8; i++) {
                            myAdapter.addItem(itemArray[i]);
                        }
                    }

                    listView.setAdapter(myAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final CommunityItem item = (CommunityItem) myAdapter.getItem(i);

                            TextView oTextWritten = (TextView) view.findViewById(R.id.written);
                            TextView oTextTitle = (TextView) view.findViewById(R.id.title);

                            title = oTextTitle.getText().toString();
                            written = oTextWritten.getText().toString();

                            Intent intent = new Intent(getApplicationContext(), ShowCommunityActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("user", user);
                            intent.putExtra("written", written);
                            intent.putExtra("index", item.getIndex());
                            intent.putExtra("state", user_state);
                            startActivityForResult(intent, 1111);
                        }
                    });

                    myAdapter.notifyDataSetChanged();
                    myAdapter.notifyDataSetInvalidated();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e(TAG, result);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

//    public void startAni(){
//        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
//
//        animationView.setAnimation("loading.json");
//        animationView.loop(true);
//        //anim.setVisibility(View.VISIBLE);
//        animationView.playAnimation();
//        animationView.addAnimatorListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                animationView.setRepeatCount(3);
//                animationView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                //anim.setVisibility(View.GONE);
//                animationView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//            }
//        });
//    }

}
