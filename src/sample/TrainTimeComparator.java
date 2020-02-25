package sample;

import java.util.Comparator;

public class TrainTimeComparator implements Comparator<Train> {

    @Override
    public int compare(Train o1, Train o2) {
        return Long.compare(o1.getWeight(), o2.getWeight());
    }
}
