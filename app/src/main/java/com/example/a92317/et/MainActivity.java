package com.example.a92317.et;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;

import jackmego.com.jieba_android.JiebaSegmenter;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static DatabaseOperation databaseOperation;

    private List<Emo> emoList;
    private List<Emo> rEmoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter adapter;
    private BoomMenuButton bmb;

    public enum Icon {
        positive(R.drawable.posi), negative(R.drawable.nega);
        private int iconRes;
        Icon(int iconRes) {
            this.iconRes = iconRes;
        }
        int getIconRes() {
            return iconRes;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JiebaSegmenter.init(this);

        databaseOperation = new DatabaseOperation(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        HamButton.Builder builder0 = new HamButton.Builder()
                .normalText("新增")
                .normalImageRes(R.drawable.add)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        final EditText et = new EditText(MainActivity.this);
                        new AlertDialog.Builder(MainActivity.this)
                                .setView(et)
                                .setTitle(" ")
                                .setIcon(R.drawable.add)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String sentence = et.getText().toString();
                                        Emo emo = new Emo();
                                        emo.setTime();
                                        emo.setSentence(sentence);

                                        Analysis analysis = new Analysis(JiebaSegmenter.getJiebaSegmenterSingleton(), sentence);
                                        int score = analysis.emoScore();
                                        if(score > 0)
                                            emo.setLabel(getString(R.string.label_positive));
                                        else
                                            emo.setLabel(getString(R.string.label_negative));
                                        databaseOperation.save(emo);
                                        adapter.add(0, emo);
                                        recyclerView.scrollToPosition(0);
                                    }
                                })
                                .setNegativeButton("取消",null)
                                .show();
                    }
                });
        bmb.addBuilder(builder0);

        HamButton.Builder builder1 = new HamButton.Builder()
                .normalText("查找")
                .normalImageRes(R.drawable.search)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        final View searchView = getLayoutInflater().inflate(R.layout.search, null);
                        ((RadioGroup)searchView.findViewById(R.id.time_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                if(R.id.time_pick == i) {
                                    searchView.findViewById(R.id.date_picker).setVisibility(View.VISIBLE);
                                }
                                if(R.id.time_no_pick == i) {
                                    searchView.findViewById(R.id.date_picker).setVisibility(View.GONE);
                                }
                            }
                        });
                        new AlertDialog.Builder(MainActivity.this)
                                .setView(searchView)
                                .setTitle(" ")
                                .setIcon(R.drawable.search)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String sentence = "", label = "", time = "";
                                        switch (((RadioGroup)searchView.findViewById(R.id.label_group)).getCheckedRadioButtonId()) {
                                            case R.id.label_posi:
                                                label = getString(R.string.label_positive);
                                                break;
                                            case R.id.label_nega:
                                                label = getString(R.string.label_negative);
                                                break;
                                            case R.id.label_all:
                                                break;
                                            default:
                                                break;
                                        }
                                        //Toast.makeText(MainActivity.this, label,Toast.LENGTH_LONG).show();
                                        sentence = ((TextView)searchView.findViewById(R.id.getSentence)).getText().toString();
                                        //Toast.makeText(MainActivity.this, sentence,Toast.LENGTH_LONG).show();
                                        if(((RadioGroup)searchView.findViewById(R.id.time_group)).getCheckedRadioButtonId() == R.id.time_pick) {
                                            DatePicker datePicker = (DatePicker) searchView.findViewById(R.id.date_picker);
                                            time += datePicker.getYear() + ".";
                                            time += (datePicker.getMonth() < 9 ? "0" : "") + (datePicker.getMonth()+1) + ".";
                                            time += (datePicker.getDayOfMonth() < 10 ? "0" : "") + datePicker.getDayOfMonth();
                                            //Toast.makeText(MainActivity.this, time,Toast.LENGTH_LONG).show();
                                        }
                                        Intent intent_search = new Intent(MainActivity.this, SearchResult.class);
                                        intent_search.putExtra("time", time);
                                        intent_search.putExtra("sentence", sentence);
                                        intent_search.putExtra("label", label);
                                        startActivity(intent_search);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                });
        bmb.addBuilder(builder1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Button button_back = (Button) findViewById(R.id.title_back);
        button_back.setOnClickListener(this);

        Button button_question = (Button) findViewById(R.id.title_question);
        button_question.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        rEmoList.clear();
        emoList = databaseOperation.findAll();
        for(int i = emoList.size() - 1; i >= 0; i--) {
            rEmoList.add(emoList.get(i));
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(rEmoList);
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                final Emo emo = rEmoList.get(position);
                View clickView = getLayoutInflater().inflate(R.layout.item_click, null);
                ((TextView)clickView.findViewById(R.id.sentence_click)).setText(emo.getSentence());
                ((TextView)clickView.findViewById(R.id.time_click)).setText(emo.getTime());
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(" ")
                        .setIcon(Icon.valueOf(emo.getLabel()).getIconRes())
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
                                databaseOperation.delete(emo.getId());
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
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("确定退出吗？")
                        .setCancelable(false)
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCollector.finishAll();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                break;
            case R.id.title_question:
                break;
            case R.id.fab:
                bmb.boom();
                break;
            default:
                break;
        }
    }
}
