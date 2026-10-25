import java.awt.Color;

class Warrior extends GameCharacter {
    public Warrior(String name, int level) {
        super(name, level);
        initHp(120 + level * 10);
    }
    @Override public String attack()      { return getName() + " tigbason taka ron! uhm! "; }
    @Override public String specialMove() { return getName() + " dapalon taka "; }
    @Override public int attackDamage()   { return 10 + getLevel() * 3; }
    @Override public int specialDamage()  { return (int) (attackDamage() * 1.5); }
    @Override public String getImageFile()   { return "warrior.png"; }
    @Override public String getType()        { return "Warrior"; }
    @Override public String getSpecialName() { return "Dapalon Attack"; }
    @Override public Color getColor()        { return new Color(220, 80, 70); }

    @Override public String afterSpecial() {
        int h = maxHp / 10;
        heal(h);
        return getName() + " nireject ang confession kaya kumunat by " + h + " HP.";
    }
}