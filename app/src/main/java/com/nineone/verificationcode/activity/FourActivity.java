package com.nineone.verificationcode.activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.nineone.verificationcode.R;

import java.util.ArrayList;
import java.util.List;

public class FourActivity extends Activity {
    private RecyclerView recycler;
    private Context context;
    private int height;
    private List<SimpleBean> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        context = this;
        height = context.getResources().getDisplayMetrics().heightPixels;
        init();
    }

    private void init() {
        recycler = findViewById(R.id.recycler);
    }


    private class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView textView = new ImageView(context);
            return new SimpleViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
//            holder.imageView.setImageResource(ids.get(position));
            Glide.with(context).load(list.get(position).resId).transform(new CenterCrop(), new RoundedCorners(25)).into(holder.imageView);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height / 5) + 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private class SimpleBean {
        @RawRes
        @DrawableRes
        int resId;

        String name;

        public SimpleBean(int resId) {
            this.resId = resId;
        }
    }
}