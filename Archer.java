import java.awt.Color;
import java.util.Random;

class Archer extends GameCharacter {
    private static final Random RNG = new Random();
    public Archer(String name, int level) {
        super(name, level);
        initHp(90 + level * 7);
    }
    @Override public String attack()      { return getName() + " gi sight, gi pangita, bang IGO! "; }
    @Override public String specialMove() { return getName() + " pitipiwiwiwiwiw arrow "; }
    @Override public int attackDamage() {
        int base = 9 + getLevel() * 3;
        return RNG.nextInt(100) < 25 ? base * 2 : base;
    }
    @Override public int specialDamage()  { return (9 + getLevel() * 3) * 3 * 7 / 10; }
    @Override public String getImageFile()   { return "archer.png"; }
    @Override public String getType()        { return "Archer"; }
    @Override public String getSpecialName() { return "pitipiwiwiwiwiw arrow"; }
    @Override public Color getColor()        { return new Color(70, 190, 110); }
}