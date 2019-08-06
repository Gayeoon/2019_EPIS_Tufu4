package com.gaze.rkdus.a2019_epis_tufu4;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PopupActivity extends Activity {

    TextView bank_name;
    ImageView image;
    View bank_var, company_var;
    MyAdapter bank, company;
    ListView listView;
    TextView bankTxt, companyTxt;
    int check = 0;
    // 0 = bank, 1 = company

    String hospital, name, num1, num2, num3, account=null, id=null, pw=null, pwcheck=null;

    class MyAdapter extends BaseAdapter {
        ArrayList<BankItem> items = new ArrayList<BankItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(BankItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            BankView view = new BankView(getApplicationContext());

            BankItem item = items.get(i);
            view.setBankName(item.getBankName());
            view.setImage(item.getImage());

            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        Intent intent = getIntent();

        hospital = intent.getStringExtra("hospital");
        name = intent.getStringExtra("name");
        num1 = intent.getStringExtra("num1");
        num2 = intent.getStringExtra("num2");
        num3 = intent.getStringExtra("num3");

        if (intent.hasExtra("account")){
            account = intent.getStringExtra("account");
        }
        if (intent.hasExtra("id")){
            id = intent.getStringExtra("id");
        }
        if (intent.hasExtra("pw")){
            pw = intent.getStringExtra("pw");
        }
        if (intent.hasExtra("pwcheck")){
            pwcheck = intent.getStringExtra("pwcheck");
        }

        listView = (ListView) findViewById(R.id.bankList);
        bankTxt = (TextView) findViewById(R.id.bank);
        companyTxt = (TextView) findViewById(R.id.company);

        bank_var = (View) findViewById(R.id.bank_var);
        company_var = (View) findViewById(R.id.company_var);

        bank = new MyAdapter();
        company = new MyAdapter();

        bank.addItem(new BankItem("국민", R.drawable.kb));
        bank.addItem(new BankItem("기업", R.drawable.ibk));
        bank.addItem(new BankItem("농협", R.drawable.nh));
        bank.addItem(new BankItem("신한(구조흥포함)", R.drawable.sh));
        bank.addItem(new BankItem("우체국", R.drawable.post));
        bank.addItem(new BankItem("SC(스탠다드차타드)", R.drawable.sc));
        bank.addItem(new BankItem("KEB하나(구외환포함)", R.drawable.hana));
        bank.addItem(new BankItem("한국씨티(구한미)", R.drawable.citi));
        bank.addItem(new BankItem("우리", R.drawable.bws));
        bank.addItem(new BankItem("경남", R.drawable.bnk));
        bank.addItem(new BankItem("광주", R.drawable.s));
        bank.addItem(new BankItem("대구", R.drawable.dgb));
        bank.addItem(new BankItem("도이치", R.drawable.doich));
        bank.addItem(new BankItem("부산", R.drawable.bs));
        bank.addItem(new BankItem("수협", R.drawable.su));
        bank.addItem(new BankItem("전북", R.drawable.jun));
        bank.addItem(new BankItem("제주", R.drawable.sh));
        bank.addItem(new BankItem("새마을금고", R.drawable.sm));
        bank.addItem(new BankItem("신용협동조합", R.drawable.shj));
        bank.addItem(new BankItem("카카오뱅크", R.drawable.kakao));

        company.addItem(new BankItem("NH투자증권", 0));
        company.addItem(new BankItem("유안타증권", 0));
        company.addItem(new BankItem("KB증권", 0));
        company.addItem(new BankItem("미래에셋대우", 0));
        company.addItem(new BankItem("삼성증권", 0));
        company.addItem(new BankItem("한국투자증권", 0));
        company.addItem(new BankItem("교보증권", 0));
        company.addItem(new BankItem("하이투자증권", 0));
        company.addItem(new BankItem("현대차증권", 0));
        company.addItem(new BankItem("SK증권", 0));
        company.addItem(new BankItem("한화투자증권", 0));
        company.addItem(new BankItem("하나금융투자", 0));
        company.addItem(new BankItem("신한금융투자", 0));
        company.addItem(new BankItem("유진투자증권", 0));
        company.addItem(new BankItem("메리츠종합금융증권", 0));
        company.addItem(new BankItem("신영증권", 0));
        company.addItem(new BankItem("이베스트투자증권", 0));
        company.addItem(new BankItem("케이프증권(구LIG)", 0));
        company.addItem(new BankItem("부국증권", 0));
        company.addItem(new BankItem("대신증권", 0));
        company.addItem(new BankItem("키움증권", 0));
        company.addItem(new BankItem("DB금융투자", 0));
        company.addItem(new BankItem("한국포스증권", 0));
        company.addItem(new BankItem("케이티비투자증권", 0));

        listView.setAdapter(bank);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final BankItem item = (BankItem) bank.getItem(i);

                TextView oTextWritten = (TextView) view.findViewById(R.id.bank_name);

                String bank_nameTxt = oTextWritten.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("hospital", hospital);
                intent.putExtra("num1", num1);
                intent.putExtra("name", name);
                intent.putExtra("num2", num2);
                intent.putExtra("num3", num3);
                intent.putExtra("bank", bank_nameTxt);
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        });

        bankTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_var.setBackgroundColor(PopupActivity.this.getResources().getColor(R.color.mainColor));
                company_var.setBackgroundColor(PopupActivity.this.getResources().getColor(R.color.colorGray));

                listView.setAdapter(bank);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final BankItem item = (BankItem) bank.getItem(i);

                        TextView oTextWritten = (TextView) view.findViewById(R.id.bank_name);

                        String bank_nameTxt = oTextWritten.getText().toString();

                        Intent intent = new Intent();
                        intent.putExtra("hospital", hospital);
                        intent.putExtra("num1", num1);
                        intent.putExtra("name", name);
                        intent.putExtra("num2", num2);
                        intent.putExtra("num3", num3);
                        intent.putExtra("bank", bank_nameTxt);
                        setResult(RESULT_OK, intent);

                        //액티비티(팝업) 닫기
                        finish();
                    }
                });

                listView.invalidate();
            }
        });

        companyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_var.setBackgroundColor(PopupActivity.this.getResources().getColor(R.color.colorGray));
                company_var.setBackgroundColor(PopupActivity.this.getResources().getColor(R.color.mainColor));

                listView.setAdapter(company);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final BankItem item = (BankItem) company.getItem(i);

                        TextView oTextWritten = (TextView) view.findViewById(R.id.bank_name);

                        String bank_nameTxt = oTextWritten.getText().toString();

                        Intent intent = new Intent();
                        intent.putExtra("hospital", hospital);
                        intent.putExtra("num1", num1);
                        intent.putExtra("name", name);
                        intent.putExtra("num2", num2);
                        intent.putExtra("num3", num3);
                        intent.putExtra("bank", bank_nameTxt);
                        setResult(RESULT_OK, intent);

                        //액티비티(팝업) 닫기
                        finish();
                    }
                });

                listView.invalidate();
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
