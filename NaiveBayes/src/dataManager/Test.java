
package dataManager;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2016/6/21.
 */

public class Test {

    public static void main(String[] args) throws IOException {
        Map<Integer, String> result = new TreeMap<Integer, String>();
        File file = new File("D://data//131220044.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        ArrayList<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();
        System.out.println(list.size());
        for(int i=0;i<list.size();i++){
            String s=list.get(i).split("\\.")[0];
            int k=Integer.parseInt(s);
            System.out.println(k);
            if(k!=i+1)
                System.out.println(k);
        }
    }

}