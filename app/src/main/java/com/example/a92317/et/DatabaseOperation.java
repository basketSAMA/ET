package com.example.a92317.et;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOperation {
    private MyDatabaseHelper myDatabaseHelper;

    public DatabaseOperation(Context context) {
        myDatabaseHelper = new MyDatabaseHelper(context, "Emo.db", null, 1);
    }

    public void save(Emo emo) {
        myDatabaseHelper.getWritableDatabase().execSQL("insert into Emo (sentence, time, label) values (?, ?, ?)"
                , new Object[] { emo.getSentence(), emo.getTime(), emo.getLabel() });
        myDatabaseHelper.close();
    }

    public void delete(Integer id) {
        myDatabaseHelper.getWritableDatabase().execSQL("delete from Emo where id = ?"
                , new Object[]{ id.toString() });
        myDatabaseHelper.close();
    }

    public void change(Emo emo) {
        myDatabaseHelper.getWritableDatabase().execSQL("update Emo set label = ?, sentence = ?, time = ? where id = ?"
                , new Object[]{ emo.getLabel(), emo.getSentence(), emo.getTime(), emo.getId() });
        myDatabaseHelper.close();
    }

    public Emo findById(Integer id) {
        Emo emo = null;
        Cursor cursor = myDatabaseHelper.getWritableDatabase().rawQuery("select * from Emo where id = ?"
                , new String[]{ id.toString() });
        if(cursor.moveToFirst()) {
            emo = new Emo();
            emo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            emo.setSentence(cursor.getString(cursor.getColumnIndex("sentence")));
            emo.setTime(cursor.getString(cursor.getColumnIndex("time")));
            emo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
        }
        cursor.close();
        myDatabaseHelper.close();
        return emo;
    }

    public List<Emo> findAccurately(String label, String sentence, String time) {
        List<Emo> list = new ArrayList<>();
        Emo emo = null;
        String qLabel = label +"%";
        String qSentence = "%" + sentence +"%";
        String qTime = time +"%";
        Cursor cursor = myDatabaseHelper.getWritableDatabase().rawQuery("select * from Emo where label like ? and sentence like ? and time like ?"
                , new String[]{ qLabel, qSentence, qTime });
        while(cursor.moveToNext()) {
            emo = new Emo();
            emo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            emo.setSentence(cursor.getString(cursor.getColumnIndex("sentence")));
            emo.setTime(cursor.getString(cursor.getColumnIndex("time")));
            emo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            list.add(emo);
        }
        cursor.close();
        myDatabaseHelper.close();
        return list;
    }

    public List<Emo> findAll() {
        List<Emo> list = new ArrayList<>();
        Emo emo = null;
        Cursor cursor = myDatabaseHelper.getWritableDatabase().rawQuery("select * from Emo"
                , null);
        while(cursor.moveToNext()) {
            emo = new Emo();
            emo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            emo.setSentence(cursor.getString(cursor.getColumnIndex("sentence")));
            emo.setTime(cursor.getString(cursor.getColumnIndex("time")));
            emo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            list.add(emo);
        }
        cursor.close();
        myDatabaseHelper.close();
        return list;
    }
}
