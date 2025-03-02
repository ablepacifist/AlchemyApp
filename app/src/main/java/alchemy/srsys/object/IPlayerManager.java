package alchemy.srsys.object;

import alchemy.srsys.object.*;
import java.util.List;
import java.util.Map;

public interface IPlayerManager {
    public Player loginPlayer(String username, String password);
    public boolean registerPlayer(String username, String password);
}
