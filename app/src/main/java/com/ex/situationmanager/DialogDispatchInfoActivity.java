package com.ex.situationmanager;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ex.situationmanager.util.DispatchAdapter;

public class DialogDispatchInfoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);                            // 주변 검정으로 변하지않게
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));      // 다이얼로그 테두리 제거

        setContentView(R.layout.dispatchinfo);

        RecyclerView recyclerView = findViewById(R.id.recycler_dispatch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DispatchAdapter adapter = new DispatchAdapter("1");
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(DialogDispatchInfoActivity.this, 1));

        ImageView imgOk = findViewById(R.id.btnOk);
        imgOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
