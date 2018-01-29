package org.lxy.effective.ch6;

import java.util.ArrayList;
import java.util.List;

public class Plant {
    enum LifeCycle {ANNUAL, PERENNIAL, BIENNIAL}

    final String name;
    final LifeCycle lifeCycle;

    Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<Plant> demoList(){
        List<Plant> plantList = new ArrayList<>();
        plantList.add(new Plant("A",LifeCycle.BIENNIAL));
        plantList.add(new Plant("B",LifeCycle.PERENNIAL));
        plantList.add(new Plant("C",LifeCycle.ANNUAL));
        plantList.add(new Plant("D",LifeCycle.BIENNIAL));
        plantList.add(new Plant("E",LifeCycle.BIENNIAL));
        plantList.add(new Plant("F",LifeCycle.ANNUAL));
        plantList.add(new Plant("G",LifeCycle.BIENNIAL));

        return plantList;
    }
}
