package service;

import java.util.ArrayList;
import java.util.List;


public class BarringService {

    private List<String> barrings = new ArrayList();

    public BarringService() {
        barrings.add("Llamadas nacionales");
        barrings.add("Roaming");
    }

    public List findAll() {
        return barrings;
    }

    public String findByBarringname(String username) {
        return barrings.stream()
                .filter(b -> b.equals(username))
                .findFirst()
                .get();
    }

    public void create(String barring) {
        barrings.add(barring);
    }
}
