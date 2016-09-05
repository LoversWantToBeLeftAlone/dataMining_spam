package Bayes;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import dataManager.dataManager;
import jdk.internal.org.objectweb.asm.tree.InnerClassNode;


/**
 * Created by Administrator on 2016/6/19.
 */
public class NaiveBayes {
    Map<String,Integer>postive=new HashMap<>();
    Map<String,Integer>negtive=new HashMap<>();
    Map<String,Set<String>> Test = new HashMap<>();//所有测试用例
    Map<String,Map<String,String>>TEST=new HashMap<>();
    Map<String, Double> posMap = new HashMap<>();//存所有单词在正例中的概率
    Map<String, Double> negMap = new HashMap<>();//存所有单词在反例中的概率
    Map<Integer,String>result=new HashMap<>();
    /**
     * 获取文件夹下的所有文件目录
     * @param
     * @return
     */
    public void get_data() {
        dataManager d = new dataManager(this.postive, this.negtive, this.Test);
        d.get_data();
    }
    /**
     * 获取正例所有单词出现总数
     *
     * @return
     */
    int get_pos_num() {
        int a=0;
        for(String key:postive.keySet())
            a=a+postive.get(key);
//        System.out.println(a);
        return a;
    }

    /**
     * 获取反例出现概率
     *
     * @return
     */
    int get_neg_num() {
        int b=0;
        for(String key:negtive.keySet())
            b=b+negtive.get(key);
 //       System.out.println(b);
        return b;
    }

    /**
     * 获取一个单词在正例中出现的次数，用计算概率
     *
     * @param word
     * @return
     */
    int get_num_of_appear_in_pos(String word) {
        int num = 0;
        if(postive.containsKey(word))
            num=postive.get(word);
        return num;
    }

    /**
     * 获取一个单词在反例中的出现次数用来计算概率
     *
     * @param word
     * @return
     */
    int get_num_of_appear_in_neg(String word) {
        int num = 0;
        if(negtive.containsKey(word))
            num=negtive.get(word);
        return num;
    }

    /**
     * 训练模型，也就是获取所有我们需要的概率
     */
    void train() {
        //获取所有单词在正例的概率
        System.out.println("TRAIN BEGIN:");
        int m = 0;
        int n = 0;
        m = get_pos_num();//m为ham中总的单词出现次数
        n = get_neg_num();//n为spam中总的单词出现次数
        //获取所有单词在正例中的出现次数
        Set<String> posSet = postive.keySet();
        for (String s : posSet) {
            if (!posMap.containsKey(s)) {//概率表中没存在这个单词，则计算
                double p;
                p = (double) get_num_of_appear_in_pos(s) / (double) m;
                posMap.put(s, p);
            }
        }
        //获取所有单词在反例中出现的概率
        Set<String> negSet = negtive.keySet();
        for (String s : negSet) {
            if (!negMap.containsKey(s)) {
                double p;
                p = (double) get_num_of_appear_in_neg(s) / (double) n;
                //               System.out.println(s + " " + p);
                negMap.put(s, p);
            }
        }
        System.out.println("TRAIN OVER！");
    }
    /**
     * 测试。
     * 讲道理应该把文件输出
     */
    void test() throws Exception{
        System.out.println("TEST BEGIN:");
        int c=get_pos_num();
        int d=get_neg_num();
        List<Double> P1 = new ArrayList<>();//所有的概率P(x1|ham),P(x2|ham)......
        List<Double> P2 = new ArrayList<>();//所有的概率P(x1|spam),P(x2|spam)......
        for (String filename :Test.keySet()) {
            P1 = new ArrayList<>();
            P2 = new ArrayList<>();
            //先计算需要的值，避免不停的计算，消耗代价昂贵
            for (String s:Test.get(filename)) {//s是单词
                int a = get_num_of_appear_in_pos(s);
                int b = get_num_of_appear_in_neg(s);
                if (a == 0) {//出现次数为0使用拉普拉斯修正
//                    if(P1.contains())
                    double p = (double) 1 / (double) (c + 2);
                    P1.add(p);
                }
                if (b == 0) {//同上
                    double p = (double) 1 / (double) (d + 2);
                    P2.add(p);
                }

                if (a>=1&&a<25000) {//ham中，过滤出现次数大于25000的词语
                    P1.add(posMap.get(s));
                }

                if (b>=1&&b<4500) {//spam中过滤掉出现次数大于4500的词语
                    P2.add(negMap.get(s));
                }
            }//end for 2

            /**
            计算P(X|C)P(C)
             使用取对数的方式
             */

            double pos = 0.0, neg = 0.0;
            for (double d1 : P1)
                pos = pos + Math.log(d1);
            pos=pos+Math.log(0.2);

            for (double d1 : P2)
                neg = neg + Math.log(d1);
            neg=neg+Math.log(0.8);

            if (pos >= neg) {
                String[]array=filename.split("\\\\");
                String[]a=array[array.length-1].split("\\.");
                int key=Integer.parseInt(a[0]);
                result.put(key, "-1");
            } else {
                String[]array=filename.split("\\\\");
                String[]a=array[array.length-1].split("\\.");
                int key=Integer.parseInt(a[0]);
                result.put(key, "+1");
            }
        }//end for1
        System.out.println("TEST OVER!");
        try {
            System.out.println("OUTPUT:");
            File out=new File("D://data//131220044.txt");
            FileOutputStream fs = new FileOutputStream(out);
            PrintStream p = new PrintStream(fs);
            System.out.println(result.size());
            for(int key:result.keySet()) {
                String s = Integer.toString(key);
                String filename = s + ".txt";
                System.out.println(filename);
                p.println(filename + "\t" + result.get(key));
            }
            p.close();
            System.out.println(get_pos_num()+":"+get_neg_num());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static  void main(String[]args) throws Exception{
        NaiveBayes bayes=new NaiveBayes();
        bayes.get_data();
        bayes.train();
        bayes.test();
    }
}
