public class MyCounter {

	private int counter;

	public MyCounter() {

	}

	public void count() {
		++counter;
	}

	public String toString() {
		return "Counter: " + counter;
	}
}
