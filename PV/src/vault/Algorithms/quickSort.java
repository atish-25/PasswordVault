package vault.Algorithms;

import java.util.List;

public class quickSort {

    public static void sortByPlatform(List<String[]> entries) {
        quickSort(entries, 0, entries.size() - 1);
    }

    private static void quickSort(List<String[]> arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(List<String[]> arr, int low, int high) {
        String pivot = arr.get(high)[0]; // platform name
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr.get(j)[0].compareToIgnoreCase(pivot) <= 0) {
                i++;
                String[] temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }
        String[] temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);
        return i + 1;
    }
}
