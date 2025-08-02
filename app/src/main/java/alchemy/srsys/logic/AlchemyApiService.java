package alchemy.srsys.logic;

import java.util.List;
import java.util.Map;

import alchemy.srsys.object.DTO.AuthResponse;
import alchemy.srsys.object.DTO.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;

import alchemy.srsys.object.Player;
import alchemy.srsys.object.DTO.InventoryResponse;
import alchemy.srsys.object.DTO.ForageResponse;
import alchemy.srsys.object.DTO.KnowledgeEntry;
import alchemy.srsys.object.DTO.BrewResponse;

public interface AlchemyApiService {

    // Player endpoints
    // LOGIN
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest body);

    @GET("api/player/{id}")
    Call<Player> getPlayerById(@Path("id") int playerId);

    @GET("api/player/username/{username}")
    Call<Player> getPlayerByUsername(@Path("username") String username);

    @GET("api/player/all")
    Call<List<Player>> getAllPlayers();

    // Inventory
    @GET("api/player/inventory/{playerId}")
    Call<InventoryResponse> getInventory(@Path("playerId") int playerId);

    // Foraging
    @GET("api/player/forage/{playerId}")
    Call<ForageResponse> forage(@Path("playerId") int playerId);

    // Consume an ingredient
    // payload: { "playerId": <int>, "ingredientId": <int> }
    @POST("api/player/ingredient/consume")
    Call<Void> consumeIngredient(@Body Map<String, Integer> payload);

    // Consume a potion
    // payload: { "playerId": <int>, "potionId": <int> }
    @POST("api/player/potion/consume")
    Call<Void> consumePotion(@Body Map<String, Integer> payload);

    // Level up player
    // payload: { "playerId": <int>, "secretPassword": <String> }
    @POST("api/player/levelup")
    Call<Player> levelUpPlayer(@Body Map<String, Object> payload);

    // Knowledge Book
    @GET("api/player/knowledge/{playerId}")
    Call<List<KnowledgeEntry>> getKnowledgeBook(@Path("playerId") int playerId);

    // Brew Potion
    // payload: { "playerId": <int>, "ingredientId1": <int>, "ingredientId2": <int> }
    @POST("api/potion/brew")
    Call<BrewResponse> brewPotion(@Body Map<String, Object> payload);
}
