package alchemy.srsys.object;

public class Effect implements IEffect {
    private int id;
    private String title;
    private String description;

    public Effect(int id, String title) {
        this.id = id;
        this.title = title;
        this.description = ""; // Description can be populated later
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
