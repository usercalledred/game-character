import java.awt.Color;


abstract class GameCharacter {
    private String name;
    private int level;
    protected int maxHp;
    protected int hp;

    public GameCharacter(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public abstract String attack();
    public abstract String specialMove();
    public abstract int attackDamage();
    public abstract int specialDamage();
    public abstract String getType();
    public abstract String getSpecialName();
    public abstract Color getColor();
    public abstract String getImageFile();


    public String afterSpecial() { return ""; }

    
    protected void initHp(int hpValue) { this.maxHp = hpValue; this.hp = hpValue; }

    public void takeDamage(int dmg) { hp = Math.max(0, hp - dmg); }
    public void heal(int amount)   { hp = Math.min(maxHp, hp + amount); }
    public void fullHeal()         { hp = maxHp; }
    public boolean isDefeated()    { return hp <= 0; }

    public String getName() { return name; }
    public int getLevel()   { return level; }
    public int getHp()      { return hp; }
    public int getMaxHp()   { return maxHp; }

    public String showName() { return "Character: " + name + " (" + getType() + ", Lv " + level + ")"; }
}
