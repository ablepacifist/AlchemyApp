package alchemy.srsys.data;

/*
public class SQLiteJDBCDatabase implements IStubDatabase {

    private Connection connection; //sqlitejdbc

    static {
        try {
            System.loadLibrary("sqlitejdbc"); // Replace "sqliteX" with the actual library name without "lib" prefix
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    public SQLiteJDBCDatabase(String databasePath) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            createTables();
            System.out.println("Connection established successfully");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the SQLite database", e);
        }
    }

    // Close the connection when done
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create the necessary tables
    private void createTables() {
        String createEffectsTable = "CREATE TABLE IF NOT EXISTS effects (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT NOT NULL," +
                "description TEXT" +
                ");";

        String createIngredientsTable = "CREATE TABLE IF NOT EXISTS ingredients (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL" +
                ");";

        String createIngredientEffectsTable = "CREATE TABLE IF NOT EXISTS ingredient_effects (" +
                "ingredient_id INTEGER NOT NULL," +
                "effect_id INTEGER NOT NULL," +
                "PRIMARY KEY (ingredient_id, effect_id)," +
                "FOREIGN KEY(ingredient_id) REFERENCES ingredients(id)," +
                "FOREIGN KEY(effect_id) REFERENCES effects(id)" +
                ");";

        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "id INTEGER PRIMARY KEY," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL" +
                ");";

        String createInventoryTable = "CREATE TABLE IF NOT EXISTS inventory (" +
                "player_id INTEGER NOT NULL," +
                "ingredient_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "PRIMARY KEY (player_id, ingredient_id)," +
                "FOREIGN KEY(player_id) REFERENCES players(id)," +
                "FOREIGN KEY(ingredient_id) REFERENCES ingredients(id)" +
                ");";

        String createKnowledgeBookTable = "CREATE TABLE IF NOT EXISTS knowledge_book (" +
                "player_id INTEGER NOT NULL," +
                "ingredient_id INTEGER NOT NULL," +
                "effect_id INTEGER NOT NULL," +
                "PRIMARY KEY (player_id, ingredient_id, effect_id)," +
                "FOREIGN KEY(player_id) REFERENCES players(id)," +
                "FOREIGN KEY(ingredient_id) REFERENCES ingredients(id)," +
                "FOREIGN KEY(effect_id) REFERENCES effects(id)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createEffectsTable);
            stmt.execute(createIngredientsTable);
            stmt.execute(createIngredientEffectsTable);
            stmt.execute(createPlayersTable);
            stmt.execute(createInventoryTable);
            stmt.execute(createKnowledgeBookTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Initialize master effects
    private void initializeEffects() {
        // Insert default effects into the database
        addEffect(new Effect(1, "Healing"));
        addEffect(new Effect(2, "Poison"));
        addEffect(new Effect(3, "Strength"));
        addEffect(new Effect(4, "Weakness"));
        // Add more effects as needed
    }

    // Initialize master ingredients
    private void initializeIngredients() {
        // Example ingredients
        addIngredient(new Ingredient(1, "Red Herb", Arrays.asList(
                getEffectById(1), // Healing
                getEffectById(3)  // Strength
        )));
        addIngredient(new Ingredient(2, "Blue Mushroom", Arrays.asList(
                getEffectById(2), // Poison
                getEffectById(4)  // Weakness
        )));
        addIngredient(new Ingredient(3, "Yellow Flower", Arrays.asList(
                getEffectById(1), // Healing
                getEffectById(2)  // Poison
        )));
        // Add more ingredients as needed
    }

    // Initialize players
    private void initializePlayers() {
        // Create a player with username "alex" and password "zx7364pl" and ID 1
        Player player = new Player(1, "alex", "zx7364pl", getAllIngredients());

        // Add the player to the players table
        addPlayer(player);

        // Add inventory items to the player
        addIngredientToPlayerInventory(player.getId(), getIngredientByName("Red Herb"), 3);
        addIngredientToPlayerInventory(player.getId(), getIngredientByName("Blue Mushroom"), 1);
        addIngredientToPlayerInventory(player.getId(), getIngredientByName("Yellow Flower"), 4);

        // Update knowledge book for the player
        updateKnowledgeBook(player.getId(), getIngredientByName("Red Herb"));
        updateKnowledgeBook(player.getId(), getIngredientByName("Blue Mushroom"));
    }

    // Implementing methods from IStubDatabase

    // Effects management
    @Override
    public void addEffect(IEffect effect) {
        String sql = "INSERT OR IGNORE INTO effects (id, title, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, effect.getId());
            pstmt.setString(2, effect.getTitle());
            pstmt.setString(3, effect.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IEffect getEffectById(int id) {
        String sql = "SELECT * FROM effects WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Effect(
                        rs.getInt("id"),
                        rs.getString("title")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IEffect getEffectByTitle(String title) {
        String sql = "SELECT * FROM effects WHERE LOWER(title) = LOWER(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Effect(
                        rs.getInt("id"),
                        rs.getString("title")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<IEffect> getAllEffects() {
        List<IEffect> effects = new ArrayList<>();
        String sql = "SELECT * FROM effects";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                effects.add(new Effect(
                        rs.getInt("id"),
                        rs.getString("title")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return effects;
    }

    // Ingredients management
    @Override
    public void addIngredient(IIngredient ingredient) {
        String sql = "INSERT OR IGNORE INTO ingredients (id, name) VALUES (?, ?)";
        String insertEffectLink = "INSERT OR IGNORE INTO ingredient_effects (ingredient_id, effect_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             PreparedStatement effectLinkStmt = connection.prepareStatement(insertEffectLink)) {
            pstmt.setInt(1, ingredient.getId());
            pstmt.setString(2, ingredient.getName());
            pstmt.executeUpdate();

            // Link effects
            for (IEffect effect : ingredient.getEffects()) {
                if (effect != null) {
                    effectLinkStmt.setInt(1, ingredient.getId());
                    effectLinkStmt.setInt(2, effect.getId());
                    effectLinkStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IIngredient getIngredientById(int id) {
        String sql = "SELECT * FROM ingredients WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        getEffectsForIngredient(rs.getInt("id"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IIngredient getIngredientByName(String name) {
        String sql = "SELECT * FROM ingredients WHERE LOWER(name) = LOWER(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        getEffectsForIngredient(rs.getInt("id"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<IEffect> getEffectsForIngredient(int ingredientId) {
        List<IEffect> effects = new ArrayList<>();
        String sql = "SELECT e.id, e.title FROM effects e " +
                "INNER JOIN ingredient_effects ie ON e.id = ie.effect_id " +
                "WHERE ie.ingredient_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, ingredientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                effects.add(new Effect(
                        rs.getInt("id"),
                        rs.getString("title")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return effects;
    }

    @Override
    public List<IIngredient> getAllIngredients() {
        List<IIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        getEffectsForIngredient(rs.getInt("id"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    // Players management
    @Override
    public void addPlayer(Player player) {
        String sql = "INSERT OR IGNORE INTO players (id, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, player.getId());
            pstmt.setString(2, player.getUsername());
            pstmt.setString(3, player.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Player getPlayer(int playerId) {
        String sql = "SELECT * FROM players WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Player player = new Player(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        getAllIngredients()
                );
                // Load player's inventory
                player.setInventory(getPlayerInventory(player.getId()));
                // Load player's knowledge book
                player.setKnowledgeBook(new KnowledgeBook(getKnowledgeBook(player.getId())));
                return player;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Player getPlayerByUsername(String username) {
        String sql = "SELECT * FROM players WHERE LOWER(username) = LOWER(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Player player = new Player(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        getAllIngredients()
                );
                // Load player's inventory
                player.setInventory(getPlayerInventory(player.getId()));
                // Load player's knowledge book
                player.setKnowledgeBook(new KnowledgeBook(getKnowledgeBook(player.getId())));
                return player;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM players";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Player player = new Player(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        getAllIngredients()
                );
                // Load player's inventory
                player.setInventory(getPlayerInventory(player.getId()));
                // Load player's knowledge book
                player.setKnowledgeBook(new KnowledgeBook(getKnowledgeBook(player.getId())));
                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    // Inventory management for players
    public void addIngredientToPlayerInventory(int playerId, IIngredient ingredient, int quantity) {
        String sql = "INSERT INTO inventory (player_id, ingredient_id, quantity) VALUES (?, ?, ?) " +
                "ON CONFLICT(player_id, ingredient_id) DO UPDATE SET quantity = quantity + ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, ingredient.getId());
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeIngredientFromPlayerInventory(int playerId, IIngredient ingredient, int quantity) {
        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE player_id = ? AND ingredient_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, playerId);
            pstmt.setInt(3, ingredient.getId());
            pstmt.executeUpdate();

            // Remove the entry if quantity is zero or less
            String deleteSql = "DELETE FROM inventory WHERE player_id = ? AND ingredient_id = ? AND quantity <= 0";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, playerId);
                deleteStmt.setInt(2, ingredient.getId());
                deleteStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public IInventory getPlayerInventory(int playerId) {
        IInventory inventory = new Inventory();
        String sql = "SELECT ingredient_id, quantity FROM inventory WHERE player_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                IIngredient ingredient = getIngredientById(rs.getInt("ingredient_id"));
                int quantity = rs.getInt("quantity");
                inventory.addIngredient(ingredient, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventory;
    }

    // Knowledge Book management
    @Override
    public void updateKnowledgeBook(IIngredient ingredient) {
        // Updates knowledge book for all players
        String sql = "INSERT OR IGNORE INTO knowledge_book (player_id, ingredient_id, effect_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Player player : getAllPlayers()) {
                for (IEffect effect : ingredient.getEffects()) {
                    if (effect != null) {
                        pstmt.setInt(1, player.getId());
                        pstmt.setInt(2, ingredient.getId());
                        pstmt.setInt(3, effect.getId());
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Overloaded method to update knowledge book for a specific player
    public void updateKnowledgeBook(int playerId, IIngredient ingredient) {
        String sql = "INSERT OR IGNORE INTO knowledge_book (player_id, ingredient_id, effect_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (IEffect effect : ingredient.getEffects()) {
                if (effect != null) {
                    pstmt.setInt(1, playerId);
                    pstmt.setInt(2, ingredient.getId());
                    pstmt.setInt(3, effect.getId());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, IEffect[]> getKnowledgeBook() {
        // Retrieves knowledge book entries for all players
        Map<String, IEffect[]> knowledgeBook = new HashMap<>();
        String sql = "SELECT i.name AS ingredient_name, e.id AS effect_id, e.title AS effect_title " +
                "FROM knowledge_book kb " +
                "INNER JOIN ingredients i ON kb.ingredient_id = i.id " +
                "INNER JOIN effects e ON kb.effect_id = e.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String ingredientName = rs.getString("ingredient_name");
                IEffect effect = new Effect(rs.getInt("effect_id"), rs.getString("effect_title"));
                IEffect[] effects = knowledgeBook.getOrDefault(ingredientName, new IEffect[0]);
                effects = Arrays.copyOf(effects, effects.length + 1);
                effects[effects.length - 1] = effect;
                knowledgeBook.put(ingredientName, effects);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return knowledgeBook;
    }

    // Overloaded method to get knowledge book for a specific player
    public Map<Integer, List<IEffect>> getKnowledgeBook(int playerId) {
        Map<Integer, List<IEffect>> knowledgeBook = new HashMap<>();
        String sql = "SELECT kb.ingredient_id, e.id AS effect_id, e.title " +
                "FROM knowledge_book kb " +
                "INNER JOIN effects e ON kb.effect_id = e.id " +
                "WHERE kb.player_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int ingredientId = rs.getInt("ingredient_id");
                IEffect effect = new Effect(
                        rs.getInt("effect_id"),
                        rs.getString("title")
                );

                knowledgeBook.computeIfAbsent(ingredientId, k -> new ArrayList<>());
                List<IEffect> effects = knowledgeBook.get(ingredientId);
                effects.add(effect);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return knowledgeBook;
    }



    @Override
    public IIngredient findIngredientByName(String name) {
        return getIngredientByName(name);
    }

    @Override
    public IEffect findEffectById(int id) {
        return getEffectById(id);
    }

    // Inventory management for default inventory (global inventory)
    private List<IIngredient> inventory = new ArrayList<>();

    @Override
    public void addIngredientToInventory(IIngredient ingredient) {
        // Assuming this is a global inventory not tied to any player
        inventory.add(ingredient);
    }

    @Override
    public void removeIngredientFromInventory(IIngredient ingredient) {
        inventory.remove(ingredient);
    }

    @Override
    public List<IIngredient> getInventoryIngredients() {
        return new ArrayList<>(inventory);
    }

    // Loading data from files (if required)
    @Override
    public void loadIngredients(String filename) {
        // Implement file loading logic if needed
    }

    @Override
    public void loadEffects(String filename) {
        // Implement file loading logic if needed
    }

    // Potions management (if needed)
    public int getNextPotionId() {
        // Implement logic to get the next potion ID
        return 0;
    }

    // Additional utility methods
    public int getNextPlayerId() {
        String sql = "SELECT IFNULL(MAX(id), 0) + 1 AS next_id FROM players";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Default to 1 if no players exist
    }

}

 */
