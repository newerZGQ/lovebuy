package com.example.zgq.lovebuy.util;

import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 37902 on 2016/2/27.
 */
public class SortUtil {
    public static int CONSUMTASK = 1;
    public static int DESIRETASK = 2;
    public static void sortDesire(List<MyConsumDesire> list){
        Comparator<MyConsumDesire> comparator = new Comparator<MyConsumDesire>() {
            @Override
            public int compare(MyConsumDesire lhs, MyConsumDesire rhs) {
                if (lhs.getDate().compareTo(rhs.getDate())>0) return 1;
                if (lhs.getDate().compareTo(rhs.getDate())<0) return -1;
                else {return 0;}
            }
        };
        Collections.sort(list,comparator);
    }
    public static void sortConsum(List<Consum> list){
        Comparator<Consum> comparator = new Comparator<Consum>() {
            @Override
            public int compare(Consum lhs, Consum rhs) {
                if (lhs.getDate().compareTo(rhs.getDate())>0) return 1;
                if (lhs.getDate().compareTo(rhs.getDate())<0) return -1;
                else {return 0;}
            }
        };
        Collections.sort(list,comparator);
    }

    public static ArrayList<MyConsumDesire> mergeDesire(ArrayList<MyConsumDesire> part1,ArrayList<MyConsumDesire> part2){
        int N = part1.size()+part2.size();
        ArrayList<MyConsumDesire> all = new ArrayList<MyConsumDesire>(N);
        int part1Size = part1.size(),part2Size = part2.size();
        int i = 0,j=0;
        for (int k = 0;k<N;k++){
            if (i>=part1Size && j<part2Size ) {
                all.add(part2.get(j++));
                continue;
            }
            if (j>=part2Size && i< part1Size) {
                all.add(part1.get(i++));
                continue;
            }
            if (j<part2Size && i< part1Size && ((part1.get(i).getDate()).compareTo(part2.get(j).getDate()))<0) {
                all.add(part1.get(i++));
                continue;
            }
            if (j<part2Size && i< part1Size && ((part1.get(i).getDate()).compareTo(part2.get(j).getDate()))>=0) {
                all.add(part2.get(j++));
                continue;
            }
        }
        return all;
    }
}
