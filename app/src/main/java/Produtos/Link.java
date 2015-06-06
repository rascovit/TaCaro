package Produtos;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class Link {

    private String productUrl;
    private String productJsonUrl;

    public Link(String productUrl, String productJsonUrl) {
        this.productUrl = productUrl;
        this.productJsonUrl = productJsonUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductJsonUrl() {
        return productJsonUrl;
    }
}
