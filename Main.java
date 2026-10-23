import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame {
    static final Color GOLD = new Color(255, 215, 80), DARK = new Color(25, 25, 25),
            GREEN = new Color(85, 255, 85), STONE = new Color(125, 125, 125);

    static Font baseFont = new Font(Font.MONOSPACED, Font.BOLD, 12);

    static void loadFont() {
        String[] paths = {"fonts/minecraft.ttf", "fonts/Minecraft.ttf", "fonts/minecraft.otf",
                "fonts/Minecraftia.ttf", "minecraft.ttf", "Minecraft.ttf"};
        for (String p : paths) {
            File f = new File(p);
            if (!f.exists()) continue;
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                baseFont = font;
                return;
            } catch (Exception ignored) { }
        }
    }
    static Font mc(float size) { return baseFont.deriveFont(size); }

    static final Map<String, BufferedImage> IMAGES = new HashMap<>();

    static BufferedImage image(String file) {
        if (IMAGES.containsKey(file)) return IMAGES.get(file);
        BufferedImage img = null;
        List<File> dirs = new ArrayList<>();
        dirs.add(new File("images"));
        dirs.add(new File(System.getProperty("user.dir"), "images"));
        try {
            File here = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File base = here.isDirectory() ? here : here.getParentFile();
            dirs.add(new File(base, "images"));
            if (base.getParentFile() != null) dirs.add(new File(base.getParentFile(), "images"));
        } catch (Exception ignored) { }
        for (File d : dirs) {
            File f = new File(d, file);
            if (f.exists()) {
                try { img = crop(ImageIO.read(f)); break; } catch (Exception ignored) { }
            }
        }
        if (img == null) System.err.println("Could not find image: " + file + " (looked in " + dirs + ")");
        IMAGES.put(file, img);
        return img;
    }

    static BufferedImage crop(BufferedImage s) {
        int minX = s.getWidth(), minY = s.getHeight(), maxX = -1, maxY = -1;
        for (int y = 0; y < s.getHeight(); y++)
            for (int x = 0; x < s.getWidth(); x++)
                if ((s.getRGB(x, y) >>> 24) > 20) {
                    minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y); maxY = Math.max(maxY, y);
                }
        return maxX < 0 ? s : s.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    static class DirtBackground extends JPanel {
        private final BufferedImage tile = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        DirtBackground() {
            Random r = new Random(7);
            for (int y = 0; y < 16; y++)
                for (int x = 0; x < 16; x++) {
                    int v = r.nextInt(30) - 15;
                    tile.setRGB(x, y, new Color(105 + v, 75 + v, 48 + v / 2).getRGB());
                }
        }
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            for (int y = 0; y < getHeight(); y += 64)
                for (int x = 0; x < getWidth(); x += 64) g.drawImage(tile, x, y, 64, 64, null);
            g.setColor(new Color(0, 0, 0, 110));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    static class GlassPanel extends JPanel {
        GlassPanel(LayoutManager lm) { super(lm); setOpaque(false); setBorder(new EmptyBorder(12, 12, 12, 12)); }
        @Override protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            g.setColor(new Color(15, 25, 35, 175)); g.fillRect(0, 0, w, h);
            g.setColor(new Color(150, 210, 255, 40)); g.fillRect(0, 0, w, h);
            g.setColor(new Color(255, 255, 255, 60));
            for (int k = 0; k <= 6; k++) { g.fillRect(8 + k * 4, 24 - k * 4, 4, 4); g.fillRect(24 + k * 4, 24 - k * 4, 4, 4); }
            g.setColor(Color.BLACK); g.drawRect(0, 0, w - 1, h - 1); g.drawRect(1, 1, w - 3, h - 3);
            g.setColor(new Color(255, 255, 255, 140)); g.fillRect(2, 2, w - 4, 2); g.fillRect(2, 2, 2, h - 4);
            g.setColor(new Color(0, 0, 0, 130)); g.fillRect(2, h - 4, w - 4, 2); g.fillRect(w - 4, 2, 2, h - 4);
        }
    }

    static class PixelButton extends JButton {
        private final Color base;
        PixelButton(String text, Color base) {
            super(text);
            this.base = base;
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setRolloverEnabled(true); setFont(mc(14)); setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 36)); setAlignmentX(LEFT_ALIGNMENT);
        }
        @Override protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            ButtonModel m = getModel();
            Color c = m.isPressed() ? base.darker() : m.isRollover() ? base.brighter() : base;
            g.setColor(c); g.fillRect(0, 0, w, h);
            boolean down = m.isPressed();
            g.setColor(new Color(255, 255, 255, down ? 40 : 110)); g.fillRect(2, 2, w - 4, 3); g.fillRect(2, 2, 3, h - 4);
            g.setColor(new Color(0, 0, 0, down ? 60 : 130)); g.fillRect(2, h - 5, w - 4, 3); g.fillRect(w - 5, 2, 3, h - 4);
            g.setColor(Color.BLACK); g.drawRect(0, 0, w - 1, h - 1); g.drawRect(1, 1, w - 3, h - 3);
            g.setFont(getFont());
            FontMetrics fm = g.getFontMetrics();
            int tx = (w - fm.stringWidth(getText())) / 2, ty = (h + fm.getAscent() - fm.getDescent()) / 2 + (down ? 1 : 0);
            g.setColor(new Color(0, 0, 0, 170)); g.drawString(getText(), tx + 2, ty + 2);
            g.setColor(Color.WHITE); g.drawString(getText(), tx, ty);
        }
    }

    private final CharacterList roster = new CharacterList();
    private final Deque<GameCharacter> recycleBin = new ArrayDeque<>();
    private final Random rng = new Random();

    private DefaultTableModel model;
    private JTable table;
    private JTextField nameField, searchField;
    private JComboBox<String> typeBox;
    private JSpinner levelSpinner;
    private JTextArea log;
    private AvatarPanel avatar;
    private List<GameCharacter> shown = new ArrayList<>();

    public Main() {
        super("Game Character Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        DirtBackground root = new DirtBackground();
        root.setLayout(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        root.add(buildTitle(), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.WEST);
        root.add(buildTable(), BorderLayout.CENTER);
        root.add(buildBattle(), BorderLayout.SOUTH);

        seedSamples();
        refreshTable(null);
        setSize(1100, 860);
        setMinimumSize(new Dimension(980, 700));
        setLocationRelativeTo(null);
    }

    private JComponent buildTitle() {
        JComponent t = new JComponent() {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0;
                g.setFont(mc(34));
                String s = "GAME CHARACTER MANAGER";
                int x = (getWidth() - g.getFontMetrics().stringWidth(s)) / 2, y = 34;
                g.setColor(new Color(60, 40, 0)); g.drawString(s, x + 4, y + 4);
                g.setColor(GOLD); g.drawString(s, x, y);
            }
        };
        t.setPreferredSize(new Dimension(0, 48));
        return t;
    }

    private JComponent buildForm() {
        GlassPanel p = new GlassPanel(null);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        avatar = new AvatarPanel();
        avatar.setAlignmentX(LEFT_ALIGNMENT);
        p.add(avatar);
        p.add(Box.createVerticalStrut(8));

        nameField = new JTextField();
        levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        searchField = new JTextField();
        typeBox = new JComboBox<>(new String[]{"Warrior", "Mage", "Archer"});
        for (JComponent c : new JComponent[]{nameField, searchField, levelSpinner, typeBox}) {
            c.setBackground(DARK); c.setForeground(Color.WHITE); c.setFont(mc(14));
            c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); c.setAlignmentX(LEFT_ALIGNMENT);
        }
        Color edge = new Color(150, 150, 150);
        nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(edge, 2), new EmptyBorder(2, 6, 2, 6)));
        searchField.setBorder(nameField.getBorder());
        nameField.setCaretColor(Color.WHITE); searchField.setCaretColor(Color.WHITE);
        levelSpinner.setBorder(BorderFactory.createLineBorder(edge, 2));
        JTextField sf = ((JSpinner.DefaultEditor) levelSpinner.getEditor()).getTextField();
        sf.setBackground(DARK); sf.setForeground(Color.WHITE); sf.setFont(mc(14)); sf.setCaretColor(Color.WHITE);
        typeBox.setBorder(BorderFactory.createLineBorder(edge, 2));
        typeBox.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                setBackground(s ? new Color(70, 110, 60) : DARK); setForeground(Color.WHITE);
                setFont(mc(14)); setBorder(new EmptyBorder(3, 6, 3, 6));
                return this;
            }
        });

        p.add(label("NAME (max 15)")); p.add(nameField);
        p.add(label("CLASS")); p.add(typeBox);
        p.add(label("LEVEL (1-99)")); p.add(levelSpinner);
        p.add(Box.createVerticalStrut(10));

        PixelButton add = new PixelButton("Add", new Color(70, 150, 70));
        PixelButton upd = new PixelButton("Update Selected", new Color(70, 110, 180));
        PixelButton rem = new PixelButton("Remove Selected", new Color(180, 60, 60));
        PixelButton undo = new PixelButton("Undo Remove", new Color(150, 120, 50));
        add.addActionListener(e -> addCharacter());
        upd.addActionListener(e -> updateCharacter());
        rem.addActionListener(e -> removeCharacter());
        undo.addActionListener(e -> undoRemove());
        add.setText("Add"); upd.setText("Update"); rem.setText("Remove"); undo.setText("Undo");
        JPanel grid = new JPanel(new GridLayout(2, 2, 6, 6));
        grid.setOpaque(false); grid.setAlignmentX(LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        grid.add(add); grid.add(upd); grid.add(rem); grid.add(undo);
        p.add(grid);
        p.add(Box.createVerticalStrut(6));

        p.add(label("SEARCH (name / class)")); p.add(searchField);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(null); }
            public void removeUpdate(DocumentEvent e) { refreshTable(null); }
            public void changedUpdate(DocumentEvent e) { refreshTable(null); }
        });
        JScrollPane scroll = new JScrollPane(p, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(300, 0));
        return scroll;
    }

    private JComponent buildTable() {
        model = new DefaultTableModel(new String[]{"Name", "Class", "Level", "HP"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(mc(14));
        table.setGridColor(new Color(20, 20, 20));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                GameCharacter ch = r < shown.size() ? shown.get(r) : null;
                setFont(mc(14));
                setBackground(sel ? new Color(70, 120, 60) : r % 2 == 0 ? new Color(45, 45, 45) : new Color(58, 58, 58));
                Color fg = Color.WHITE;
                if (ch != null && c == 1) fg = ch.getColor().brighter();
                if (ch != null && c == 3 && ch.isDefeated()) fg = new Color(255, 85, 85);
                setForeground(fg);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setOpaque(true); setBackground(new Color(80, 80, 80)); setForeground(GOLD); setFont(mc(14));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 3, 2, Color.BLACK), new EmptyBorder(4, 8, 4, 8)));
                return this;
            }
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            GameCharacter c = selected();
            if (c != null) {
                nameField.setText(c.getName());
                typeBox.setSelectedItem(c.getType());
                levelSpinner.setValue(c.getLevel());
            }
            avatar.setCharacter(c);
        });
        GlassPanel p = new GlassPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(new Color(45, 45, 45));
        sp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JComponent buildBattle() {
        GlassPanel p = new GlassPanel(new BorderLayout(12, 0));
        log = new JTextArea(5, 40);
        log.setEditable(false); log.setBackground(new Color(10, 10, 10)); log.setForeground(GREEN);
        log.setFont(mc(13)); log.setMargin(new Insets(6, 8, 6, 8));
        JScrollPane sp = new JScrollPane(log);
        sp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel btns = new JPanel(new GridLayout(3, 1, 0, 6));
        btns.setOpaque(false);
        PixelButton atk = new PixelButton("Attack", new Color(190, 90, 40));
        PixelButton spc = new PixelButton("Special Move", new Color(130, 70, 200));
        PixelButton heal = new PixelButton("Heal All", new Color(40, 150, 150));
        atk.addActionListener(e -> fight(false));
        spc.addActionListener(e -> fight(true));
        heal.addActionListener(e -> {
            for (GameCharacter c : roster.search("")) c.fullHeal();
            print("All characters fully healed.");
            refreshTable(selectedName());
        });
        btns.add(atk); btns.add(spc); btns.add(heal);
        btns.setPreferredSize(new Dimension(190, 0));
        p.add(sp, BorderLayout.CENTER); p.add(btns, BorderLayout.EAST);
        return p;
    }

    private JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setForeground(GOLD); l.setFont(mc(12)); l.setAlignmentX(LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(6, 0, 2, 0));
        return l;
    }

    private String validName(String ignoreName) {
        String n = nameField.getText().trim();
        if (n.isEmpty()) { error("Name cannot be empty."); return null; }
        if (n.length() > 15) { error("Name must be 15 characters or fewer."); return null; }
        if (!n.matches("[A-Za-z0-9 ]+")) { error("Name may only contain letters, numbers and spaces."); return null; }
        GameCharacter dup = roster.findByName(n);
        if (dup != null && !dup.getName().equalsIgnoreCase(ignoreName == null ? "" : ignoreName)) {
            error("A character named \"" + n + "\" already exists."); return null;
        }
        return n;
    }

    private GameCharacter create(String type, String name, int level) {
        switch (type) {
            case "Mage":   return new Mage(name, level);
            case "Archer": return new Archer(name, level);
            default:       return new Warrior(name, level);
        }
    }

    private void addCharacter() {
        String n = validName(null);
        if (n == null) return;
        GameCharacter c = create((String) typeBox.getSelectedItem(), n, (Integer) levelSpinner.getValue());
        roster.add(c);
        print("Created " + c.showName());
        nameField.setText("");
        refreshTable(n);
    }

    private void updateCharacter() {
        GameCharacter old = selected();
        if (old == null) { error("Select a character in the table first."); return; }
        String n = validName(old.getName());
        if (n == null) return;
        GameCharacter c = create((String) typeBox.getSelectedItem(), n, (Integer) levelSpinner.getValue());
        roster.replace(old.getName(), c);
        print("Updated " + old.getName() + " -> " + c.showName());
        refreshTable(n);
    }

    private void removeCharacter() {
        GameCharacter c = selected();
        if (c == null) { error("Select a character in the table first."); return; }
        if (JOptionPane.showConfirmDialog(this, "Remove " + c.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        recycleBin.push(roster.removeByName(c.getName()));
        print("Removed " + c.getName() + " (use Undo Remove to restore).");
        avatar.setCharacter(null);
        refreshTable(null);
    }

    private void undoRemove() {
        if (recycleBin.isEmpty()) { error("Nothing to undo."); return; }
        GameCharacter c = recycleBin.pop();
        if (roster.findByName(c.getName()) != null) { error("Cannot restore: name \"" + c.getName() + "\" is in use."); return; }
        roster.add(c);
        print("Restored " + c.getName() + ".");
        refreshTable(c.getName());
    }

    private void fight(boolean special) {
        GameCharacter a = selected();
        if (a == null) { error("Select an attacker first."); return; }
        if (a.isDefeated()) { error(a.getName() + " is defeated. Use Heal All."); return; }
        List<GameCharacter> foes = new ArrayList<>();
        for (GameCharacter c : roster.search("")) if (c != a && !c.isDefeated()) foes.add(c);
        if (foes.isEmpty()) { error("No living opponents available."); return; }
        GameCharacter t = foes.get(rng.nextInt(foes.size()));
        print(special ? a.specialMove() : a.attack());
        int dmg = special ? a.specialDamage() : a.attackDamage();
        t.takeDamage(dmg);
        print("  -> " + t.getName() + " takes " + dmg + " damage (" + t.getHp() + "/" + t.getMaxHp() + " HP).");
        if (special && !a.afterSpecial().isEmpty()) print("  " + a.afterSpecial());
        if (t.isDefeated()) print("  *** " + t.getName() + " has been defeated! ***");
        refreshTable(a.getName());
    }

    private GameCharacter selected() {
        int r = table.getSelectedRow();
        return (r >= 0 && r < shown.size()) ? shown.get(r) : null;
    }
    private String selectedName() { GameCharacter c = selected(); return c == null ? null : c.getName(); }

    private void refreshTable(String reselect) {
        shown = roster.search(searchField == null ? "" : searchField.getText());
        model.setRowCount(0);
        int sel = -1;
        for (int i = 0; i < shown.size(); i++) {
            GameCharacter c = shown.get(i);
            model.addRow(new Object[]{c.getName(), c.getType(), c.getLevel(), c.getHp() + " / " + c.getMaxHp()});
            if (c.getName().equals(reselect)) sel = i;
        }
        if (sel >= 0) table.setRowSelectionInterval(sel, sel);
        else if (reselect == null) avatar.setCharacter(null);
        avatar.repaint();
    }

    private void print(String s) { log.append(s + "\n"); log.setCaretPosition(log.getDocument().getLength()); }
    private void error(String s) { JOptionPane.showMessageDialog(this, s, "Oops!", JOptionPane.WARNING_MESSAGE); }

    private void seedSamples() {
        roster.add(new Warrior("Brader", 5));
        roster.add(new Mage("GabMids", 5));
        roster.add(new Archer("Sylva", 4));
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception ignored) { }
        loadFont();
        UIManager.put("OptionPane.messageFont", mc(13));
        UIManager.put("OptionPane.buttonFont", mc(13));
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

    private class AvatarPanel extends JPanel {
        private final String[] HEART = {".XX.XX.", "XXXXXXX", "XXXXXXX", "XXXXXXX", ".XXXXX.", "..XXX..", "...X..."};
        private GameCharacter ch;
        private double shownHp, bob;

        AvatarPanel() {
            setPreferredSize(new Dimension(250, 240));
            setMinimumSize(new Dimension(250, 240));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
            new Timer(30, e -> {
                bob += 0.12;
                if (ch != null) shownHp += (ch.getHp() - shownHp) * 0.15;
                repaint();
            }).start();
        }

        void setCharacter(GameCharacter c) { ch = c; if (c != null) shownHp = c.getHp(); }

        private void heart(Graphics2D g, int x, int y, int s, int state) {
            for (int r = 0; r < HEART.length; r++)
                for (int c = 0; c < HEART[r].length(); c++)
                    if (HEART[r].charAt(c) == 'X') {
                        boolean on = state == 2 || (state == 1 && c < 4);
                        g.setColor(on ? new Color(230, 40, 50) : new Color(70, 25, 25));
                        g.fillRect(x + c * s, y + r * s, s, s);
                    }
        }

        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            int w = getWidth(), h = getHeight(), sceneH = h - 72, gy = sceneH - 30;

            g.setPaint(new GradientPaint(0, 0, new Color(95, 165, 240), 0, sceneH, new Color(190, 230, 255)));
            g.fillRect(0, 0, w, sceneH);
            g.setColor(new Color(255, 255, 255, 210));
            g.fillRect(20, 22, 44, 12); g.fillRect(32, 12, 24, 10);
            g.fillRect(w - 84, 44, 48, 12); g.fillRect(w - 72, 34, 24, 10);
            for (int x = 0; x < w; x += 16) {
                g.setColor((x / 16) % 2 == 0 ? new Color(95, 66, 42) : new Color(125, 90, 58));
                g.fillRect(x, gy + 10, 16, 20);
            }
            for (int x = 0; x < w; x += 8) {
                g.setColor((x / 8) % 3 == 0 ? new Color(70, 140, 45) : new Color(100, 185, 65));
                g.fillRect(x, gy, 8, 10);
            }
            g.setColor(new Color(20, 20, 20)); g.fillRect(0, sceneH, w, h - sceneH);

            if (ch == null) {
                g.setColor(Color.WHITE); g.setFont(mc(14));
                String s = "Select a character";
                g.drawString(s, (w - g.getFontMetrics().stringWidth(s)) / 2, sceneH / 2);
            } else {
                BufferedImage img = image(ch.getImageFile());
                if (img != null) {
                    double sc = Math.min((w - 20.0) / img.getWidth(), (sceneH - 24.0) / img.getHeight());
                    int iw = (int) (img.getWidth() * sc), ih = (int) (img.getHeight() * sc);
                    int dy = (int) (Math.sin(bob) * 3);
                    if (ch.isDefeated()) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                    g.drawImage(img, (w - iw) / 2, gy + 14 - ih + dy, iw, ih, null);
                    g.setComposite(AlphaComposite.SrcOver);
                } else {
                    g.setColor(new Color(150, 20, 20)); g.setFont(mc(12));
                    g.drawString("missing: images/" + ch.getImageFile(), 10, sceneH / 2);
                }
                if (ch.isDefeated()) {
                    g.setFont(mc(26)); String s = "DEFEATED";
                    int x = (w - g.getFontMetrics().stringWidth(s)) / 2;
                    g.setColor(Color.BLACK); g.drawString(s, x + 3, 53);
                    g.setColor(new Color(255, 70, 70)); g.drawString(s, x, 50);
                }
                g.setFont(mc(14));
                String s = ch.getName() + "  Lv." + ch.getLevel();
                int tx = (w - g.getFontMetrics().stringWidth(s)) / 2;
                g.setColor(Color.BLACK); g.drawString(s, tx + 2, sceneH + 22);
                g.setColor(Color.WHITE); g.drawString(s, tx, sceneH + 20);
                double pct = Math.max(0, shownHp / ch.getMaxHp()) * 10;
                int total = 10 * 14 + 9 * 3, hx = (w - total) / 2;
                for (int i = 0; i < 10; i++) {
                    double fill = Math.max(0, Math.min(1, pct - i));
                    heart(g, hx + i * 17, sceneH + 34, 2, fill >= 0.75 ? 2 : fill >= 0.25 ? 1 : 0);
                }
            }
            g.setColor(Color.BLACK); g.drawRect(0, 0, w - 1, h - 1); g.drawRect(1, 1, w - 3, h - 3);
            g.dispose();
        }
    }
}