import java.awt.Color;

class Mage extends GameCharacter {
    public Mage(String name, int level) {
        super(name, level);
        initHp(70 + level * 5);
    }
    @Override public String attack()      { return getName() + " casts a fireball!"; }
    @Override public String specialMove() { return getName() + " uses Lightning Storm!"; }
    @Override public int attackDamage()   { return 12 + getLevel() * 4; }
    @Override public int specialDamage()  { return attackDamage() * 2; }
    @Override public String getImageFile()   { return "mage.png"; }
    @Override public String getType()        { return "Mage"; }
    @Override public String getSpecialName() { return "Lightning Storm"; }
    @Override public Color getColor()        { return new Color(130, 100, 240); }
}