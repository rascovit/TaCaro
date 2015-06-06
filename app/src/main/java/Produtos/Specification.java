package Produtos;

/**
 * Created by PC MASTER RACE on 05/06/2015.
 */
public class Specification {

    private String label;
    private String value;

    public Specification(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
