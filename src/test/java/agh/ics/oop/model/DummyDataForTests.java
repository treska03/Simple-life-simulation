package agh.ics.oop.model;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.Boundary;

public class DummyDataForTests {

    public static void setUpConstants(int simulationId) {
        boolean BACK_AND_FORTH = false;
        int NUMBER_OF_GENS = 10;
        int MIN_MUTATIONS = 0;
        int MAX_MUTATIONS = 3;
        int MIN_ENERGY_FOR_REPRODUCTION = 30;
        int ENERGY_USED_FOR_REPRODUCTION = 25;
        int NEW_ANIMAL_ENERGY = 15;
        int DAILY_NEW_GRASS_NUMBER = 25;
        int ENERGY_FROM_PLANT = 20;
        Boundary MAP_BOUNDARY = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        Constants mockConsts = new Constants(BACK_AND_FORTH, NUMBER_OF_GENS, MIN_MUTATIONS,
                MAX_MUTATIONS, MIN_ENERGY_FOR_REPRODUCTION, ENERGY_USED_FOR_REPRODUCTION,
                NEW_ANIMAL_ENERGY, DAILY_NEW_GRASS_NUMBER, ENERGY_FROM_PLANT, MAP_BOUNDARY);

        ConstantsList.addToConstantsList(simulationId, mockConsts);
    }

}