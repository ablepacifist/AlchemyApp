package alchemy.srsys.tests.data;

import org.junit.*;
import static org.junit.Assert.*;

import java.sql.*;
import java.util.*;
import java.lang.reflect.Field;

import alchemy.srsys.data.HSQLDatabase;
import alchemy.srsys.object.Effect;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.Ingredient;
import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.KnowledgeBook;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.Potion;

public class HSQLDatabaseTest {

    private HSQLDatabase db;
    private String dbPath;

    // Helper method to access the private "connection" field via reflection.
    private Connection getConnection() throws Exception {
        Field field = HSQLDatabase.class.getDeclaredField("connection");
        field.setAccessible(true);
        return (Connection) field.get(db);
    }

    @Before
    public void setUp() throws Exception {
        // Create a unique temporary database name.
        dbPath = "testdb_" + System.currentTimeMillis();
        db = new HSQLDatabase(dbPath);

        // Create additional tables used in tests.
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        // Table for tracking player's ingredient inventory.
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS player_inventory ("
                + "player_id INTEGER NOT NULL, "
                + "ingredient_id INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "PRIMARY KEY (player_id, ingredient_id))");
        // Table for player's potion inventory.
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS player_potions ("
                + "player_id INTEGER NOT NULL, "
                + "potion_id INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "PRIMARY KEY (player_id, potion_id))");
        // Table for mapping potions to effects (if needed).
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS potion_effects ("
                + "potion_id INTEGER NOT NULL, "
                + "effect_id INTEGER NOT NULL, "
                + "PRIMARY KEY (potion_id, effect_id))");
        stmt.close();
    }

    @After
    public void tearDown() {
        if (db != null) {
            db.close();
        }
    }

    // 1. getNextPotionId()
    @Test
    public void testGetNextPotionId() {
        int nextPotionId = db.getNextPotionId();
        assertEquals("Next potion id should be 1 when none exists", 1, nextPotionId);

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO potions (id, name) VALUES (?, ?)");
            pstmt.setInt(1, 1);
            pstmt.setString(2, "Test Potion");
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            fail("Exception during manual potion insert: " + e.getMessage());
        }

        int nextAfter = db.getNextPotionId();
        assertEquals("After inserting one potion, next potion id should be 2", 2, nextAfter);
    }

    // 2. getNextPlayerId()
    @Test
    public void testGetNextPlayerId() {
        int nextPlayerId = db.getNextPlayerId();
        assertEquals("Next player id should be 1 when table is empty", 1, nextPlayerId);

        Player player = new Player(nextPlayerId, "user1", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        int nextAfter = db.getNextPlayerId();
        assertEquals("After adding one player, next player id should be 2", 2, nextAfter);
    }

    // 3. getIngredientByName(String name)
    @Test
    public void testGetIngredientByName() {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ingredients (NAME) VALUES (?)");
            pstmt.setString(1, "Herb");
            pstmt.executeUpdate();
            pstmt.close();
        } catch(Exception e) {
            fail("Exception inserting ingredient: " + e.getMessage());
        }
        IIngredient ing = db.getIngredientByName("Herb");
        assertNotNull("Ingredient should be found", ing);
        assertEquals("Ingredient name should be 'Herb'", "Herb", ing.getName());
    }

    // 4. getAllIngredients()
    @Test
    public void testGetAllIngredients() {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ingredients (NAME) VALUES (?)");
            pstmt.setString(1, "Herb");
            pstmt.executeUpdate();
            pstmt.setString(1, "Mushroom");
            pstmt.executeUpdate();
            pstmt.close();
        } catch(Exception e) {
            fail("Exception inserting ingredients: " + e.getMessage());
        }
        List<IIngredient> ingredients = db.getAllIngredients();
        assertTrue("Ingredients list should contain 'Herb'",
                ingredients.stream().anyMatch(ing -> ing.getName().equalsIgnoreCase("Herb")));
        assertTrue("Ingredients list should contain 'Mushroom'",
                ingredients.stream().anyMatch(ing -> ing.getName().equalsIgnoreCase("Mushroom")));
    }

    // 5. addPlayer(Player player)
    @Test
    public void testAddPlayer() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "playerTest", "pass123", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        Player fetched = db.getPlayer(playerId);
        assertNotNull("Added player should be retrieved", fetched);
        assertEquals("Username should match", "playerTest", fetched.getUsername());
    }

    // 6. getPlayer(int playerId)
    @Test
    public void testGetPlayer() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "playerGet", "secret", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        Player fetched = db.getPlayer(playerId);
        assertNotNull("Fetched player should not be null", fetched);
        assertEquals("Fetched player's username should be 'playerGet'", "playerGet", fetched.getUsername());
    }

    // 7. getPlayerByUsername(String username)
    @Test
    public void testGetPlayerByUsername() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "uniqueUser", "mypassword", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        Player fetched = db.getPlayerByUsername("uniqueUser");
        assertNotNull("Fetched player by username should not be null", fetched);
        assertEquals("Fetched player's id should match", playerId, fetched.getId());
    }

    // 8. getAllPlayers()
    @Test
    public void testGetAllPlayers() {
        int initialSize = db.getAllPlayers().size();
        int id1 = db.getNextPlayerId();
        Player p1 = new Player(id1, "p1", "a", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(p1);
        int id2 = db.getNextPlayerId();
        Player p2 = new Player(id2, "p2", "b", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(p2);

        Collection<Player> players = db.getAllPlayers();
        assertEquals("All players size should be initialSize+2", initialSize + 2, players.size());
    }

    // 9. addPotionToPlayerInventory(int playerId, Potion potion, int quantity)
    @Test
    public void testAddPotionToPlayerInventory() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "potionUser", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        // Create a test potion.
        List<IEffect> effects = new ArrayList<>();
        effects.add(new Effect(101, "Heal", "Restores health"));
        IIngredient ingr1 = new Ingredient(501, "Red Herb", new ArrayList<IEffect>());
        IIngredient ingr2 = new Ingredient(502, "Blue Herb", new ArrayList<IEffect>());
        Potion potion = new Potion(db.getNextPotionId(), "Healing Potion", effects, ingr1, ingr2);

        db.addPotionToPlayerInventory(playerId, potion, 2);

        // Verify by directly querying the player_potions table.
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT quantity FROM player_potions WHERE player_id = ? AND potion_id = ?");
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, potion.getId());
            ResultSet rs = pstmt.executeQuery();
            int quantity = 0;
            if(rs.next()){
                quantity = rs.getInt("quantity");
            }
            rs.close();
            pstmt.close();
            assertEquals("Quantity should be 2", 2, quantity);
        } catch(Exception e) {
            fail("Exception verifying potion inventory: " + e.getMessage());
        }
    }

    // 10. addIngredientToPlayerInventory(int playerId, IIngredient ingredient, int quantity)
    @Test
    public void testAddIngredientToPlayerInventory() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "ingredientUser", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        // Use a seeded or valid ingredient
        IIngredient ingredient = new Ingredient(1, "Alkanet Flower", new ArrayList<IEffect>()); // ID 1, pre-seeded

        // Add the ingredient to the player's inventory
        db.addIngredientToPlayerInventory(playerId, ingredient, 5);

        // Verify by directly querying the inventory table
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT quantity FROM inventory WHERE player_id = ? AND ingredient_id = ?");
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, ingredient.getId());
            ResultSet rs = pstmt.executeQuery();
            int quantity = 0;
            if (rs.next()) {
                quantity = rs.getInt("quantity");
            }
            rs.close();
            pstmt.close();
            assertEquals("Quantity should be 5", 5, quantity); // Check the correct quantity
        } catch (Exception e) {
            fail("Exception verifying ingredient inventory: " + e.getMessage());
        }
    }


    // 11. removeIngredientFromPlayerInventory(int playerId, IIngredient ingredient, int quantity)
    @Test
    public void testRemoveIngredientFromPlayerInventory() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "removeIngredientUser", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        // Use a valid, seeded ingredient ID
        IIngredient ingredient = new Ingredient(1, "Alkanet Flower", new ArrayList<IEffect>()); // ID 1 is pre-seeded

        try {
            // Add the ingredient to the inventory
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO inventory (player_id, ingredient_id, quantity) VALUES (?, ?, ?)");
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, ingredient.getId());
            pstmt.setInt(3, 10);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            fail("Exception inserting ingredient into inventory: " + e.getMessage());
        }

        // Remove part of the ingredient quantity
        db.removeIngredientFromPlayerInventory(playerId, ingredient, 4);

        try {
            // Verify the updated quantity in the inventory
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT quantity FROM inventory WHERE player_id = ? AND ingredient_id = ?");
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, ingredient.getId());
            ResultSet rs = pstmt.executeQuery();
            int quantity = 0;
            if (rs.next()) {
                quantity = rs.getInt("quantity");
            }
            rs.close();
            pstmt.close();

            // Assert the remaining quantity is 6
            assertEquals("Quantity should be 6 after removal", 6, quantity);
        } catch (Exception e) {
            fail("Exception verifying removal: " + e.getMessage());
        }
    }


    // 12. getPlayerInventory(int playerId)
    @Test
    public void testGetPlayerInventory() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "inventoryTestUser", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        // Add the ingredient to the ingredients table (it must exist for foreign key constraints).
        IIngredient ingredient = new Ingredient(801, "Mystic Flower", new ArrayList<IEffect>());
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ingredients (ID, NAME) VALUES (?, ?)");
            pstmt.setInt(1, ingredient.getId());
            pstmt.setString(2, ingredient.getName());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            fail("Exception inserting ingredient into master list: " + e.getMessage());
        }

        // Add the ingredient to the player's inventory.
        db.addIngredientToPlayerInventory(playerId, ingredient, 3);

        // Fetch the player's inventory and check its contents.
        IInventory inventory = db.getPlayerInventory(playerId);
        assertTrue("Inventory should contain the ingredient", inventory.containsIngredient(ingredient));
    }


    // 13. addKnowledgeEntry(int playerId, IIngredient ingredient, IEffect effect)
    @Test
    public void testAddKnowledgeEntry() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "knowledgeUser", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        // Use existing ingredient and effect IDs from the seeded data
        IIngredient ingredient = new Ingredient(1, "Alkanet Flower", new ArrayList<IEffect>()); // ID 1 matches seeded data
        IEffect effect = new Effect(5, "Dream", ""); // ID 5 matches seeded data

        db.addKnowledgeEntry(playerId, ingredient, effect);

        Map<Integer, List<IEffect>> kb = db.getKnowledgeBook(playerId);
        assertTrue("Knowledge book should contain entry for ingredient", kb.containsKey(ingredient.getId()));
        List<IEffect> effects = kb.get(ingredient.getId());
        assertNotNull("Effects list should not be null", effects);
        assertEquals("Effects list size should be 1", 1, effects.size());
        assertEquals("Effect title should be 'Dream'", "Dream", effects.get(0).getTitle());
    }


    // 14. updateKnowledgeBook(int playerId, IIngredient ingredient)
    @Test
    public void testUpdateKnowledgeBook() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "updateKnowledgeUser", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        // Use existing data from the seeded database
        IEffect effect1 = new Effect(1, "Paralyze", ""); // ID 1 matches seeded data
        IEffect effect2 = new Effect(2, "Light", "");    // ID 2 matches seeded data
        List<IEffect> listEffects = new ArrayList<>();
        listEffects.add(effect1);
        listEffects.add(effect2);
        IIngredient ingredient = new Ingredient(1, "Alkanet Flower", listEffects); // ID 1 matches seeded data

        // Verify the knowledge book does not contain the ingredient initially
        Map<Integer, List<IEffect>> kbInitial = db.getKnowledgeBook(playerId);
        assertFalse("Knowledge book should not contain the ingredient initially", kbInitial.containsKey(ingredient.getId()));

        // Update the knowledge book with the ingredient and effects
        db.updateKnowledgeBook(playerId, ingredient);

        // Verify the knowledge book now contains the ingredient
        Map<Integer, List<IEffect>> kbUpdated = db.getKnowledgeBook(playerId);
        assertTrue("Knowledge book should now contain the ingredient", kbUpdated.containsKey(ingredient.getId()));
        List<IEffect> learnedEffects = kbUpdated.get(ingredient.getId());
        assertEquals("Should have learned 2 effects", 2, learnedEffects.size());

        // Re-updating should not duplicate effects
        db.updateKnowledgeBook(playerId, ingredient);
        Map<Integer, List<IEffect>> kbUpdated2 = db.getKnowledgeBook(playerId);
        List<IEffect> learnedEffects2 = kbUpdated2.get(ingredient.getId());
        assertEquals("Duplicate entries should not be added", 2, learnedEffects2.size());
    }


    // 15. getKnowledgeBook(int playerId)
    @Test
    public void testGetKnowledgeBook() {
        int playerId = db.getNextPlayerId();
        Player player = new Player(playerId, "viewKnowledgeUser", "pass", new Inventory(), new KnowledgeBook(new HashMap<Integer, List<IEffect>>()));
        db.addPlayer(player);

        Map<Integer, List<IEffect>> kb = db.getKnowledgeBook(playerId);
        assertTrue("Knowledge book should be empty initially", kb.isEmpty());
    }

    // 16. getEffectsForIngredient(int ingredientId)
    @Test
    public void testGetEffectsForIngredient() {
        int ingredientId = 1101;
        try {
            Connection conn = getConnection();
            // Insert ingredient.
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ingredients (ID, NAME) VALUES (?, ?)");
            pstmt.setInt(1, ingredientId);
            pstmt.setString(2, "Test Ingredient");
            pstmt.executeUpdate();
            pstmt.close();
            // Insert an effect.
            pstmt = conn.prepareStatement("INSERT INTO effects (ID, TITLE, DESCRIPTION) VALUES (?, ?, ?)");
            pstmt.setInt(1, 501);
            pstmt.setString(2, "Test Effect");
            pstmt.setString(3, "Effect Description");
            pstmt.executeUpdate();
            pstmt.close();
            // Map the effect to the ingredient.
            pstmt = conn.prepareStatement("INSERT INTO ingredient_effects (ingredient_id, effect_id) VALUES (?, ?)");
            pstmt.setInt(1, ingredientId);
            pstmt.setInt(2, 501);
            pstmt.executeUpdate();
            pstmt.close();
        } catch(Exception e) {
            fail("Exception setting up ingredient effects: " + e.getMessage());
        }

        List<IEffect> effects = db.getEffectsForIngredient(ingredientId);
        assertNotNull("Effects list should not be null", effects);
        assertEquals("Should have exactly 1 effect", 1, effects.size());
        assertEquals("Effect title should be 'Test Effect'", "Test Effect", effects.get(0).getTitle());
    }

    // 17. Test Inventory.containsIngredient (as an extra test to confirm the utility method)
    @Test
    public void testInventoryContainsIngredient() {
        Inventory inv = new Inventory();
        IIngredient ingredient = new Ingredient(1201, "Special Herb", new ArrayList<IEffect>());
        inv.addIngredient(ingredient, 3);

        assertTrue("Inventory should contain the ingredient", inv.containsIngredient(ingredient));
    }
}

