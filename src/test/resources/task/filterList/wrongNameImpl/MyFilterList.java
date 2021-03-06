import java.util.ArrayList;
import java.util.List;

public class MyFilterList<T> {

    private List<T> list;

    public MyFilterList(List<T> list) {
        this.list = list;
    }

    public List<T> filter(Predicate<T> filter) {
        List<T> result = new ArrayList<T>(list.size());

        for (T t : list) {
            if (filter.accecpt(t))
                result.add(t);
        }

        return result;
    }

}
