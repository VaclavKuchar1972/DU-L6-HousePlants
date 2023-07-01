package com.domaciukollekce6.houseplants;

import java.time.format.DateTimeFormatter;

public class Settings {
    public static String fileNamePrimary() {return "DB-ListOfPlantsPrimary.txt";}
    public static String fileNameAfterChanges() {return "DB-ListOfPlantsAfterChanges.txt";}
    public static String delimiter() {return "\t";}

    public static void printPlantsPeopleDateOutput(Plant plant) {
        System.out.println("\t" + plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                + plant.getPlantNormalWateringFrequency() + delimiter()
                + plant.getPlantLastWateringDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")) + delimiter()
                + plant.getPlantPlantingDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")));
    }

    public static void printPlantsComputerOutput(Plant plant) {
        System.out.println("\t" + plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                + plant.getPlantNormalWateringFrequency() + delimiter() + plant.getPlantLastWateringDate()
                + delimiter() + plant.getPlantPlantingDate());
    }

}