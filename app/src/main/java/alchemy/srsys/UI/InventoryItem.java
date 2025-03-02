package alchemy.srsys.UI;

import java.io.Serializable;

public class InventoryItem implements Serializable {
    public String name;
    public String type;
    public int quantity;

    public InventoryItem(String name, String type, int quantity) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }
}
