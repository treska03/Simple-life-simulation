package agh.ics.oop.model.util;

import agh.ics.oop.model.creatures.Animal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

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
        List<Animal> mutableCopy = new ArrayList<>(animalList);
        mutableCopy.sort(Comparator.comparingInt(Animal::getCurrentEnergy).reversed());
    }

    public static void sortByAge(List<Animal> animalList) {
        // Sorts in place by Age of each animal
        // TODO: Fix not reversing comparator.
        List<Animal> mutableCopy = new ArrayList<>(animalList);
        mutableCopy.sort(Comparator.comparingInt(animal -> animal.getGenome().getCurrentMove()));
    }
    public static void sortByChildrenNo(List<Animal> animalList) {
        // Sorts in place by Children number of each animal
        List<Animal> mutableCopy = new ArrayList<>(animalList);
        mutableCopy.sort(Comparator.comparingInt(Animal::getChildrenNumber).reversed());
    }
}
