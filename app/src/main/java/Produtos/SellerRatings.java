package Produtos;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class SellerRatings {
    private double rating;
    private String rantingType;

    public SellerRatings(double rating, String rantingType) {
        this.rating = rating;
        this.rantingType = rantingType;
    }

    public double getRating() {
        return rating;
    }

    public String getRantingType() {
        return rantingType;
    }
}
