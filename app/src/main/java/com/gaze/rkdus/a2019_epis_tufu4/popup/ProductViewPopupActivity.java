package com.gaze.rkdus.a2019_epis_tufu4.popup;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.MainActivity;
import com.gaze.rkdus.a2019_epis_tufu4.databinding.ActivityProductViewPopupBinding;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.item.ProductItemData;

public class ProductViewPopupActivity extends BaseActivity {
    ActivityProductViewPopupBinding binding;
    ProductItemData productItemData;
    final int CHECK_PAY = 10;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_view_popup);
        binding.setActivity(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("productData")) {
                productItemData = (ProductItemData) intent.getSerializableExtra("productData");
            }
            else
                finishPopup();
        }
        else
            finishPopup();

        binding.setProductItem(productItemData);
        binding.setStrPrice(String.valueOf(productItemData.getPrice()) + "원");

        if (productItemData.getShipping() == 0)
            binding.setStrShipping("무료");
        else
            binding.setStrShipping(String.valueOf(productItemData.getShipping()) + "원");

        binding.setCount(count);
        setSumPrice();
    }

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    // - 버튼 클릭
    public void minusClick(View view) {
        if (count <= 1) {
            Toast.makeText(getApplicationContext(), "1 이하로는 감소시킬 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            count--;
            binding.setCount(count);
            setSumPrice();

        }
    }

    // + 버튼 클릭
    public void plusClick(View view) {
        if (count > 99) {
            Toast.makeText(getApplicationContext(), "99개 이상으로는 구입하실 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            count++;
            binding.setCount(count);
            setSumPrice();
        }
    }

    public void setSumPrice() {
        String sumPrice = String.valueOf(count * productItemData.getPrice()) + "원";
        binding.setSumPrice(sumPrice);
    }

    // 결제하기 버튼 클릭 이벤트
    public void payProductClick(View view) {
        Intent payIntent = new Intent(this, MessagePopupActivity.class);
        payIntent.putExtra("messageType", "payment");
        startActivityForResult(payIntent, CHECK_PAY);
        Log.d(TAG, "결제버튼 클릭");
    }

    // 취소하기 버튼 클릭 이벤트
    public void cancelClick(View view) {
        Log.d(TAG, "취소 클릭");
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CHECK_PAY:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "결제 완료.");
                    Toast.makeText(getApplicationContext(), "결제가 완료되었습니다.", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    Log.d(TAG, "결제 실패.");
                }
                break;
            default:
                break;
        }
    }
}
