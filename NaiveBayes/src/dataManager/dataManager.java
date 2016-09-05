package dataManager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/6/19.
 * 数据处理获取数据
 */
public class dataManager {
//    ArrayList<List<String>> Test = new ArrayList<>();//所有测试用例
    Map<String,Set<String>>Test=new HashMap<>();
    Map<String,Integer> postive=new HashMap<>();
    Map<String,Integer>negtive=new HashMap<>();


    public dataManager(Map<String,Integer> pos, Map<String,Integer> neg,Map<String,Set<String>> T){
        this.postive=pos;
        this.negtive=neg;
        this.Test=T;
    }
    public static List<String> get_file_list(File file) {
        List<String> result = new ArrayList<String>();
        if (!file.isDirectory()) {
            System.out.println(file.getAbsolutePath());
            result.add(file.getAbsolutePath());
        } else {
            File[] directoryList = file.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isFile() && file.getName().indexOf("txt") > -1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            for (int i = 0; i < directoryList.length; i++) {
                result.add(directoryList[i].getPath());
            }
        }

        return result;
    }

    /**
     * 进行文件读取。
     * 注意把一字词和二字词去除
     */
    public void get_data() {
        Map<String,String> content=new HashMap<>() ;
        Set<String> set=new HashSet<>();
        File f1 = new File("D:\\data\\data\\train\\ham");
        File f2 = new File("D:\\data\\data\\train\\spam");
        File f3 = new File("D:\\data\\data\\test");
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        List<String> l3 = new ArrayList<>();
        l1 = get_file_list(f1);
        l2 = get_file_list(f2);
        l3 = get_file_list(f3);
        for (String l : l1) {
 //           System.out.println(l);
            File file = new File(l);
            try {
                FileInputStream in = new FileInputStream(l);
                byte[] readBytes = new byte[in.available()];
                in.read(readBytes);
                String line = new String(readBytes);
                String[] array = line.split("[\r\n|, |@|#|$|{|}|%|^|\\. |; |: |' |! |? |+ |* |/|\r]");//获取txt中的数据

                for (int i = 0; i < array.length; i++) {
                    if (postive.containsKey(array[i])) {//出现过这个词了，这个词计数+1
                        int v = postive.get(array[i]);
                        v++;
                        postive.remove(array[i]);
                        postive.put(array[i], v);
                    } else {//没有出现过，词计数减1
                        if(array[i].length()>2)
                            postive.put(array[i], 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String l : l2) {
//            System.out.println(l);
            File file = new File(l);
            try {
                FileInputStream in = new FileInputStream(l);
                byte[] readBytes = new byte[in.available()];
                in.read(readBytes);
                String line = new String(readBytes);
                String[] array = line.split("[\r\n" +
                        "|, |@|#|$|%|^{|}|&|\\. |; |: |' |! |? |+ |* |/|\n" +
                        "|\r]");
                for (int i = 0; i < array.length; i++) {
                    if (negtive.containsKey(array[i])) {//已经存在这个词，词计数+1
                        int v = negtive.get(array[i]);
                        v++;
                        negtive.remove(array[i]);
                        negtive.put(array[i], v);
                    } else {//这个词没出现过，加入这个词，计数设为1
                        if(array[i].length()>2)
                            negtive.put(array[i], 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String l : l3) {
            File file = new File(l);
            set=new HashSet<>();
            try {
                FileInputStream in = new FileInputStream(l);
                byte[] readBytes = new byte[in.available()];
                in.read(readBytes);
                String line = new String(readBytes);
                String[] array = line.split("[\r\n" +
                        "|, |\\. |@|#|$|%|^|&|{|}(|)|; |: |' |! |? |+ |* |/|\n" +
                        "|\r]");
                for(int i=0;i<array.length;i++) {//将出现单词加入set
                    if (!set.contains(array[i])&&array[i].length()>2)
                        set.add(array[i]);
                }
                Test.put(l,set);//用<filename,set<word>>的KV存入Test中
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
