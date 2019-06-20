package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AlarmActivity extends BaseActivity {

    ListView builtInList, externalList;
    MyAdapter builtInAdapter, externalAdapter;
    String builtInName[] = new String[50];
    String externalName[] = new String[50];

    class MyAdapter extends BaseAdapter {
        ArrayList<MessageItem> items = new ArrayList<MessageItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(MessageItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            MessageView view = new MessageView(getApplicationContext());

            MessageItem item = items.get(i);
            view.setName(item.getName());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        builtInList = (ListView) findViewById(R.id.builtInList);
        externalList = (ListView) findViewById(R.id.externalList);

        builtInAdapter = new MyAdapter();
        externalAdapter = new MyAdapter();

        ArrayList<String> titles = new ArrayList<>();

        BuiltInList(builtInName);
        ExternalList(externalName);

        for (int i = 0; i < builtInName.length; i++) {
            builtInAdapter.addItem(new MessageItem(builtInName[i]));
        }

        for (int i = 0; i < externalName.length; i++) {
            externalAdapter.addItem(new MessageItem(externalName[i]));
        }

        builtInList.setAdapter(builtInAdapter);
        externalList.setAdapter(externalAdapter);

        builtInList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final MessageItem item = (MessageItem) builtInAdapter.getItem(i);
                final String name = item.getName();

                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                intent.putExtra("name", name);
                startActivityForResult(intent, 4491);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 소리 템플릿
                case 4491:
                    String name = data.getStringExtra("choose");
                    Delete(name);

                    finish();

                    startActivity(new Intent(AlarmActivity.this, AlarmActivity.class));

            }
        }
    }

    public void BuiltInList(String[] builtInName) {
        // To 지원
        // 저기 BuiltInname 배열에 내장형 신청한 소유자 이름 좀 넣어줭!
        // + 예약 확인 안한 사람만!
    }

    public void ExternalList(String[] externalName) {
        // To 지원
        // 저기 Externalname 배열에 내장형 신청한 소유자 이름 좀 넣어줭!
        // + 예약 확인 안한 사람만!
    }

    public void Delete (String name){
        // To 지원
        // state를 예약 확인 상태로 바꿔줘!
    }
}

