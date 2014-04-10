public class Counter {

	private int counter;

	public Counter() {

	}

	public void count() {
		++counter;
	}

	public String toString() {
		return "Counter: " + counter;
	}
}
