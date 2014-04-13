import java.util.ArrayList;
import java.util.List;

public class FilterList<T> {

    private List<T> list;

    public FilterList(List<T> list) {
        this.list = list;
    }

    public List<T> filter(Predicate<T> filter) {
        // Implement function
    }

}
