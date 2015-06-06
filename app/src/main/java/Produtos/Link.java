package Produtos;

import java.io.Serializable;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class Link implements Serializable {

    private String productUrl;
    private String productJsonUrl;

    public Link(String productUrl, String productJsonUrl) {
        this.productUrl = productUrl;
        this.productJsonUrl = productJsonUrl.substring(0,productJsonUrl.indexOf("?") + 1) + "format=json&" + productJsonUrl.substring(productJsonUrl.indexOf("?") + 1);
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductJsonUrl() {
        return productJsonUrl;
    }
}
