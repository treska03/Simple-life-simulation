package agh.ics.oop.model.util;

import agh.ics.oop.model.creatures.Animal;

import java.util.Collections;
import java.util.List;

public class MaximalAnimalsFinder {

    public Animal getOneMax (List<Animal> listOfAnimals){
        // the method takes nonempty list of animals

        /*
         the shuffle is made to ensure that if the animals are compared to be equal
         it takes a random choice which is greater;
         in that case the animal is assumed to be greater if it is earlier in the list;
         because animals are shuffled every of them have the same chance
         to be earlier in the list as the others;
        */
        Collections.shuffle(listOfAnimals);

        // iterate throw animals in the list to find the max element
        Animal maxAnimal = listOfAnimals.get(0);
        for (Animal currAnimal : listOfAnimals){
            if (isGreater(currAnimal, maxAnimal)){
                maxAnimal = currAnimal;
            }
        }
        return maxAnimal;
    }

    public List<Animal> getTwoMax (List<Animal> listOfAnimals){
        // the method takes the list of animals of minimum size of 2

        /*
         the shuffle is made to ensure that if the animals are compared to be equal
         it takes a random choice which is greater;
         in that case the animal is assumed to be greater if it is earlier in the list;
         because animals are shuffled every of them have the same chance
         to be earlier in the list as the others;
        */
        Collections.shuffle(listOfAnimals);

        // iterate throw animals in the list to find the max element
        // and remember its list index;
        Animal maxAnimal = listOfAnimals.get(0);
        int maxIdx = 0;
        for (int i = 1; i < listOfAnimals.size(); i++){
            if (isGreater(listOfAnimals.get(i), maxAnimal)){
                maxAnimal = listOfAnimals.get(i);
                maxIdx = i;
            }
        }

        Animal maxAnimal2;

        /*
         find the index from which to start a loop
         and set temporarily candidate for the second max element
         in the way that it won't be the same as the max element;
        */
        int startIdx;
        if (maxIdx != 0){
            startIdx = 0;
            maxAnimal2 = listOfAnimals.get(0);
        }
        else {
            startIdx = 1;
            maxAnimal2 = listOfAnimals.get(1);
        }

        // iterate throw animals in the list to find the second max element
        // ensure that we don't compare the max element
        for (int i = startIdx; i < listOfAnimals.size(); i++){
            if (i != maxIdx && isGreater(listOfAnimals.get(i), maxAnimal2)){
                maxAnimal2 = listOfAnimals.get(i);
            }
        }
        return List.of(maxAnimal, maxAnimal2);
    }

    private boolean isGreater(Animal animal1, Animal animal2) {
        // check if animal1 is greater than animal2,
        // if so return true, otherwise return false

        // check the energy
        if (animal1.getCurrentEnergy() > animal2.getCurrentEnergy()) {
            return true;
        } else if (animal1.getCurrentEnergy() == animal2.getCurrentEnergy()) {
            // check the age (by the date of birth)
            if (animal1.getDateOfBirth() < animal2.getDateOfBirth()) {
                return true;
            } else if (animal1.getDateOfBirth() == animal2.getDateOfBirth()) {
                // check the number of children
                return animal1.getChildrenNumber() > animal2.getChildrenNumber();
            }
        }
        return false;
    }

    public boolean checkIsGreaterForTests (Animal animal1, Animal animal2){
        return isGreater(animal1, animal2);
    }
}