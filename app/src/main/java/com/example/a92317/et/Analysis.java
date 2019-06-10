package com.example.a92317.et;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jackmego.com.jieba_android.JiebaSegmenter;

public class Analysis {

    private List<String> wordList;

    public Analysis(JiebaSegmenter jiebaSegmenter, String sentence) {
        wordList = jiebaSegmenter.getDividedString(sentence);
    }

    public int emoScore() {
        int score = 0;

        String prefix = "E:\\AndroidProject\\ET\\app\\src\\main\\res\\dict\\";
        String posPath = prefix + "pos.txt";
        String negPath = prefix + "neg.txt";
        String plusPath = prefix + "plus.txt";
        String noPath = prefix + "no.txt";

        List<String> posDict = txt(posPath);
        List<String> negDict = txt(negPath);
        List<String> plusDict = txt(plusPath);
        List<String> noDict = txt(noPath);

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
