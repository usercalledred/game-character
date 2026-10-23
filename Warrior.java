import java.awt.Color;

class Warrior extends GameCharacter {
    public Warrior(String name, int level) {
        super(name, level);
        initHp(120 + level * 10);
    }
    @Override public String attack()      { return getName() + " swings a mighty sword!"; }
    @Override public String specialMove() { return getName() + " uses Shield Bash!"; }
    @Override public int attackDamage()   { return 10 + getLevel() * 3; }
    @Override public int specialDamage()  { return (int) (attackDamage() * 1.5); }
    @Override public String getImageFile()   { return "warrior.png"; }
    @Override public String getType()        { return "Warrior"; }
    @Override public String getSpecialName() { return "Shield Bash"; }
    @Override public Color getColor()        { return new Color(220, 80, 70); }

    @Override public String afterSpecial() {
        int h = maxHp / 10;
        heal(h);
        return getName() + " braces behind the shield and recovers " + h + " HP.";
    }
}
