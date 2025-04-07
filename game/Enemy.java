package game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy {
    public String name = "Spider";
    public int maxHP = 25;
    public int currentHP = maxHP;

    public BufferedImage sprite;

    public Enemy() {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/assets/enemies/spiders/spider01.png"));
        } catch (IOException e) {}
    }

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
