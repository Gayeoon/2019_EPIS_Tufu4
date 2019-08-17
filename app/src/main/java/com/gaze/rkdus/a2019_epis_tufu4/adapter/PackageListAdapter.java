package com.gaze.rkdus.a2019_epis_tufu4.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.item.PackageListItem;
import com.gaze.rkdus.a2019_epis_tufu4.item.ProductItemData;

import java.util.ArrayList;

/*
ProductPopupActivity RecyclerView의 Adapter
 */
public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.ItemViewHolder> {

    // adapter에 들어갈 list
    private ArrayList<PackageListItem> listData;
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

    public PackageListAdapter(ArrayList<PackageListItem> arrayList) {
        listData = arrayList;
    }

    public void resetAll(ArrayList<PackageListItem> newArrayList) {

        this.listData = null;
        this.listData = newArrayList;
    }

    // TODO: 클릭이벤트, 이미지 , ArrayList에 특정값들 넣기
    // 여기서 view setting.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        private ImageView ivPackageImage;
        private TextView tvPackagename, tvSale, tvPrice, tvRealPrice;

        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivPackageImage = (ImageView) itemView.findViewById(R.id.packageImage);
            tvPackagename = (TextView) itemView.findViewById(R.id.tvPackagename);
            tvSale = (TextView) itemView.findViewById(R.id.tvSale);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvRealPrice = (TextView) itemView.findViewById(R.id.tvRealPrice);
        }

        void onBind(PackageListItem data) {
            tvPackagename.setText(data.getName());
            tvSale.setText(String.valueOf(data.getSale()) + "%");
            tvPrice.setText(String.valueOf(data.getPrice()) + "원");
            tvRealPrice.setText(String.valueOf(data.getReal_price()) + "원");

            ivPackageImage.setImageResource(data.getImg_id());
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate.
        // return 인자는 ViewHolder.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_list_item, parent, false);
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
}