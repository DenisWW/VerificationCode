package com.nineone.verificationcode.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nineone.verificationcode.R;

public class DCStockDetailActivity extends Activity implements View.OnClickListener {

    public static final String STOCK_CODE = "STOCK_CODE";
    public static final String STOCK_NAME = "STOCK_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcstock_detail);
        TextView stock_title_tv = findViewById(R.id.stock_title_tv);

        String title = getIntent().getStringExtra(STOCK_NAME);
        String code = getIntent().getStringExtra(STOCK_CODE);
        stock_title_tv.setText(title);

        TextView go_bk_tv = findViewById(R.id.go_bk_tv);
        TextView go_stock_tv = findViewById(R.id.go_stock_tv);
        TextView go_stock_df_tv = findViewById(R.id.go_stock_df_tv);
        go_stock_df_tv.setOnClickListener(this);
        go_stock_tv.setOnClickListener(this);
        go_bk_tv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.go_stock_tv) {
            Intent intent = new Intent(v.getContext(), DCStockDetailActivity.class);
            intent.putExtra(DCStockDetailActivity.STOCK_NAME, "比亚迪");
            intent.putExtra(DCStockDetailActivity.STOCK_CODE, "byd");
            startActivity(intent);
        } else if (id == R.id.go_bk_tv) {
            Intent intent = new Intent(v.getContext(), DCBKActivity.class);
            intent.putExtra(DCBKActivity.BK_NAME, "汽车");
            intent.putExtra(DCBKActivity.BK_CODE, "qc");
            startActivity(intent);
        } else if (id == R.id.go_stock_df_tv) {
            Intent intent = new Intent(v.getContext(), DCStockDetailActivity.class);
            intent.putExtra(DCStockDetailActivity.STOCK_NAME, "东风汽车");
            intent.putExtra(DCStockDetailActivity.STOCK_CODE, "df");
            startActivity(intent);
        }
    }
}