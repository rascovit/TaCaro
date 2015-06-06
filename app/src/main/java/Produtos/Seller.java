package Produtos;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class Seller {
    private String sellerName;
    private int sellerId;
    private ThumbNail sellerThumbNail;
    private SellerRatings sellerRatings;
    private String sellerWebSiteUrl;

    public Seller(String sellerName, int sellerId, ThumbNail sellerThumbNail, SellerRatings sellerRatings, String sellerWebSiteUrl) {
        this.sellerName = sellerName;
        this.sellerId = sellerId;
        this.sellerThumbNail = sellerThumbNail;
        this.sellerRatings = sellerRatings;
        this.sellerWebSiteUrl = sellerWebSiteUrl;
    }

    public String getSellerName() {
        return sellerName;
    }

    public int getSellerId() {
        return sellerId;
    }

    public ThumbNail getSellerThumbNail() {
        return sellerThumbNail;
    }

    public SellerRatings getSellerRatings() {
        return sellerRatings;
    }

    public String getSellerWebSiteUrl() {
        return sellerWebSiteUrl;
    }
}
