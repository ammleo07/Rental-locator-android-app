package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alber on 29/11/2018.
 */

public class Transaction extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRentee() {
        return rentee;
    }

    public void setRentee(String rentee) {
        this.rentee = rentee;
    }

    public String getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(String houseOwner) {
        this.houseOwner = houseOwner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("rentee")
    @Expose
    String rentee;

    public String getRenteeContactNumber() {
        return renteeContactNumber;
    }

    public void setRenteeContactNumber(String renteeContactNumber) {
        this.renteeContactNumber = renteeContactNumber;
    }

    @SerializedName("renteeContactNumber")
    @Expose
    String renteeContactNumber;


    @SerializedName("houseOwner")
    @Expose
    String houseOwner;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("renteeTokenId")
    @Expose
    String renteeTokenId;

    public String getRenteeTokenId() {
        return renteeTokenId;
    }

    public void setRenteeTokenId(String renteeTokenId) {
        this.renteeTokenId = renteeTokenId;
    }

    public String getOwnerTokenId() {
        return ownerTokenId;
    }

    public void setOwnerTokenId(String ownerTokenId) {
        this.ownerTokenId = ownerTokenId;
    }

    @SerializedName("ownerTokenId")
    @Expose
    String ownerTokenId;



}
