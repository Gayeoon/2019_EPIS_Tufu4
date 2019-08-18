package com.gaze.rkdus.a2019_epis_tufu4.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.item.ReviewListItem;
import com.gaze.rkdus.a2019_epis_tufu4.item.SearchResultData;

import java.util.ArrayList;

import static com.gaze.rkdus.a2019_epis_tufu4.user.UserLoginActivity.TAG;

/*
SearchActivity RecyclerView의 Adapter
 */
public class HospitalReviewListAdapter extends RecyclerView.Adapter<HospitalReviewListAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<ReviewListItem> listData;
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

    public HospitalReviewListAdapter(ArrayList<ReviewListItem> arrayList) {
        listData = arrayList;
    }

    public void resetAll(ArrayList<ReviewListItem> newArrayList) { ;

        this.listData = null;
        this.listData = newArrayList;
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        private TextView reviewContent;
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private int score;


        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            reviewContent = itemView.findViewById(R.id.tvReviewContent);
            ivStar1 = itemView.findViewById(R.id.reviewStar1);
            ivStar2 = itemView.findViewById(R.id.reviewStar2);
            ivStar3 = itemView.findViewById(R.id.reviewStar3);
            ivStar4 = itemView.findViewById(R.id.reviewStar4);
            ivStar5 = itemView.findViewById(R.id.reviewStar5);

        }

        void onBind(ReviewListItem data) {
            Log.d(TAG, "data 들어옴");
            Log.d(TAG, "data 들어옴. score : " + data.getScore() + ", " + data.getContent());
            score = data.getScore();
            reviewContent.setText(data.getContent());

            switch (score) {
                case 1:
                    ivStar1.setImageResource(R.drawable.review_coloredstar);
                    break;
                case 2:
                    ivStar1.setImageResource(R.drawable.review_coloredstar);
                    ivStar2.setImageResource(R.drawable.review_coloredstar);
                    break;
                case 3:
                    ivStar1.setImageResource(R.drawable.review_coloredstar);
                    ivStar2.setImageResource(R.drawable.review_coloredstar);
                    ivStar3.setImageResource(R.drawable.review_coloredstar);
                    break;
                case 4:
                    ivStar1.setImageResource(R.drawable.review_coloredstar);
                    ivStar2.setImageResource(R.drawable.review_coloredstar);
                    ivStar3.setImageResource(R.drawable.review_coloredstar);
                    ivStar4.setImageResource(R.drawable.review_coloredstar);
                    break;
                case 5:
                    ivStar1.setImageResource(R.drawable.review_coloredstar);
                    ivStar2.setImageResource(R.drawable.review_coloredstar);
                    ivStar3.setImageResource(R.drawable.review_coloredstar);
                    ivStar4.setImageResource(R.drawable.review_coloredstar);
                    ivStar5.setImageResource(R.drawable.review_coloredstar);
                    break;
                    default:
                        break;
            }
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate.
        // return 인자는 ViewHolder.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hosinfo_review_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.

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
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(ReviewListItem reviewListItem) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(reviewListItem);
    }
}