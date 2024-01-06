package agh.ics.oop.model.info;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.algorithms.DFS;
import agh.ics.oop.model.util.GraphVertex;

import java.util.*;

public class Stats {
    private final int NUMBER_OF_TICKS; // time of simulation
    private final Map<UUID, GraphVertex> familyTree = new HashMap<>();
    private Animal markedAnimal;
    private final HashSet<UUID> descendants = new HashSet<>();
    private final int[] numberOfEachGenotype = new int[8];
    private final HashSet<Animal>[] commonGenotypeAnimals = new HashSet[8];

    public Stats(int NUMBER_OF_TICKS) {
        this.NUMBER_OF_TICKS = NUMBER_OF_TICKS;
        for (int i = 0; i < 8; i++) {
            this.commonGenotypeAnimals[i] = new HashSet<>();
        }
    }

    public void reportAddingStartingAnimal (Animal animal){
        // adding animal to familyTree
        GraphVertex animalVertex = new GraphVertex(animal.getId());
        familyTree.put(animal.getId(), animalVertex);
        addToGenomeStats(animal);
    }

    public void reportAddingAnimalHavingParents (Animal child, Animal parent1, Animal parent2){
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
            addToGenomeStats(child);
        }
    }

    private void addToGenomeStats (Animal animal){
        int[] genes = animal.getGenome().getMoveList();
        for (int gene : genes){
            // adding genes of this animal to statistics
            numberOfEachGenotype[gene] +=1;
            // assigning animal to its genotypes statistics
            commonGenotypeAnimals[gene].add(animal); // if it's already there, no effects
        }
    }

    public void reportDeathOfAnimal (Animal animal){
        removeFromGenomeStats(animal);
    }

    private void removeFromGenomeStats (Animal animal){
        int[] genes = animal.getGenome().getMoveList();
        for (int gene : genes){
            // removing genes of this animal from statistics
            numberOfEachGenotype[gene] -=1;
            // the dead animal no longer is counted
            commonGenotypeAnimals[gene].remove(animal); // if it's not there, no effects
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

    public int[] getNumberOfEachGenotype() {
        return numberOfEachGenotype;
    }

    public HashSet<Animal> getPopularGenotypeAnimals(){
        /*
         getting animals having the most popular genotype(s)
         if the number of some genotypes are equal,
         the animal that have one of its genes are counted;
         return the HashSet of animals;
        */
        int maxValue = numberOfEachGenotype[0];
        List<Integer> indicesWithMaxValue = new ArrayList<>(List.of(0));
        for (int i=1; i<8; i++){
            int nextSize = numberOfEachGenotype[i];
            if (nextSize > maxValue){
                maxValue = nextSize;
                indicesWithMaxValue.clear();
                indicesWithMaxValue.add(i);
            }
            else if (nextSize == maxValue){
                indicesWithMaxValue.add(i);
            }
        }
        HashSet<Animal> popularGenotypeAnimals = new HashSet<>();
        for (int genotype : indicesWithMaxValue){
            popularGenotypeAnimals.addAll(commonGenotypeAnimals[genotype]);
        }
        return popularGenotypeAnimals;
    }

    public int[] getGenomeFromMarkedAnimal(){
        return markedAnimal.getGenome().getMoveList();
    }

    public int getActivatedGeneFromMarkedAnimal(){
        return markedAnimal.getGenome().getActivatedGene();
    }

    // only for tests
    public Map<UUID, GraphVertex> getFamilyTreeForTests() {
        return familyTree;
    }

    public HashSet<UUID> getDescendantsForTests() {
        return descendants;
    }
}
