package Produtos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class BuscapeProduct implements Serializable{
    private String fullProductName;
    private double maxProductPrice;
    private double minProductPrice;
    private int quantity;
    private int amountOfOffers;
    private int amountOfSellers;
    private int productId;
    private int productCategoryId;

    private ArrayList<Thumbnail> thumbNails;
    private ArrayList<Specification> productSpecification;
    private boolean haveSpefication;
    private ProductRatings productRatings;
    private int ratingAmount;
    Link productLink;

    private ArrayList<Offer> productOffers;

    public BuscapeProduct(String fullProductName, double maxProductPrice, double minProductPrice, int quantity, int amountOfOffers, int amountOfSellers, int productId, int productCategoryId, int ratingAmount) {
        this.fullProductName = fullProductName;
        this.maxProductPrice = maxProductPrice;
        this.minProductPrice = minProductPrice;
        this.quantity = quantity;
        this.amountOfOffers = amountOfOffers;
        this.amountOfSellers = amountOfSellers;
        this.productId = productId;
        this.productCategoryId = productCategoryId;
        this.thumbNails = new ArrayList<>();
        this.productSpecification = new ArrayList<>();
        this.productOffers = new ArrayList<>();
        this.haveSpefication = false;
        this.productOffers = new ArrayList<>();
        this.ratingAmount = ratingAmount;
    }

    public void setOffer(Offer offer){
        productOffers.add(offer);
    }

    public void setProductLink(Link productLink) {
        this.productLink = productLink;
    }

    public void setThumbNail(Thumbnail thumbNail) {
        this.thumbNails.add(thumbNail);
    }
    public void setSpecification(Specification specification) {
        this.productSpecification.add(specification);
        this.haveSpefication = true;
    }

    public void setProductRatings(ProductRatings productRatings){
        this.productRatings = productRatings;
    }

    public boolean haveSpefication(){
        return haveSpefication;
    }

    public String getFullProductName() {
        return fullProductName;
    }

    public double getMaxProductPrice() {
        return maxProductPrice;
    }

    public double getMinProductPrice() {
        return minProductPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmountOfOffers() {
        return amountOfOffers;
    }

    public int getAmountOfSellers() {
        return amountOfSellers;
    }

    public int getProductId() {
        return productId;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public ArrayList<Thumbnail> getThumbNails() {
        return thumbNails;
    }

    public ArrayList<Specification> getProductSpecification() {
        return productSpecification;
    }

    public ProductRatings getProductRatings() {
        return productRatings;
    }

    public ArrayList<Offer> getProductOffers() {
        return productOffers;
    }

    public Link getProductLink() {
        return productLink;
    }

    public int getRatingAmount() {
        return ratingAmount;
    }
}
