package agh.ics.oop.model.info;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.util.DFS;
import agh.ics.oop.model.util.GraphVertex;

import java.util.*;

public class Stats {
    private final int NUMBER_OF_TICKS; // time of simulation
    private final Map<UUID, GraphVertex> familyTree = new HashMap<>();
    private Animal markedAnimal;
    private final HashSet<UUID> descendants = new HashSet<>();

    public Stats(int NUMBER_OF_TICKS) {
        this.NUMBER_OF_TICKS = NUMBER_OF_TICKS;
    }

    public void addStartingAnimal (Animal animal){
        // adding animal to familyTree
        GraphVertex animalVertex = new GraphVertex(animal.getId());
        familyTree.put(animal.getId(), animalVertex);
    }

    public void addAnimalHavingParents (Animal child, Animal parent1, Animal parent2){
        // adding child to familyTree
        GraphVertex childVertex = new GraphVertex(child.getId());
        familyTree.put(child.getId(), childVertex);

        // assigning child to parents
        GraphVertex parent1Vertex = familyTree.get(parent1.getId());
        parent1Vertex.addChild(childVertex);
        GraphVertex parent2Vertex = familyTree.get(parent2.getId());
        parent2Vertex.addChild(childVertex);

        // adding child to descendantList if markedAnimal exist
        // and one of parents is markedAnimal or one of its descendants;
        if (!(markedAnimal == null)){
            boolean check1 = descendants.contains(parent1Vertex.getId());
            boolean check2 = descendants.contains(parent2Vertex.getId());
            boolean check3 = (parent1 == markedAnimal);
            boolean check4 = (parent2 == markedAnimal);
            if (check1 || check2 || check3 || check4) {
                descendants.add(child.getId());
            }
        }
    }

    public void addMark (Animal markedAnimal){
        // mark markedAnimal
        // and creating its list of descendants using DFS
        this.markedAnimal = markedAnimal;
        GraphVertex markedAnimalVertex = familyTree.get(markedAnimal.getId());
        DFS dfs = new DFS();
        List<GraphVertex> descendantsList = dfs.getDescendantsList(markedAnimalVertex);
        for (GraphVertex vertex : descendantsList){
            descendants.add(vertex.getId());
        }
    }

    public void deleteMark(){
        // unmark markedAnimal
        this.markedAnimal = null;
        descendants.clear();
    }

    public int getNumberOfTicks() {
        return NUMBER_OF_TICKS;
    }

    public int getNumberOfDescendants() {
        return descendants.size();
    }

    // only for tests
    public Map<UUID, GraphVertex> getFamilyTreeForTests() {
        return familyTree;
    }

    public HashSet<UUID> getDescendantsForTests() {
        return descendants;
    }
}
