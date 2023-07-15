import com.domaciukollekce6.houseplants.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.domaciukollekce6.houseplants.Settings.delimiter;

public class HousePlants {

    // Pomocné metody pro třídu HousePlants
    private static void printPlantsPeopleDateOutput(Plant plant) {
        System.out.println(delimiter() + plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                + plant.getPlantNormalWateringFrequency() + delimiter()
                + plant.getPlantLastWateringDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")) + delimiter()
                + plant.getPlantPlantingDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")));
    }
    private static void printPlantsComputerOutput(Plant plant) {
        System.out.println(delimiter() + plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                + plant.getPlantNormalWateringFrequency() + delimiter() + plant.getPlantLastWateringDate()
                + delimiter() + plant.getPlantPlantingDate());
    }
    public static void main(String[] args) throws PlantException {

        // Pomocná proměnná pro vyhledávání rostlin zasazených za poslední měsíc od dnešního datumu
        LocalDate lastMonthPlanting = LocalDate.now().minusMonths(1);

        PlantManager plantManager = new PlantManager();

        // Výpis dní, kdy byla zasazena alespoň jedna rostlina - Domácí úkol lekce 6 bod 6 - část 1
        HashSet<LocalDate> uniquePlantingDates = new HashSet<>();

        // Výpis rostlin, které byli zasazeny za poslední měsíc od dnešního datumu - Domácí úkol lekce 6 - VÝZVA část 1
        HashSet<Plant> plantsPlantingLastMonth = new HashSet<>();


        try {plantManager.loadDataPlantsFromFile(Settings.fileNamePrimary(), delimiter());}
        catch (PlantException e) {
            System.err.println("Nepodařilo se načíst data ze souboru " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        // Toto musí být až po natažení dat, jinak je list prázdný.
        List<Plant> plantList = plantManager.getPlantList();

        System.out.println();
        System.out.println("Surová data ze VSTUPNÍHO souboru DB-ListOfPlantsPrimary.txt:");
        for (Plant plant : plantList) {printPlantsComputerOutput(plant);}

        System.out.println();
        System.out.println("Informace o zálivce dle zadání domácího úkolu v bodě 13:");
        for (Plant plant : plantList) {printPlantsComputerOutput(plant);}

        System.out.println();
        System.out.println("Informace o zálivce dle zadání domácího úkolu v bodě 13 z GETTERu ve třídě Plant:");
        for (Plant plant : plantList) {
            System.out.println(plant.getWateringInfo());
        }

        // Oprava přidávání do skutečného seznamu a ne do jeho kopie, jak to bylo před tím
        // !!! na toto do budoucna bacha! - to je fatální chyba (to je pro mě) :-)
        try {
            plantManager.addPlant(new Plant("Jahodník", "na zábradlí balkónu", LocalDate.now(),
                    LocalDate.now(),
                    3));
            plantManager.addPlant(new Plant("Mochíto Máta", "na balkóně", LocalDate.now(),
                    LocalDate.now(), 2));
        }
        catch (PlantException e) {
            System.err.print("Nastala chyba při přidávání nových rostlin" + e.getLocalizedMessage());
        }

        // ! Tady jsem musel znovu natáhnout ten List, jina se změny neprojeví na výstupu na obrazovku
        plantList = plantManager.getPlantList();

        System.out.println();
        System.out.println("Aktualizovaný seznam rostlin po přidání dvou rostlin dle zadání domácího úkolu v bodě 14:");
        for (Plant plant : plantList) {printPlantsPeopleDateOutput(plant);}

        // Oprava odebírání ze skutečného seznamu a ne z jeho kopie, jak to bylo před tím
        // !!! na toto do budoucna bacha! - to je fatální chyba (to je pro mě) :-)
        plantManager.removePlantByName("Sukulent v koupelně"); plantList = plantManager.getPlantList();

        System.out.println();
        System.out.println
                ("Aktualizovaný seznam rostlin po odebrání jedné rostliny dle zadání domácího úkolu v bodě 14:");
        for (Plant plant : plantList) {printPlantsPeopleDateOutput(plant);}

        System.out.println();
        try {
            plantManager.saveDataPlantsToNewFile(Settings.fileNameAfterChanges(), plantList);
            System.out.println("Aktualizovaný seznam rostlin byl uložen do souboru: "
                    + Settings.fileNameAfterChanges());
        } catch (PlantException e) {
            System.err.println("Chyba při ukládání dat: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try {plantManager.loadDataPlantsFromFile(Settings.fileNameAfterChanges(), delimiter());}
        catch (PlantException e) {
            System.err.println("Nepodařilo se načíst data ze souboru " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println("Surová data z VÝSTUPNÍHO souboru DB-ListOfPlantsAfterChanges.txt:");
        for (Plant plant : plantList) {printPlantsComputerOutput(plant);}

        // OPRAVA - Řazení volané metodou v PlantManageru - ALE PODLE MĚ TO JE ŠPATNĚ
        // Seřazení rostlin dle názvu od A do Z a zobrazení - domácí úkol lekce 6 bod 3
        plantManager.sortPlantsByName();
        plantList = plantManager.getPlantList();
        System.out.println();
        System.out.println("Seřazený seznam rostlin dle názvu rostliny:");
        for (Plant plant : plantList) {printPlantsPeopleDateOutput(plant);
        }





        // ORAVA ŘAZENÍ - koná se nově přímo v PlantManageru
        // Seřazení rostlin dle data poslední zálivky od nejstaršího po nejnovější - domácí úkol lekce 6 bod 3b, 4 a 5
//        plantManager.sortPlantsByLastWateringDate(); plantList = plantManager.getPlantList();
//        System.out.println();
//        System.out.println("Seřazený seznam rostlin dle data poslední zálivky od rostliny, " +
//                "která byla zalita před nejdelší dobou:");
//        for (Plant plant : plantList) {
//            printPlantsPeopleDateOutput(plant);
//        }

        // Výpis dní, kdy byla zasazena alespoň jedna rostlina - Domácí úkol lekce 6 bod 6 - část 2
        System.out.println();
        System.out.println("Výpis dnů, kdy byla zasazena alespoň jedna rostlina:");
        // Ten HashSet evidentně v základu ignoruje shodné datumy, jinak by to nemohlo fungovat - ale funguje. ???
        for (Plant plant : plantList) {uniquePlantingDates.add(plant.getPlantPlantingDate());}
        for (LocalDate date : uniquePlantingDates) {
            System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        // Výpis rostlin, které byli zasazeny za poslední měsíc od dnešního datumu - Domácí úkol lekce 6 - VÝZVA část 2
        for (Plant plant : plantList) {
            if (plant.getPlantPlantingDate().isAfter(lastMonthPlanting)) {plantsPlantingLastMonth.add(plant);}
        }
        System.out.println();
        System.out.println("Rostliny zasazené za poslední měsíc od dnešního dne:");
        for (Plant plant : plantsPlantingLastMonth) {printPlantsPeopleDateOutput(plant);}

    }

}