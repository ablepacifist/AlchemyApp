package alchemy.srsys.object;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {

    private final int id; // Unique identifier for the player
    private final String username; // Player's username
    private final String password; // Player's password
    private final IInventory inventory; // Player's unique inventory
    private final IKnowledgeBook knowledgeBook; // Player's unique knowledge book


    public Player(int id, String username, String password, IInventory inventory, IKnowledgeBook knowledgeBook) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.inventory = inventory;
        this.knowledgeBook = knowledgeBook;
    }


    public int getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public IInventory getInventory() {
        return inventory;
    }


    public IKnowledgeBook getKnowledgeBook() {
        return knowledgeBook;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", inventory=" + inventory +
                ", knowledgeBook=" + knowledgeBook +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return id == player.id &&
                Objects.equals(username, player.username) &&
                Objects.equals(password, player.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
