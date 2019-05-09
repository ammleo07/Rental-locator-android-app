package Model;

import java.io.Serializable;

/**
 * Created by alber on 17/04/2019.
 */

public class HouseOwnerForm implements Serializable {

    User user;


    public HouseOwner getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(HouseOwner houseOwner) {
        this.houseOwner = houseOwner;
    }

    HouseOwner houseOwner;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    House house;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    Address address;


}
