package Produtos;

import java.io.Serializable;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class ProductRatings implements Serializable {
    private double rating;

    public ProductRatings(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }
}
