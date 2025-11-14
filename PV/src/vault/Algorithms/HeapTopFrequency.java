package vault.Algorithms;

import java.util.*;

public class HeapTopFrequency {

    public static List<String> topPlatforms(List<String[]> entries, int n) {
        Map<String, Integer> freq = new HashMap<>();
        for (String[] entry : entries) {
            freq.put(entry[0], freq.getOrDefault(entry[0], 0) + 1);
        }

        PriorityQueue<Map.Entry<String,Integer>> heap = new PriorityQueue<>(
                (a,b) -> a.getValue() - b.getValue()
        );

        for (Map.Entry<String,Integer> e : freq.entrySet()) {
            heap.offer(e);
            if (heap.size() > n) heap.poll();
        }

        List<String> top = new ArrayList<>();
        while(!heap.isEmpty()) top.add(heap.poll().getKey());
        Collections.reverse(top);
        return top;
    }
}
