public class SpecialNumbers {

    public static int[][] amicablePairs(int n) {
        int[][] result = new int[n][2];

        for (int i = 3, cnt = 0; cnt < n; ++i) {
            int tmp = getTeilerSum(i);
            if (tmp > i && getTeilerSum(tmp) == i) {
                result[cnt][0] = i;
                result[cnt][1] = tmp;
                ++cnt;
            }

        }

        return result;
    }

    private static int getTeilerSum(int number) {
        int sum = 1;
        int border = (int) Math.sqrt(number);
        for (int i = 2; i <= border; ++i) {
            if (number % i == 0)
                sum += (i + (number / i));
        }

        return sum;
    }

    public static void main(String[] args) {
        int[][] result = amicablePairs(20);
        for (int[] array : result)
            System.out.println(array[0] + "," + array[1]);

    }
}