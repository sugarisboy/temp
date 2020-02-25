package sample.graph;

import java.time.ZonedDateTime;

public interface GraphBridge<NODE, BRIDGE extends GraphBridge> extends Comparable<BRIDGE> {

    // Пункт отправления
    NODE getA();

    // Пункт назначения
    NODE getB();

    // Вес ребра
    long getWeight();

    // Отображает актуально ли еще ребро
    boolean isExpired(ZonedDateTime time);

    // Время отправления
    ZonedDateTime getTimeOut();

    // Время прихода
    ZonedDateTime getTimeIn();

    default int compareTo(BRIDGE o) {
        return Long.compare(this.getWeight(), o.getWeight());
    }
}
