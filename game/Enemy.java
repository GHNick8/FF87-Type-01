package game;

public class Enemy {
    String name = "Slime";
    int maxHP = 20;
    int currentHP = maxHP;

    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) currentHP = 0;
    }

    public boolean isDefeated() {
        return currentHP <= 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
