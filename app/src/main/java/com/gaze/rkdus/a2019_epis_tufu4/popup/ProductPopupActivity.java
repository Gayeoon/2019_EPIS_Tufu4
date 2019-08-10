package com.gaze.rkdus.a2019_epis_tufu4.popup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.ProductPopupListAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.SearchListAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.item.PostCodeItem;
import com.gaze.rkdus.a2019_epis_tufu4.item.ProductItemData;
import com.gaze.rkdus.a2019_epis_tufu4.item.SearchResultData;
import com.gaze.rkdus.a2019_epis_tufu4.user.HospitalProfileActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductPopupActivity extends BaseActivity {
    RecyclerView productRecyclerView;
    Button cancelBtn;
    ProductPopupListAdapter adapter;
    ArrayList<ProductItemData> itemList = new ArrayList<>();
    int type = 0;   // 2 : outer, 3 : badge
    private final int VIEW_PRODUCTITEM = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_popup);

        productRecyclerView = (RecyclerView) findViewById(R.id.productItemView);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("type")) {
                type = intent.getIntExtra("type", 0);
            }
            else
                finishPopup();
        }

        Log.e("LogGoGo", "type : " + String.valueOf(type));
        setProductItems(type);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ProductPopupListAdapter(itemList);
        adapter.resetAll(itemList);
        productRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // RecyclerView 클릭 이벤트 초기화
        setRecyclerViewItemClick(itemList, adapter);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /*
    RecyclerView Item 개별 클릭 리스너 설정하는 함수
     */
    private void setRecyclerViewItemClick(final ArrayList<ProductItemData> result, ProductPopupListAdapter productPopupListAdapter) {
        productPopupListAdapter.setItemClick(new ProductPopupListAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                //해당 위치의 Data get
                ProductItemData resultData = result.get(position);
                Toast.makeText(getApplicationContext(), "아이템 클릭 : " + resultData.getPRODUCT_NAME() + ", " + resultData.getPRODUCT_CONTEXT(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ProductViewPopupActivity.class);
                intent.putExtra("productData", (Serializable) resultData);
                startActivityForResult(intent, VIEW_PRODUCTITEM);  // ProductViewPopupActivity 실행
            }
        });
    }

    /*
    임시 뷰로 보여줄 아이템 값 설정
     */
    private void setProductItems(int type) {
        if (type == 2)  {   // outer
            Log.e("LogGoGo", "type : " + String.valueOf(type));
            itemList.add(getProductItem(1, 2, 10000, "VOWOW 외장형 목걸이", "특수합금 제작",
                    "https://blogfiles.pstatic.net/MjAxOTA4MTBfMTc4/MDAxNTY1MzY1NDk4OTUx.X7pCqJ-UH4dWCWoz7DohBEN0TUVi1tbhvTXHv4xAebQg.wQsd2Hk6EoZmlO3B-Xf3SiayCIO29hAyXbykWgcpcRIg.JPEG.banner4/outer1.jpg",
                    "기본 외장형 목걸이를 디자인해봤습니다. 꼭 사세요.", false, true));
            itemList.add(getProductItem(2, 2, 20000, "VOWOW 블루투스 부착 외장형 목걸이", "고무로 만듬", true, true));
            itemList.add(getProductItem(3, 2, 5000, "싸구려 외장형 목걸이", "싸구려로 만듬", false, false));
        }
        else {  // type == 3 : badge
            itemList.add(getProductItem(1, type, 99900, "VOWOW 특제 등록 인식표", "어여쁨",
                    "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png",
                    "특제 등록 인식표입니다. 구매하세요.", true, true));
            itemList.add(getProductItem(2, type, 123456, "VOWOW 블루투스 등록 인식표", "고무로 만듬",
                    "https://blogfiles.pstatic.net/MjAxOTA4MTBfNDcg/MDAxNTY1MzY1MjczMTE4.8DEJ8XVdgmUHNJ_Gnb5yddBb9VkUbRIkHBVJanvkC7cg.pDacrS50BsaGlf2X0BkgTh96kRt9pqYCM-1puGnecwwg.JPEG.banner4/badge.jpg",
                    "네이버 임시 인식표입니다. 구매하세요.", false, true));
            itemList.add(getProductItem(3, type, 1111, "싸구려 등록 인식표", "싸구려로 만듬", false, false));
            itemList.add(getProductItem(4, type, 555, "고구려 등록 인식표", "고구려로 만듬", true, false));
        }
    }


    private ProductItemData getProductItem(int key, int type, int price, String name, String context, boolean soldout, boolean shippingFee) {
        return new ProductItemData(key, type, price, name, context, soldout, shippingFee);
    }

    private ProductItemData getProductItem(int key, int type, int price, String name, String context, String url, String text, boolean soldout, boolean shippingFee) {
        return new ProductItemData(key, type, price, name, context, url, text, soldout, shippingFee);
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case VIEW_PRODUCTITEM:
                if(resultCode == RESULT_OK) {
                    // TODO : 해당 아이템의 키를 받아와 예약 시 같이 보내기
                    Log.d(TAG, "해당 아이템 구매 완료!");
                }
                else {
                    Log.d(TAG, "해당 아이템 구매 취소!");
                }
                break;
            default:
                break;
        }
    }
}
