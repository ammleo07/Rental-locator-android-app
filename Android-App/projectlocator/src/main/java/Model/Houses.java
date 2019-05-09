package Model;


import java.io.Serializable;
import java.util.List;

public class Houses implements Serializable{

    public List<House> getHouseList() {
        return houseList;
    }

    public void setHouseList(List<House> houseList) {
        this.houseList = houseList;
    }

    List<House> houseList;
}
