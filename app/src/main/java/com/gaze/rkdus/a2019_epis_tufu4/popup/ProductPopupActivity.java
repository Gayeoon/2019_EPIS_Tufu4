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
import android.widget.TextView;
import android.widget.Toast;

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.HospitalReviewListAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.ProductPopupListAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.SearchListAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.item.PostCodeItem;
import com.gaze.rkdus.a2019_epis_tufu4.item.ProductItemData;
import com.gaze.rkdus.a2019_epis_tufu4.item.ReviewListItem;
import com.gaze.rkdus.a2019_epis_tufu4.item.SearchResultData;
import com.gaze.rkdus.a2019_epis_tufu4.user.HospitalProfileActivity;
import com.gaze.rkdus.a2019_epis_tufu4.utils.ProductService;
import com.gaze.rkdus.a2019_epis_tufu4.utils.ReviewService;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HEAD;

public class ProductPopupActivity extends BaseActivity {
    RecyclerView productRecyclerView;
    Button cancelBtn;
    ProductPopupListAdapter adapter;
    TextView tvProductType;
    int type = 0;   // 2 : outer, 3 : badge
    private final int VIEW_PRODUCTITEM = 10;

    private Retrofit retrofit;
    private ProductService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_popup);

        productRecyclerView = (RecyclerView) findViewById(R.id.productItemView);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        tvProductType = (TextView) findViewById(R.id.tvProductType);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("type")) {
                type = intent.getIntExtra("type", 0);
            }
            else
                finishPopup();
        }

        Log.e("LogGoGo", "type : " + String.valueOf(type));
        if (type == 2)
            tvProductType.setText("외장형");
        setProductItems(type);

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
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();
        service = retrofit.create(ProductService.class);

        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("type", type);

        service.resultProductListRepos(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<ProductItemData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ArrayList<ProductItemData> reviewListItems) {
                        Log.d(TAG, "받아오기 성공!");
                        if (reviewListItems.size() > 0) {
                            for (int i = 0; i < reviewListItems.size(); i++) {
                                Log.d(TAG, "img_list : " + reviewListItems.get(i).getImg_list());
                            }
                            // 상품 리스트 출력
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                            productRecyclerView.setLayoutManager(gridLayoutManager);
                            adapter = new ProductPopupListAdapter(reviewListItems);
                            adapter.resetAll(reviewListItems);
                            productRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            // RecyclerView 클릭 이벤트 초기화
                            setRecyclerViewItemClick(reviewListItems, adapter);
                        }
                        else {
                            Log.d(TAG, "상품이 없음");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "받아오기 실패! 실패 사유 : " + e);
                    }
                });

        if (type == 2)  {   // outer
            Log.e("LogGoGo", "type : " + String.valueOf(type));
//            itemList.add(getProductItem(1, 2, 10000, "VOWOW 외장형 목걸이", "특수합금 제작",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjAg/MDAxNTY2MDE5NDA0MjU1.3Jh9KnAfLlc-ztlja3FB8tMdga9oTAn_X4jsGX_x3wog.0VmBQzoJauPEmn9dpHg4rA9EUbNlt5UlZUNQ9QC5VFYg.PNG.banner4/2-1.png",
//                    "기본 외장형 목걸이를 디자인해봤습니다. 꼭 사세요.", false, true));
//            itemList.add(getProductItem(1, 2, 10000, "VOWOW 외장형 목걸이", "특수합금 제작",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjAg/MDAxNTY2MDE5NDA0MjU1.3Jh9KnAfLlc-ztlja3FB8tMdga9oTAn_X4jsGX_x3wog.0VmBQzoJauPEmn9dpHg4rA9EUbNlt5UlZUNQ9QC5VFYg.PNG.banner4/2-2.png",
//                    "기본 외장형 목걸이를 디자인해봤습니다. 꼭 사세요.", false, true));
//            itemList.add(getProductItem(1, 2, 10000, "VOWOW 외장형 목걸이", "특수합금 제작",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjAg/MDAxNTY2MDE5NDA0MjU1.3Jh9KnAfLlc-ztlja3FB8tMdga9oTAn_X4jsGX_x3wog.0VmBQzoJauPEmn9dpHg4rA9EUbNlt5UlZUNQ9QC5VFYg.PNG.banner4/2-3.png",
//                    "기본 외장형 목걸이를 디자인해봤습니다. 꼭 사세요.", false, true));
//            itemList.add(getProductItem(1, 2, 10000, "VOWOW 외장형 목걸이", "특수합금 제작",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjAg/MDAxNTY2MDE5NDA0MjU1.3Jh9KnAfLlc-ztlja3FB8tMdga9oTAn_X4jsGX_x3wog.0VmBQzoJauPEmn9dpHg4rA9EUbNlt5UlZUNQ9QC5VFYg.PNG.banner4/2-4.png",
//                    "기본 외장형 목걸이를 디자인해봤습니다. 꼭 사세요.", false, true));
//            itemList.add(getProductItem(1, 2, 10000, "VOWOW 외장형 목걸이", "특수합금 제작",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjAg/MDAxNTY2MDE5NDA0MjU1.3Jh9KnAfLlc-ztlja3FB8tMdga9oTAn_X4jsGX_x3wog.0VmBQzoJauPEmn9dpHg4rA9EUbNlt5UlZUNQ9QC5VFYg.PNG.banner4/2-5.png",
//                    "기본 외장형 목걸이를 디자인해봤습니다. 꼭 사세요.", false, true));
//            itemList.add(getProductItem(1, 2, 10000, "VOWOW 외장형 목걸이", "특수합금 제작",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjAg/MDAxNTY2MDE5NDA0MjU1.3Jh9KnAfLlc-ztlja3FB8tMdga9oTAn_X4jsGX_x3wog.0VmBQzoJauPEmn9dpHg4rA9EUbNlt5UlZUNQ9QC5VFYg.PNG.banner4/2-6.png",
//                    "기본 외장형 목걸이를 디자인해봤습니다. 꼭 사세요.", false, true));
        }
        else {  // type == 3 : badge
//            itemList.add(getProductItem(1, type, 99900, "VOWOW 특제 등록 인식표", "어여쁨",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjYy/MDAxNTY2MDE5MzUzMDg1.FfqQlaMxWjK71nsFrrmazZi40xWjk31xm4tOkxrFJP0g.e-4sUyqs8vpdvpCc1B1eD1ZohLQER-ureh4P8EFsDwog.PNG.banner4/1-1.png",
//                    "특제 등록 인식표입니다. 구매하세요.", true, true));
//            itemList.add(getProductItem(2, type, 123456, "VOWOW 블루투스 등록 인식표", "고무로 만듬",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjUw/MDAxNTY2MDE5MzU5Nzc0.c5STF3Yem_ZBBcrotD0nB0EHLaiANBWRnPSVqza-S-cg.zbO6Frp2FTZPlxwO6iWicZ3vhH_HFEPut-ya1NoDWjwg.PNG.banner4/1-2.png",
//                    "네이버 임시 인식표입니다. 구매하세요.", false, true));
//            itemList.add(getProductItem(3, type, 123456, "VOWOW 블루투스 등록 인식표", "고무로 만듬",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjUw/MDAxNTY2MDE5MzU5Nzc0.c5STF3Yem_ZBBcrotD0nB0EHLaiANBWRnPSVqza-S-cg.zbO6Frp2FTZPlxwO6iWicZ3vhH_HFEPut-ya1NoDWjwg.PNG.banner4/1-3.png",
//                    "네이버 임시 인식표입니다. 구매하세요.", false, true));
//            itemList.add(getProductItem(4, type, 123456, "VOWOW 블루투스 등록 인식표", "고무로 만듬",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjUw/MDAxNTY2MDE5MzU5Nzc0.c5STF3Yem_ZBBcrotD0nB0EHLaiANBWRnPSVqza-S-cg.zbO6Frp2FTZPlxwO6iWicZ3vhH_HFEPut-ya1NoDWjwg.PNG.banner4/1-4.png",
//                    "네이버 임시 인식표입니다. 구매하세요.", false, true));
//            itemList.add(getProductItem(5, type, 123456, "VOWOW 블루투스 등록 인식표", "고무로 만듬",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjUw/MDAxNTY2MDE5MzU5Nzc0.c5STF3Yem_ZBBcrotD0nB0EHLaiANBWRnPSVqza-S-cg.zbO6Frp2FTZPlxwO6iWicZ3vhH_HFEPut-ya1NoDWjwg.PNG.banner4/1-5.png",
//                    "네이버 임시 인식표입니다. 구매하세요.", false, true));
//            itemList.add(getProductItem(6, type, 123456, "VOWOW 블루투스 등록 인식표", "고무로 만듬",
//                    "https://blogfiles.pstatic.net/MjAxOTA4MTdfMjUw/MDAxNTY2MDE5MzU5Nzc0.c5STF3Yem_ZBBcrotD0nB0EHLaiANBWRnPSVqza-S-cg.zbO6Frp2FTZPlxwO6iWicZ3vhH_HFEPut-ya1NoDWjwg.PNG.banner4/1-6.png",
//                    "네이버 임시 인식표입니다. 구매하세요.", false, true));
        }
    }


    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) {
                        Log.d(TAG, "널로 들어옴");
                        return null;
                    }
                    return delegate.convert(body);
                }
            };
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case VIEW_PRODUCTITEM:
                if(resultCode == RESULT_OK) {
                    // TODO : 해당 아이템의 키를 받아와 예약 시 같이 보내기
                    Log.d(TAG, "해당 아이템 구매 완료!");
                    setResult(RESULT_OK);
                    finish();
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
