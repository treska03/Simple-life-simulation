package agh.ics.oop.model;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;

public class ConstantSetterForTests {
    private boolean BACK_AND_FORTH = false;
    private int NUMBER_OF_GENS = 10;
    private int MIN_MUTATIONS = 0;
    private int MAX_MUTATIONS = 3;
    private int MIN_ENERGY_FOR_REPRODUCTION = 30;
    private int ENERGY_USED_FOR_REPRODUCTION = 25;
    private int STARTING_ANIMALS_NUMBER = 15;
    private int NEW_ANIMAL_ENERGY = 15;
    private int DAILY_NEW_GRASS_NUMBER = 25;
    private int ENERGY_FROM_PLANT = 20;
    private Boundary MAP_BOUNDARY = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

    public void setUpConstants(int simulationId) {

        Constants mockConsts = new Constants(BACK_AND_FORTH, NUMBER_OF_GENS, MIN_MUTATIONS,
                MAX_MUTATIONS, MIN_ENERGY_FOR_REPRODUCTION, ENERGY_USED_FOR_REPRODUCTION,
                STARTING_ANIMALS_NUMBER,
                NEW_ANIMAL_ENERGY, DAILY_NEW_GRASS_NUMBER, ENERGY_FROM_PLANT, MAP_BOUNDARY);

        ConstantsList.addToConstantsList(simulationId, mockConsts);
    }

    public void setBACK_AND_FORTH(boolean BACK_AND_FORTH) {
        this.BACK_AND_FORTH = BACK_AND_FORTH;
    }

    public void setNUMBER_OF_GENS(int NUMBER_OF_GENS) {
        this.NUMBER_OF_GENS = NUMBER_OF_GENS;
    }

    public void setMIN_MUTATIONS(int MIN_MUTATIONS) {
        this.MIN_MUTATIONS = MIN_MUTATIONS;
    }

    public void setMAX_MUTATIONS(int MAX_MUTATIONS) {
        this.MAX_MUTATIONS = MAX_MUTATIONS;
    }

    public void setMIN_ENERGY_FOR_REPRODUCTION(int MIN_ENERGY_FOR_REPRODUCTION) {
        this.MIN_ENERGY_FOR_REPRODUCTION = MIN_ENERGY_FOR_REPRODUCTION;
    }

    public void setENERGY_USED_FOR_REPRODUCTION(int ENERGY_USED_FOR_REPRODUCTION) {
        this.ENERGY_USED_FOR_REPRODUCTION = ENERGY_USED_FOR_REPRODUCTION;
    }

    public void setSTARTING_ANIMALS_NUMBER(int STARTING_ANIMALS_NUMBER) {
        this.STARTING_ANIMALS_NUMBER = STARTING_ANIMALS_NUMBER;
    }

    public void setNEW_ANIMAL_ENERGY(int NEW_ANIMAL_ENERGY) {
        this.NEW_ANIMAL_ENERGY = NEW_ANIMAL_ENERGY;
    }

    public void setDAILY_NEW_GRASS_NUMBER(int DAILY_NEW_GRASS_NUMBER) {
        this.DAILY_NEW_GRASS_NUMBER = DAILY_NEW_GRASS_NUMBER;
    }

    public void setENERGY_FROM_PLANT(int ENERGY_FROM_PLANT) {
        this.ENERGY_FROM_PLANT = ENERGY_FROM_PLANT;
    }

    public void setMAP_BOUNDARY(Boundary MAP_BOUNDARY) {
        this.MAP_BOUNDARY = MAP_BOUNDARY;
    }
}