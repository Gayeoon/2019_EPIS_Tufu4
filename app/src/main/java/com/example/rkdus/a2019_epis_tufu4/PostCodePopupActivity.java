package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.SERVER_URL;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.StringToJSON;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.printConnectionError;

public class PostCodePopupActivity extends AppCompatActivity {
    public final String POSTCODE_URL = "http://biz.epost.go.kr/KpostPortal/openapi2?regkey=2616b9bdd82727fe11562253882955";
    EditText eSearchPostCode;
    Button searchPostCodeBtn;
    RecyclerView recyclerView;
    ArrayList<PostCodeItem> arrayList = new ArrayList<>();
    PostCodeResultAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_code_popup);

        eSearchPostCode = (EditText) findViewById(R.id.searchPostCodeEditText);
        searchPostCodeBtn = (Button) findViewById(R.id.searchPostCode);
        recyclerView = (RecyclerView) findViewById(R.id.postCodeRecyclerView);


        // EditText 자판 리스너 이벤트
        eSearchPostCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH: // 자판에서 검색 모양 아이콘을 누르면
                        String getEditText = eSearchPostCode.getText().toString();
                        String URL = POSTCODE_URL + "&target=postNew&query=" +getEditText;
                        PostCodeAsyncTask postCodeAsyncTask = new PostCodeAsyncTask();
                        postCodeAsyncTask.execute(URL);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });

        searchPostCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEditText = eSearchPostCode.getText().toString();
                String URL = POSTCODE_URL + "&target=postNew&query=" +getEditText;
                PostCodeAsyncTask postCodeAsyncTask = new PostCodeAsyncTask();
                postCodeAsyncTask.execute(URL);
            }
        });
    }

    /*
    POSTCODE 얻어오기
     */
    private class PostCodeAsyncTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                // 서버에 메세지 정보 전송
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = null;
                Document doc;
                db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                return doc;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Document document) {
            arrayList.clear();
            NodeList nodeList = document.getElementsByTagName("item");
            for(int i = 0; i< nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;
                PostCodeItem postCodeItem = new PostCodeItem();
                NodeList postcd = fstElmnt.getElementsByTagName("postcd");
                postCodeItem.setPostcd(postcd.item(0).getChildNodes().item(0).getNodeValue());

                NodeList address = fstElmnt.getElementsByTagName("address");
                postCodeItem.setAddress(address.item(0).getChildNodes().item(0).getNodeValue());

                NodeList addrjibun  = fstElmnt.getElementsByTagName("addrjibun");
                postCodeItem.setAddrjibun(addrjibun.item(0).getChildNodes().item(0).getNodeValue());

                arrayList.add(postCodeItem);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new PostCodeResultAdapter(arrayList);
            adapter.resetAll(arrayList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            // RecyclerView 클릭 이벤트 초기화
            setRecyclerViewItemClick(arrayList, adapter);

            super.onPostExecute(document);
        }
    }

    /*
    RecyclerView Item 개별 클릭 리스너 설정하는 함수
     */
    private void setRecyclerViewItemClick(final ArrayList<PostCodeItem> result, PostCodeResultAdapter postCodeResultAdapter) {
        postCodeResultAdapter.setItemClick(new PostCodeResultAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                //해당 위치의 Data get
                PostCodeItem resultData = result.get(position);
                Log.d("LogGoGo", "결과 : " + resultData.getPostcd() + ", " + resultData.getAddress() + ", " + resultData.getAddrjibun());
                Intent intent = new Intent();
                intent.putExtra("data", (Serializable) resultData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
