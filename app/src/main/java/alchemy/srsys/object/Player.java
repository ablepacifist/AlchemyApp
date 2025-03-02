package alchemy.srsys.object;

import java.util.List;

import alchemy.srsys.logic.KnowledgeBook;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.logic.Inventory;

import java.util.List;

public class Player {
    private int id;
    private String username;
    private String password; // In a real app, passwords should be hashed.
    private IInventory inventory;
    private IKnowledgeBook knowledgeBook;

    public Player(int id, String username, String password, List<IIngredient> allIngredients) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.inventory = new Inventory();
        this.knowledgeBook = new KnowledgeBook(allIngredients);
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public IInventory getInventory() { return inventory; }
    public IKnowledgeBook getKnowledgeBook() { return knowledgeBook; }
}

