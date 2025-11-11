package gr1.fpt.bambikitchen.model.enums;

public enum Unit {
    GRAM("g"),
    KILOGRAM("kg"),
    LITER("l"),
    PCS("pcs");

    Unit(String name){
    }

    public String getName() {
        return name();
    }

}
