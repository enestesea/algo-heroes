package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    /**
     * Сложность: O(n * m). (n - количество юнитов в армии игрока, m - количество юнитов в армии компьютера)
     */
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        final Set<Unit> playerUnits = new HashSet<>(playerArmy.getUnits());
        final Set<Unit> computerUnits = new HashSet<>(computerArmy.getUnits());

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            executeAttacks(playerUnits, computerUnits);
            executeAttacks(computerUnits, playerUnits);
        }
    }

    private void executeAttacks(Set<Unit> attackingUnits, Set<Unit> defendingUnits) throws InterruptedException {
        final Iterator<Unit> iterator = attackingUnits.iterator();

        // Используем копию списка, чтобы избежать ошибок при модификации коллекции во время итерации
        while (iterator.hasNext()) {
            Unit attackingUnit = iterator.next();

            // Пропускаем мертвых юнитов
            if (!attackingUnit.isAlive()) {
                iterator.remove(); // Удаляем мертвых
                continue;
            }

            // Атакуем, если есть живые юниты
            Unit target = attackingUnit.getProgram().attack();
            if (target != null && target.isAlive()) {
                // Заносим в баттллог
                printBattleLog.printBattleLog(attackingUnit, target);

                // Если цель убита, удаляем её
                if (!target.isAlive()) {
                    defendingUnits.remove(target); // Удаляем из армии противника
                }
            }
        }
    }
}
