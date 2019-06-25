package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewReservationActivity extends BaseActivity {

    ListView internalList, externalList, dogtagList;
    MyAdapter internalAdapter, externalAdapter, dogtagAdapter;

    ImageButton internalBtn, externalBtn, dogtagBtn, back;
    TextView internalCount, externalCount, dogtagCount;

    FrameLayout internalBack, externalBack, dogtagBack;

    boolean internalopen, externalopen, dogtagopen = false;

    String id;

    class MyAdapter extends BaseAdapter {
        ArrayList<NewReservationItem> items = new ArrayList<NewReservationItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(NewReservationItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            NewReservationView view = new NewReservationView(getApplicationContext());

            NewReservationItem item = items.get(i);
            view.setName(item.getName());
            view.setTime(item.getTime());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);

//        Intent getintet = getIntent();
//        id = getintet.getStringExtra("id");

        internalBtn = (ImageButton) findViewById(R.id.internal);
        externalBtn = (ImageButton) findViewById(R.id.external);
        dogtagBtn = (ImageButton) findViewById(R.id.dogtag);

        internalList = (ListView) findViewById(R.id.internalList);
        externalList = (ListView) findViewById(R.id.externalList);
        dogtagList = (ListView) findViewById(R.id.dogtagList);

        internalCount = (TextView) findViewById(R.id.internal_count);
        externalCount = (TextView) findViewById(R.id.external_count);
        dogtagCount = (TextView) findViewById(R.id.dogtag_count);

        internalBack = (FrameLayout) findViewById(R.id.internalBack);
        externalBack = (FrameLayout) findViewById(R.id.externalBack);
        dogtagBack = (FrameLayout) findViewById(R.id.dogtagBack);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        internalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internalopen) {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    internalopen = false;
                } else {
                    internalList.setVisibility(View.VISIBLE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.VISIBLE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    internalopen = true;
                    externalopen = false;
                    dogtagopen = false;
                }
            }
        });

        externalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalopen) {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    externalopen = false;
                } else {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.VISIBLE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.VISIBLE);
                    dogtagBack.setVisibility(View.GONE);

                    internalopen = false;
                    externalopen = true;
                    dogtagopen = false;
                }
            }
        });

        dogtagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dogtagopen) {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    dogtagopen = false;
                } else {

                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.VISIBLE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.VISIBLE);

                    internalopen = false;
                    externalopen = false;
                    dogtagopen = true;
                }
            }
        });

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String nowTime = sdf.format(date);

        internalCount.setText("6");
        externalCount.setText("11");
        dogtagCount.setText("2");

        internalAdapter = new MyAdapter();

        ArrayList<String> intitles = new ArrayList<>();

        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        internalList.setAdapter(internalAdapter);

        internalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final NewReservationItem item = (NewReservationItem) internalAdapter.getItem(i);
                final String name = item.getName();

                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                //intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        externalAdapter = new MyAdapter();

        ArrayList<String> extitles = new ArrayList<>();

        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
        externalList.setAdapter(externalAdapter);

        externalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final NewReservationItem item = (NewReservationItem) externalAdapter.getItem(i);
                final String name = item.getName();

                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                //intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        dogtagAdapter = new MyAdapter();

        ArrayList<String> titles = new ArrayList<>();

        dogtagAdapter.addItem(new NewReservationItem("김가연", nowTime));
        dogtagAdapter.addItem(new NewReservationItem("김가연", nowTime));

        dogtagList.setAdapter(dogtagAdapter);

        dogtagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final NewReservationItem item = (NewReservationItem) dogtagAdapter.getItem(i);
                final String name = item.getName();

                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                //intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });
    }


}
