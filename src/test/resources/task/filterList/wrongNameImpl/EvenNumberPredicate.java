public class EvenNumberPredicate implements Predicate<Integer> {

    @Override
    public boolean accecpt(Integer ele) {
        return ele.intValue() % 2 == 1;
    }
}
