package pokemonHeroes;

public class Move {

    private String name, type;
    private int damage;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public Move(String name, String type, int damage) {
        this.name = name;
        this.type = type;
        this.damage = damage;
    }
}
