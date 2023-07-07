package com.domaciukollekce6.houseplants;

import java.util.Comparator;

// Seřazení rostlin dle data poslední zálivky - domácí úkol lekce 6 bod 3b, 4 a 5 - nutno přidat třídu s komparátorem,
// aby se zachovala možnost řazení i podle názvu rostliny, která je oštřena již přímo ve třídě Plant
public class PlantLastWateringDateComparator implements Comparator<Plant> {
    @Override
    public int compare(Plant plant, Plant otherPlant) {
        return plant.getPlantLastWateringDate().compareTo(otherPlant.getPlantLastWateringDate());
    }

}
