import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<int[]> list1 = Arrays.asList(
                new int[] { 1, 3 },
                new int[] { 5, 7 },
                new int[] { 9, 12 },
                new int[] { 14, 16 });
        List<int[]> list2 = Arrays.asList(
                new int[] { 0, 2 },
                new int[] { 3, 6 },
                new int[] { 8, 10 },
                new int[] { 11, 15 });

        List<int[]> mergedList = mergeIntervals(list1, list2);

        for (int[] interval : mergedList) {
            System.out.println("[" + interval[0] + ", " + interval[1] + "]");
        }
    }

    public static List<int[]> mergeIntervals(List<int[]> list1, List<int[]> list2) {
        int n1 = list1.size();
        int n2 = list2.size();
        List<int[]> res = new ArrayList<>();

        int i1 = 0;
        int i2 = 0;

        int[] prev;
        if (list1.get(0)[0] <= list2.get(0)[0]) {
            prev = list1.get(0);
            i1++;
        } else {
            prev = list2.get(0);
            i2++;
        }

        while (i1 < n1 && i2 < n2) {
            System.out.println(i1 + " - " + i2);
            int[] interval1 = list1.get(i1);
            int[] interval2 = list2.get(i2);

            if (interval1[0] <= prev[1]) {
                prev[0] = Math.min(prev[0], interval1[0]);
                prev[1] = Math.max(prev[1], interval1[1]);
                i1++;
            } else if (interval2[0] <= prev[1]) {
                prev[0] = Math.min(prev[0], interval2[0]);
                prev[1] = Math.max(prev[1], interval2[1]);
                i2++;
            } else {
                res.add(prev);
                prev = interval1[0] < interval2[0] ? interval1 : interval2;
            }
        }

        while (i1 != n1) {
            int[] interval = list1.get(i1);
            if (prev[0] < interval[1]) {
                prev[0] = Math.min(prev[0], interval[0]);
                prev[1] = Math.max(prev[1], interval[1]);
                i1++;
            } else {
                res.add(prev);
                prev = interval;
            }
        }

        while (i2 != n2) {
            int[] interval = list2.get(i2);
            if (prev[0] < interval[1]) {
                prev[0] = Math.min(prev[0], interval[0]);
                prev[1] = Math.max(prev[1], interval[1]);
                i1++;
            } else {
                res.add(prev);
                prev = interval;
            }
        }

        res.add(prev);

        return res;
    }

    // [[0, 7], [8, 16]]
}
