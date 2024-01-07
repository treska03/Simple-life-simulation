package agh.ics.oop.model.info;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.algorithms.DFS;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.GraphVertex;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;

import static java.lang.Math.min;

public class Stats {
    private Constants constants;
    private int day = 1;
    private final int[] numberOfEachGenotype = new int[8];
    private int numberOfLiveAnimals = 0;
    private int numberOfPlants;
    private int numberOfEmptyFields;
    private int numberOfNewAnimals = 0;
    private int sumOfEnergy = 0;
    private int sumOfDaysOfLiving = 0;
    private int numberOfDeadAnimals = 0;
    private int sumOfChildrenNumber = 0;
    private final HashSet<Animal>[] commonGenotypeAnimals = new HashSet[8];
    private Animal markedAnimal;
    private boolean markedAnimalDead = false;
    private int daysOfLiving;
    private int dayOfDeath;
    private final HashSet<UUID> descendants = new HashSet<>();
    private final Map<UUID, GraphVertex> familyTree = new HashMap<>();

    public Stats(int simulationID) {
        this.constants = ConstantsList.getConstants(simulationID);
        for (int i = 0; i < 8; i++) {
            this.commonGenotypeAnimals[i] = new HashSet<>();
        }
    }

    public void reportAddingStartingAnimal (Animal animal){
        // adding animal to familyTree
        GraphVertex animalVertex = new GraphVertex(animal.getId());
        familyTree.put(animal.getId(), animalVertex);

        numberOfLiveAnimals++;
        sumOfEnergy += constants.getNewAnimalEnergy();
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
        }

        numberOfLiveAnimals++;
        numberOfNewAnimals++;
        sumOfChildrenNumber += 2;
        addToGenomeStats(child);
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
        numberOfDeadAnimals++;
        numberOfLiveAnimals--;
        sumOfChildrenNumber -= animal.getChildrenNumber();
        sumOfDaysOfLiving += (day - animal.getDateOfBirth());

        // change statistics for marked animal if that was it who died
        if (animal == markedAnimal){
            markedAnimalDead = true;
            dayOfDeath = day;
            daysOfLiving = 0;
        }

        // the animal's current energy was already subtracted by daily energy loss;
        sumOfEnergy -= (constants.getDailyEnergyLoss() + animal.getCurrentEnergy());

        removeFromGenomeStats(animal);
    }

    private void removeFromGenomeStats (Animal animal){
        int[] genes = animal.getGenome().getMoveList();
        for (int gene : genes){
            // removing genes of this animal from statistics
            numberOfEachGenotype[gene] -=1;
            // the dead animal is no longer counted
            commonGenotypeAnimals[gene].remove(animal); // if it's not there, no effects
        }
    }

    public void reportPlantConsumption(){
        sumOfEnergy += constants.getEnergyFromPlant();
    }

    public void reportEndOfTheDay(WorldMap map){
        day++;

        // subtract daily energy loss for all animals
        // that are alive and hasn't been born that day;
        sumOfEnergy -= (numberOfLiveAnimals - numberOfNewAnimals) * constants.getDailyEnergyLoss();

        numberOfNewAnimals = 0;
        numberOfPlants = map.getPlantPositions().size();
        numberOfEmptyFields = 0;

        // counting number of all fields
        int width = constants.getMapBoundary().upperRight().getX() -
                constants.getMapBoundary().lowerLeft().getX();
        int height = constants.getMapBoundary().upperRight().getY() -
                constants.getMapBoundary().lowerLeft().getY();
        int numberOfAllFields = height * width;

        // counting how many fields contains both plant and at least one animal;
        int numberOfCombinedFields = 0;
        for (Vector2d grassPosition : map.getPlantPositions()){
            if (map.getAnimalPositions().containsKey(grassPosition)) {
                numberOfCombinedFields ++;
            }
        }

        // Use inclusion - exclusion principle
        // to calculate number of empty fields;
        numberOfEmptyFields = numberOfAllFields - numberOfPlants -
                map.getAnimalPositions().size() + numberOfCombinedFields;

        // statistics for marked animal
        if (!(markedAnimal == null) && !(markedAnimalDead)){
            daysOfLiving++;
        }
    }

    public void addMark (Animal markedAnimal){
        // if previously marked animal is still marked, unmark it
        if (!(markedAnimal == null)){
            deleteMark();
        }

        // create its list of descendants using DFS
        this.markedAnimal = markedAnimal;
        GraphVertex markedAnimalVertex = familyTree.get(markedAnimal.getId());
        DFS dfs = new DFS();
        List<GraphVertex> descendantsList = dfs.getDescendantsList(markedAnimalVertex);
        for (GraphVertex vertex : descendantsList){
            descendants.add(vertex.getId());
        }

        daysOfLiving = 1;
    }

    public void deleteMark(){
        // clear stats for marked animal
        this.markedAnimal = null;
        descendants.clear();
        daysOfLiving = 0;
        dayOfDeath = 0;
    }

    public int getDay() {
        return day;
    }

    public HashSet<Animal> getPopularGenotypeAnimals(){
        /*
         getting animals having the most popular genotype(s)
         if the number of some genotypes are equal,
         animals that have one of its genes are counted;
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

    public int getNumberOfLiveAnimals() {
        return numberOfLiveAnimals;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public int getNumberOfEmptyFields() {
        return numberOfEmptyFields;
    }

    public int[] getNumberOfEachGenotype() {
        return numberOfEachGenotype;
    }

    public double getAverageEnergy(){
        if (numberOfLiveAnimals != 0){
            return (double) sumOfEnergy / (double) numberOfLiveAnimals;
        }
        return 0; // default if there are no live animals
    }

    public double getAverageDaysOfLiving(){
        if (numberOfDeadAnimals != 0){
            return (double) sumOfDaysOfLiving / (double) numberOfDeadAnimals;
        }
        return 0; // default if there are no dead animals
    }

    public double getAverageChildrenNumber(){
        if (numberOfLiveAnimals != 0){
            return (double) sumOfChildrenNumber/ (double) numberOfLiveAnimals;
        }
        return 0; // default if there are no live animals
    }

    public Animal getMarkedAnimal() {
        return markedAnimal;
    }

    public boolean isMarkedAnimalDead() {
        return markedAnimalDead;
    }

    public int[] getGenome(){
        return markedAnimal.getGenome().getMoveList();
    }

    public int getActivatedGene(){
        return markedAnimal.getGenome().getActivatedGene();
    }

    public int getCurrentEnergy() {
        return markedAnimal.getCurrentEnergy();
    }

    public int getChildrenNumber() {
        return markedAnimal.getChildrenNumber();
    }

    public int getNumberOfEatenPlants() {
        return markedAnimal.getNumberOfEatenPlants();
    }

    public int getNumberOfDescendants() {
        return descendants.size();
    }

    public int getDaysOfLiving() {
        return daysOfLiving;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    // only for tests
    public void setNumberOfDeadAnimalsForTests(int numberOfDeadAnimals) {
        this.numberOfDeadAnimals = numberOfDeadAnimals;
    }

    // only for tests
    public void setSumOfEnergyForTests(int sumOfEnergy) {
        this.sumOfEnergy = sumOfEnergy;
    }

    // only for tests
    public Map<UUID, GraphVertex> getFamilyTreeForTests() {
        return familyTree;
    }

    // only for tests
    public HashSet<UUID> getDescendantsForTests() {
        return descendants;
    }
}
