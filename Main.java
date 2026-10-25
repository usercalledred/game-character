public class Main {
    public static void main(String[] args) {
        GameCharacter[] starters = {
            new Warrior("Brader", 5),
            new Mage("GabMids", 5),
            new Archer("Sylva", 4)
        };
        GameWindow.launch(starters);
    }
}