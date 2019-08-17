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
    }

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    // 결제하기 버튼 클릭 이벤트
    public void payProductClick(View view) {
        Toast.makeText(this, "결제버튼 클릭", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "결제버튼 클릭");
    }

    // 취소하기 버튼 클릭 이벤트
    public void cancelClick(View view) {
        Log.d(TAG, "취소 클릭");
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
