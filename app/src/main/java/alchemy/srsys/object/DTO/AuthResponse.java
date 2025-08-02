package alchemy.srsys.object.DTO;

public class AuthResponse {
    private int playerId;
    // you can add csrfToken, jwtToken, etc. here if your API returns them

    public int getPlayerId() {
        return playerId;
    }
}

