package data;

import org.osbot.rs07.api.map.Area;

public class Location {

    private String name;
    private Area area;
    private String[] npcs;

    public Location(String name, Area area, String[] npcs) {
        this.name = name;
        this.area = area;
        this.npcs = npcs;
    }

    public String getName() {
        return name;
    }

    public Area getArea() {
        return area;
    }

    public String[] getNpcs() {
        return npcs;
    }
}
