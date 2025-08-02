package alchemy.srsys.object.DTO;

import java.io.Serializable;

public class ForageResponse implements Serializable {
    private String forage;

    public String getForage() { return forage; }
    public void setForage(String forage) { this.forage = forage; }
}
