package grandexchange;

import Core.CombatTrainer;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Entity;

import java.util.*;
import java.util.stream.Stream;

public enum Bank {

    DRAYNOR(Banks.DRAYNOR),

    AL_KHARID(Banks.AL_KHARID),

    LUMBRIDGE(Banks.LUMBRIDGE_UPPER),

    FALADOR_EAST(Banks.FALADOR_EAST),

    FALADOR_WEST(Banks.FALADOR_WEST),

    VARROCK_EAST(Banks.VARROCK_EAST),

    VARROCK_WEST(Banks.VARROCK_WEST),

    EDGEVILLE(Banks.EDGEVILLE),

    GRAND_EXCHANGE(Banks.GRAND_EXCHANGE);

    private final Area area;

    Bank(Area area) {
        this.area = area;
    }

    public static Area closestTo(Entity e) {

        HashMap<Bank, Integer> distMap = new HashMap<Bank, Integer>();

        for (Bank b : Bank.values()) {
            distMap.put(b, e.getPosition().distance(b.area.getRandomPosition()));
        }

        HashMap<Integer, Bank> distMapSorted = sortByDistance(distMap);

        Area cBank = distMapSorted.values().toArray(new Bank[Bank.values().length])[0].area;
        return cBank;

    }

    public static boolean insideABank(CombatTrainer player) {
        for(Bank bank : Bank.values()) {
            if (bank.area.contains(player.myPlayer())) {
                return true;
            }
        }
        return false;
    }

    private static <K, V extends Comparable<? super V>> HashMap<V, K> sortByDistance(Map<K, V> map) {

        HashMap<V, K> result = new LinkedHashMap<>();

        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue()).forEachOrdered(e -> result.put(e.getValue(), e.getKey()));

        return result;

    }
}