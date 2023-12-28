package agh.ics.oop.model.util;

import agh.ics.oop.model.creatures.Animal;

import java.util.Comparator;
import java.util.List;

public class AnimalPrioritySorter {

    public static void sortAnimals(List<Animal> animalList) {
        // Sorts by all 3 variables given in FAQ of exercise.
        // If are non-decisive, then its pseudo random.
        sortByChildrenNo(animalList);
        sortByAge(animalList);
        sortByEnergy(animalList);
    }

    public static void sortByEnergy(List<Animal> animalList) {
        // Sorts in place by energy of each animal
        animalList.sort(Comparator.comparingInt(Animal::getCurrentEnergy));
    }

    public static void sortByAge(List<Animal> animalList) {
        // Sorts in place by Age of each animal
        animalList.sort(Comparator.comparingInt(animal -> animal.getGenes().getCurrentMove()));
    }
    public static void sortByChildrenNo(List<Animal> animalList) {
        // Sorts in place by Children number of each animal
        animalList.sort(Comparator.comparingInt(Animal::getChildrenNumber));
    }

}
