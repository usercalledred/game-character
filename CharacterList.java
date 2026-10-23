import java.util.ArrayList;
import java.util.List;


class CharacterList {
    private static class Node {
        GameCharacter data;
        Node next;
        Node(GameCharacter data) { this.data = data; }
    }

    private Node head;
    private int size;

    public void add(GameCharacter c) {
        Node n = new Node(c);
        if (head == null) head = n;
        else { Node cur = head; while (cur.next != null) cur = cur.next; cur.next = n; }
        size++;
    }

    public GameCharacter findByName(String name) {
        for (Node cur = head; cur != null; cur = cur.next)
            if (cur.data.getName().equalsIgnoreCase(name)) return cur.data;
        return null;
    }

    public GameCharacter removeByName(String name) {
        Node prev = null;
        for (Node cur = head; cur != null; prev = cur, cur = cur.next) {
            if (cur.data.getName().equalsIgnoreCase(name)) {
                if (prev == null) head = cur.next; else prev.next = cur.next;
                size--;
                return cur.data;
            }
        }
        return null;
    }

    
    public boolean replace(String oldName, GameCharacter replacement) {
        for (Node cur = head; cur != null; cur = cur.next) {
            if (cur.data.getName().equalsIgnoreCase(oldName)) { cur.data = replacement; return true; }
        }
        return false;
    }

 
    public List<GameCharacter> search(String text) {
        String t = text.trim().toLowerCase();
        List<GameCharacter> out = new ArrayList<>();
        for (Node cur = head; cur != null; cur = cur.next) {
            GameCharacter c = cur.data;
            if (t.isEmpty() || c.getName().toLowerCase().contains(t) || c.getType().toLowerCase().contains(t))
                out.add(c);
        }
        return out;
    }

    public int size() { return size; }
}
