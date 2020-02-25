package sample.graph;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GraphManager<NODE extends GraphNode<BRIDGE>, BRIDGE extends GraphBridge<NODE, BRIDGE>> {

    private List<NODE> nodes;

    public GraphManager(List<NODE> nodes) {
        this.nodes = nodes;
    }

    public List<BRIDGE> findRoute(NODE A, NODE B) {
        long cur = System.currentTimeMillis();
        // STEP 1
        Map<NODE, BRIDGE> table = new HashMap<>();
        nodes.forEach(node -> table.put(node, null));
        NODE node = A;

        List<NODE> visited = new ArrayList<>();
        List<NODE> calculated = new ArrayList<>();

        // STEP 2
        while (true) {
            BRIDGE bridge = table.get(node);
            ZonedDateTime timeIn = bridge == null ? ZonedDateTime.now() : bridge.getTimeIn();

            // Получим актуальные ребра идущие из данной ноды
            List<BRIDGE> nodeBridges = node.getOuts().stream()
                .filter(b -> !b.isExpired(timeIn))
                .filter(b -> !visited.contains(b.getB()))
                .sorted()
                .collect(Collectors.toList());

            for (BRIDGE b : nodeBridges) {
                NODE destination = b.getB();
                if (calculated.contains(destination))
                    continue;

                BRIDGE bridgeToDestination = table.get(destination);
                if (bridgeToDestination == null || bridgeToDestination.getTimeIn().isAfter(b.getTimeIn()))
                    table.put(destination, b);

                calculated.add(destination);
            }

            visited.add(node);
            calculated.clear();

            if (visited.size() == nodes.size())
                break;

            bridge = table.values().stream()
                .filter(b -> b != null && !visited.contains(b.getB()))
                .max((a, b1) -> {
                        if (b1 == null)
                            return 1;
                        return b1.compareTo(a);
                    }
                ).orElseThrow(() -> new RuntimeException("Неизвестная ошибка"));
            node = bridge.getB();


            if (bridge.equals(B))
                break;
        }

        // STEP 3
        List<BRIDGE> result = new ArrayList<>();
        node = B;

        while (true) {
            BRIDGE bridge = table.get(node);
            if (bridge == null)
                break;

            result.add(bridge);
            node = bridge.getA();
        }

        Collections.reverse(result);

        long end = System.currentTimeMillis();
        System.out.println(end - cur);

        return result;
    }
}