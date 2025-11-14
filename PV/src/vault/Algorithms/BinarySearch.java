package vault.Algorithms;

import java.util.List;

public class BinarySearch {

    // Assumes entries are sorted by platform
    public static int searchPlatform(List<String[]> entries, String target) {
        int left = 0, right = entries.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = entries.get(mid)[0].compareToIgnoreCase(target);
            if (cmp == 0) return mid;
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return -1; // not found
    }
}
