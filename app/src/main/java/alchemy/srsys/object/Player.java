package alchemy.srsys.object;

import alchemy.srsys.logic.KnowledgeBook;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.logic.Inventory;
public class Player {
    private IInventory inventory;
    private IKnowledgeBook knowledgeBook;

    public Player(IStubDatabase database) {
        this.inventory = new Inventory(database,this);
        this.knowledgeBook = new KnowledgeBook(database);
    }

    public IInventory getInventory() {
        return inventory;
    }

    public IKnowledgeBook getKnowledgeBook() {
        return knowledgeBook;
    }
}
