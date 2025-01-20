package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    /**
     * Сложность: O(n * m). (n - количество строк, m - количество юнитов в строке)
     */
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> allSuitableUnits = new ArrayList<>();

        // Ищем подходящих юнитов в каждой строке
        for (List<Unit> row : unitsByRow) {
            allSuitableUnits.addAll(findSuitableUnitsInRow(row, isLeftArmyTarget));
        }

        return allSuitableUnits;
    }

    // Поиск подходящих юнитов в строке
    private List<Unit> findSuitableUnitsInRow(List<Unit> row, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (int index = 0; index < row.size(); index++) {
            Unit unit = row.get(index);

            // Проверяем, что юнит жив и соответствует критериям (самый левый или правый)
            if (unit != null && unit.isAlive() &&
                    (isLeftArmyTarget ? isRightmostUnit(row, index) : isLeftmostUnit(row, index))) {
                suitableUnits.add(unit);
            }
        }

        return suitableUnits;
    }

    //Проверка, является ли юнит самым правым в ряду
    private boolean isRightmostUnit(List<Unit> row, int unitIndex) {
        // Проверяем, что юнит правый, т.е. за ним нет живых юнитов
        for (int i = unitIndex + 1; i < row.size(); i++) {
            if (row.get(i) != null && row.get(i).isAlive()) {
                return false;
            }
        }
        return true;
    }

    //Проверка, является ли юнит самым левым в ряду

    private boolean isLeftmostUnit(List<Unit> row, int unitIndex) {
        // Проверяем, что юнит левый, т.е. перед ним нет живых юнитов
        for (int i = unitIndex - 1; i >= 0; i--) {
            if (row.get(i) != null && row.get(i).isAlive()) {
                return false;
            }
        }
        return true;
    }
}
