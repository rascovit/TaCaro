package Produtos;

import java.io.Serializable;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class Offer implements Serializable {
    private Seller seller;
    private int productId;
    private double fullPrice;
    private int amountOfParcels;
    private double parcelValue;
    private String offerName;
    private ThumbNail productThumbnail;
    public Offer(Seller seller, int productId, double fullPrice, int amountOfParcels, double parcelValue, String offerName, ThumbNail productThumbnail) {
        this.seller = seller;
        this.productId = productId;
        this.fullPrice = fullPrice;
        this.amountOfParcels = amountOfParcels;
        this.parcelValue = parcelValue;
        this.offerName = offerName;
        this.productThumbnail = productThumbnail;
    }


    public Seller getSeller() {
        return seller;
    }

    public int getProductId() {
        return productId;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public int getAmountOfParcels() {
        return amountOfParcels;
    }

    public double getParcelValue() {
        return parcelValue;
    }

    public String getOfferName() {
        return offerName;
    }

    public ThumbNail getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(ThumbNail productThumbnail) {
        this.productThumbnail = productThumbnail;
    }
}
