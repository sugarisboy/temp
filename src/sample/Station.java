package sample;

import sample.graph.GraphNode;

import java.util.ArrayList;
import java.util.List;

public class Station implements GraphNode<Train> {

    private List<Train> outs;
    private List<Train> inputs;

    private String name;

    public Station(String name) {
        this.name = name;
        outs = new ArrayList<>();
        inputs = new ArrayList<>();
    }

    public void addInputTrain(Train train) {
        if (!inputs.contains(train))
            inputs.add(train);
    }

    public void addOutputTrain(Train train) {
        if (!outs.contains(train))
            outs.add(train);
    }

    public void removeTrain(Train train) {
        outs.remove(train);
        inputs.remove(train);
    }

    public String getName() {
        return name;
    }

    @Override
    public List<Train> getOuts() {
        return outs;
    }

    @Override
    public List<Train> getInputs() {
        return inputs;
    }
}
