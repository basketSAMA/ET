package com.example.a92317.et;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jackmego.com.jieba_android.JiebaSegmenter;

public class Analysis {

    private List<String> wordList;
    private List<String> posDict;
    private List<String> negDict;
    private List<String> plusDict;
    private List<String> noDict;

    public Analysis() {
        String prefix = Environment.getExternalStorageDirectory().getPath() + "/ETdict/";
        String posPath = prefix + "pos.txt";
        String negPath = prefix + "neg.txt";
        String plusPath = prefix + "plus.txt";
        String noPath = prefix + "no.txt";

        posDict = txt(posPath);
        negDict = txt(negPath);
        plusDict = txt(plusPath);
        noDict = txt(noPath);
    }

    public void setWordList(JiebaSegmenter jiebaSegmenter, String sentence) {
        wordList = jiebaSegmenter.getDividedString(sentence);
    }

    public int getEmoScore() {
        int score = 0;

        for(int i=0;i<wordList.size();i++) {
            String word = wordList.get(i);
            if(negDict.contains(word)) {
                if(i>0 && noDict.contains(wordList.get(i-1)))
                    score += 2;
                else if(i>0 && plusDict.contains(wordList.get(i-1)))
                    score -= 4;
                else
                    score -= 2;
            }
            else if(posDict.contains(word)) {
                if(i>0 && (noDict.contains(wordList.get(i-1)) || negDict.contains(wordList.get(i-1))))
                    score -= 2;
                else if(i>0 && plusDict.contains(wordList.get(i-1)))
                    score += 4;
                else if(i<wordList.size()-1 && negDict.contains(wordList.get(i+1)))
                    score -= 2;
                else
                    score += 2;
            }
            else if(noDict.contains(word))
                score -= 1;
        }

        return score;
    }

    private List<String> txt(String path) {
        List<String> res = new ArrayList<>();
        try {
            File file = new File(path);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        reds = reds.trim();
                        res.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
