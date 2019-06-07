package com.example.a92317.et;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends BaseActivity implements View.OnClickListener {

    private List<Emo> emoResultList;
    private List<Emo> rEmoResultList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        recyclerView = (RecyclerView) findViewById(R.id.rv_search);
        init();

        Button button_back = (Button) findViewById(R.id.title_back);
        button_back.setOnClickListener(this);

        Button button_question = (Button) findViewById(R.id.title_question);
        button_question.setOnClickListener(this);
    }

    private void init() {
        Intent intent = getIntent();
        String label = intent.getStringExtra("label");
        String sentence = intent.getStringExtra("sentence");
        String time = intent.getStringExtra("time");
        emoResultList = MainActivity.databaseOperation.findAccurately(label, sentence, time);
        for(int i = emoResultList.size() - 1; i >= 0; i--) {
            rEmoResultList.add(emoResultList.get(i));
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(rEmoResultList);
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                final Emo emo = rEmoResultList.get(position);
                View clickView = getLayoutInflater().inflate(R.layout.item_click, null);
                ((TextView)clickView.findViewById(R.id.sentence_click)).setText(emo.getSentence());
                ((TextView)clickView.findViewById(R.id.time_click)).setText(emo.getTime());
                new AlertDialog.Builder(SearchResult.this)
                        .setTitle(" ")
                        .setIcon(MainActivity.Icon.valueOf(emo.getLabel()).getIconRes())
                        .setView(clickView)
                        .setCancelable(false)
                        .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.databaseOperation.delete(emo.getId());
                                adapter.remove(position);
                            }
                        })
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.title_back:
                new AlertDialog.Builder(SearchResult.this)
                        .setTitle("提示")
                        .setMessage("确定离开搜索结果吗？")
                        .setCancelable(false)
                        .setPositiveButton("离开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.title_question:
                break;
            default:
                break;
        }
    }
}
