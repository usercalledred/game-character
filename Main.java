public class Main {
    public static void main(String[] args) {
        GameCharacter[] starters = {
            new Warrior("Martin", 5),
            new Mage("Ren", 5),
            new Archer("Lui", 4)
        };
        GameWindow.launch(starters);
    }
}