package sample.graph;

import java.util.List;

public interface GraphNode<BRIDGE extends GraphBridge> {

    // Список выходящих мостов
    List<BRIDGE> getOuts();

    // Список входящих мостов
    List<BRIDGE> getInputs();
}
