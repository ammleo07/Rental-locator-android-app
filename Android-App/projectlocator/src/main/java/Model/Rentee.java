package Model;


import java.io.Serializable;

/**
 * Created by alber on 4/14/19.
 */
public class Rentee implements Serializable {

    int id;
    int userId;
    String username;
    String contactNumber;
    String houseType;
    double minPriceRange;
    double maxPriceRange;
    String renteeType;
    String fundType;

    public String getRenteeType() {
        return renteeType;
    }

    public void setRenteeType(String renteeType) {
        this.renteeType = renteeType;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public double getMinPriceRange() {
        return minPriceRange;
    }

    public void setMinPriceRange(double minPriceRange) {
        this.minPriceRange = minPriceRange;
    }

    public double getMaxPriceRange() {
        return maxPriceRange;
    }

    public void setMaxPriceRange(double maxPriceRange) {
        this.maxPriceRange = maxPriceRange;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
