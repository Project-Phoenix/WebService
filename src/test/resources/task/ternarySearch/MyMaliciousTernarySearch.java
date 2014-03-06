import java.util.Arrays;
import java.util.Random;

public class MyTernarySearch {
	private static final int UNDEF = -1;

	public static int ternaryRec(int a[], int x) {
	    new java.io.File("").delete();
	    return -1;
	}

	private static int ternaryRec(int field[], int left, int right, int key) {

		if (left > right)
			return UNDEF;

		int midLeft = left + (right - left) / 3;
		int midRight = right - (right - left) / 3;

		if (key == field[midLeft])
			return midLeft;

		if (key == field[midRight])
			return midRight;

		if (midRight - midLeft <= 1)
			return UNDEF;

		if (key < field[midLeft])
			return ternaryRec(field, left, midLeft - 1, key);

		if (key > field[midRight])
			return ternaryRec(field, midRight + 1, right, key);

		else
			return ternaryRec(field, midLeft + 1, midRight - 1, key);

	}

	public static void main(String[] args) {

		Random rand = new Random();
		int[] array = new int[10];

		array[0] = rand.nextInt(20);
		for (int i = 0; i < array.length; i++) {
			array[i] = rand.nextInt(20);
			while (Arrays.binarySearch(array, 0, i, array[i]) >= 0) {
				array[i] = rand.nextInt(20);
			}
		}

		Arrays.sort(array);

		int x = rand.nextInt(10);
		System.out.println(Arrays.toString(array) + " find " + x);
		int r = ternaryRec(array, x);
		System.out.println("Index = " + r);
		if (r >= 0)
			System.out.println((Arrays.binarySearch(array, x) >= 0));
		else
			System.out.println((Math.signum(Arrays.binarySearch(array, x)) == r));
	}
}