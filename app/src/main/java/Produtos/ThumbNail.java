package Produtos;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class ThumbNail {
    private int width;
    private int height;
    private String url;

    public ThumbNail(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }
}
