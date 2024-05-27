package tmp.quarkus.entities;

import java.util.List;
import java.util.ArrayList;

public class Hero {
    public String name;
    public String surname;
    public Double height;
    public Integer mass;
    public Boolean darkSide;
    public LightSaber lightSaber;
    public List<Integer> episodeIds = new ArrayList<>();

}
