package Model;

import java.io.Serializable;

/**
 * Created by alber on 17/04/2019.
 */

public class RenteeForm implements Serializable {

    User user;
    Rentee rentee;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rentee getRentee() {
        return rentee;
    }

    public void setRentee(Rentee rentee) {
        this.rentee = rentee;
    }


}
