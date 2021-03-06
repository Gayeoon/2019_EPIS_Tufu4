package com.gaze.rkdus.a2019_epis_tufu4.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.item.ProductItemData;
import com.gaze.rkdus.a2019_epis_tufu4.item.SearchResultData;

import java.util.ArrayList;

/*
ProductPopupActivity RecyclerView의 Adapter
 */
public class ProductPopupListAdapter extends RecyclerView.Adapter<ProductPopupListAdapter.ItemViewHolder> {

    // adapter에 들어갈 list
    private ArrayList<ProductItemData> listData;
    private Context context;

    //아이템 클릭시 실행 함수
    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view, int position);

    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ProductPopupListAdapter(ArrayList<ProductItemData> arrayList) {
        listData = arrayList;
    }

    public void resetAll(ArrayList<ProductItemData> newArrayList) { ;

        this.listData = null;
        this.listData = newArrayList;
    }

    // TODO: 클릭이벤트, 이미지 , ArrayList에 특정값들 넣기
    // 여기서 view setting.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        private ImageView ivProduct;
        private TextView tvProductName, tvProductPrice;
        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivProduct = itemView.findViewById(R.id.productImage);
            tvProductName = itemView.findViewById(R.id.productName);
            tvProductPrice = itemView.findViewById(R.id.productPrice);
        }

        void onBind(ProductItemData data) {
            loadImage(ivProduct, data.getImg_list());

            tvProductName.setText(data.getName());
            tvProductPrice.setText(String.valueOf(data.getPrice()) + "원");
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate.
        // return 인자는 ViewHolder.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_popup_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수.

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null) {
                    itemClick.onClick(v, position);
                }
            }
        });

        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수.
        return listData.size();
    }

    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}