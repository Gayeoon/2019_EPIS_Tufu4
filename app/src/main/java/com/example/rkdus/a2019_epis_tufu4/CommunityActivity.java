package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * 사용자, 병원
 * 커뮤니티 액티비티
 */
public class CommunityActivity extends BaseActivity {

    // 사용자면 user 1
    //  병원이면 user 2
    public int user = 0;
    String userName = "USER";
    Bitmap bm;
    byte[] profile;
    ImageView imageView;

    ListView listView;
    MyAdapter myAdapter;

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
            view.setName(item.getName());
            view.setTime(item.getTime());
            view.setContent(item.getContent());
            view.setComment(item.getComment());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String nowTime = sdf.format(date);


//        Intent intent = getIntent();
//
//        if (intent.hasExtra("user")){
//            switch (intent.getIntExtra("user", 0)){
//                case 1:
//                    // 사용자 닉네임 같은거?
//                    break;
//
//                case 2:
//                    userName = intent.getStringExtra("hosName");
//                    profile = intent.getByteArrayExtra("profile");
//                    bm = BitmapFactory.decodeByteArray(profile, 0, profile.length);
//
//                    //imageView.setImageBitmap(bm);
//                    // 프로필 사진 지정
//            }
//        }

        listView = (ListView) findViewById(R.id.communityList);

        myAdapter = new MyAdapter();

        ArrayList<String> titles = new ArrayList<>();

        myAdapter.addItem(new CommunityItem("김가연", nowTime, "아이에게 칩을 심는다고 바늘을 찌르는 것이 신경쓰여요. 꼭 내장형이어야 하나요?", "댓글 : 2개"));
        myAdapter.addItem(new CommunityItem("user1", nowTime, "제 친구네 강아지가 내장형 마이크로 칩을 했는데 심은 위치에서 이동이 발생했다고 해요. 괜찮은건가요? ", "댓글 : 4개"));
        myAdapter.addItem(new CommunityItem("GAYEON", nowTime, "피부에 칩을 삽입하는 시술이라 피부에 이상반응이 올 수도 있을 것 같은데 부작용은 없나요?", "댓글 : 0개"));

       listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final MessageItem item = (MessageItem) myAdapter.getItem(i);
                final String name = item.getName();

                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                // 글 보여지는 intent 만들기
            }
        });
    }
}
