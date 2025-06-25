// Cs12prog.java
package com.mycompany.cs12prog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;


public class Cs12prog extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private static final String LOGIN_FILE = "C:\\Users\\1sart\\OneDrive - Halton Catholic District School Board\\Documents\\NetBeansProjects\\cs12prog\\src\\main\\java\\com\\mycompany\\cs12prog\\user_logins.txt";
    private static final String MOVIES_FILE = "C:\\Users\\1sart\\OneDrive - Halton Catholic District School Board\\Documents\\NetBeansProjects\\cs12prog\\src\\main\\java\\com\\mycompany\\cs12prog\\movies.json";

    private String currentUsername = "";
    private java.util.List<MediaEntry> allEntries = new ArrayList<>();
    private java.util.List<MediaEntry> favoriteEntries = new ArrayList<>();
    private java.util.Set<String> unlockedAchievements = new HashSet<>();
    private java.util.List<MovieItem> browseMovies = new ArrayList<>();
    private JPanel centerPanel;

    private final Map<String, String[]> genreMap = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Cs12prog app = new Cs12prog();
            app.setVisible(true);
        });
    }

    public Cs12prog() {
        loadGenreMap();
        loadMoviesJson();
        loadLoginScreen();
    }

    private void loadGenreMap() {
        genreMap.put("Movie", new String[]{"Action", "Romance", "Horror", "Comedy", "Drama"});
        genreMap.put("Book", new String[]{"Fiction", "Non-fiction", "Fantasy", "Mystery", "Romance"});
        genreMap.put("Game", new String[]{"RPG", "Shooter", "Strategy", "Puzzle"});
        genreMap.put("Music", new String[]{"Pop", "Rock", "Jazz", "Hip-Hop"});
        genreMap.put("TV Show", new String[]{"Sitcom", "Drama", "Thriller"});
        genreMap.put("Other", new String[]{"Misc"});
    }

    static class MovieItem {
        String title, description;
        int year, runtime, votes;
        double rating, revenue, metascore;

        public MovieItem(JSONObject obj) {
            title = obj.optString("Title");
            description = obj.optString("Description");
            year = obj.optInt("Year");
            runtime = obj.optInt("RuntimeMinutes");
            rating = obj.optDouble("Rating");
            votes = obj.optInt("Votes");
            revenue = obj.optDouble("RevenueMillions");
            metascore = obj.optDouble("Metascore");
        }

        public String toString() {
            return title + " (" + year + ") — " + description;
        }
    }

    static class MediaEntry {
        String name, type, genre, link, notes, progress;
        int rating;
        boolean favorite;

        public MediaEntry(String name, String type, String genre, int rating, String link, String notes, boolean favorite, String progress) {
            this.name = name;
            this.type = type;
            this.genre = genre;
            this.rating = rating;
            this.link = link;
            this.notes = notes;
            this.favorite = favorite;
            this.progress = progress;
        }

        public String getStars() {
            String stars = "";
            for (int i = 0; i < rating; i++) stars += "★";
            for (int i = rating; i < 10; i++) stars += "☆";
            return stars;
        }

        public String toString() {
            String s = name + " [" + type + ", " + genre + ", " + progress + "]: [" + getStars() + "]";
            if (!link.isEmpty()) s += " [" + link + "]";
            if (!notes.isEmpty()) s += "\n(" + notes + ")";
            if (favorite) s += " ❤️";
            return s;
        }

        public String toDataString() {
            return name + "|" + type + "|" + genre + "|" + rating + "|" + link + "|" + notes + "|" + favorite + "|" + progress;
        }

        public static MediaEntry fromDataString(String line) {
            String[] parts = line.split("\\|");
            return new MediaEntry(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], Boolean.parseBoolean(parts[6]), parts[7]);
        }
    }
    private void loadMoviesJson() {
        browseMovies.clear();
        try {
            File file = new File(MOVIES_FILE);
            if (!file.exists()) return;
            StringBuilder json = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();
            JSONArray array = new JSONArray(json.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                browseMovies.add(new MovieItem(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLoginScreen() {
        setTitle("Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                if (checkLogin(username, password)) {
                    currentUsername = username;
                    loadUserData();
                    loadHomePage();
                } else {
                    JOptionPane.showMessageDialog(Cs12prog.this, "Incorrect login.");
                }
            }
        });

        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadRegisterScreen();
            }
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(registerBtn);

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private void loadRegisterScreen() {
        setTitle("Register");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String confirm = new String(confirmPasswordField.getPassword()).trim();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(Cs12prog.this, "Fields can't be empty.");
                } else if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(Cs12prog.this, "Passwords do not match.");
                } else if (isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(Cs12prog.this, "Username already exists.");
                } else {
                    saveLogin(username, password);
                    JOptionPane.showMessageDialog(Cs12prog.this, "Registration successful.");
                    loadLoginScreen();
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadLoginScreen();
            }
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        panel.add(registerBtn);
        panel.add(backBtn);

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private boolean checkLogin(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(LOGIN_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {}
        return false;
    }

    private boolean isUsernameTaken(String username) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(LOGIN_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ",")) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {}
        return false;
    }

    private void saveLogin(String username, String password) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(LOGIN_FILE, true));
            writer.write(username + "," + password);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMediaFilePath() {
        return "C:\\Users\\1sart\\OneDrive - Halton Catholic District School Board\\Documents\\NetBeansProjects\\cs12prog\\src\\main\\java\\com\\mycompany\\cs12prog\\media_" + currentUsername + ".txt";
    }

    private String getAchievementFilePath() {
        return "C:\\Users\\1sart\\OneDrive - Halton Catholic District School Board\\Documents\\NetBeansProjects\\cs12prog\\src\\main\\java\\com\\mycompany\\cs12prog\\achievements_" + currentUsername + ".txt";
    }
    private void loadUserData() {
        allEntries.clear();
        favoriteEntries.clear();
        unlockedAchievements.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(getMediaFilePath()));
            String line;
            while ((line = reader.readLine()) != null) {
                MediaEntry entry = MediaEntry.fromDataString(line);
                allEntries.add(entry);
                if (entry.favorite) {
                    favoriteEntries.add(entry);
                }
            }
            reader.close();
        } catch (IOException e) {}

        try {
            BufferedReader reader = new BufferedReader(new FileReader(getAchievementFilePath()));
            String line;
            while ((line = reader.readLine()) != null) {
                unlockedAchievements.add(line.trim());
            }
            reader.close();
        } catch (IOException e) {}
    }

    private void saveUserData() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getMediaFilePath()));
            for (MediaEntry entry : allEntries) {
                writer.write(entry.toDataString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getAchievementFilePath()));
            for (String ach : unlockedAchievements) {
                writer.write(ach);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHomePage() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUsername + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add Logout and Manual buttons
        JPanel topRight = new JPanel();
        JButton logoutBtn = new JButton("Logout");
        JButton manualBtn = new JButton("Manual");
        topRight.add(manualBtn);
        topRight.add(logoutBtn);
        topPanel.add(topRight, BorderLayout.EAST);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        // Sidebar with navigation buttons
        JPanel sidebar = new JPanel(new GridLayout(0, 1, 5, 5));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("➕ Add Media");
        JButton viewAllBtn = new JButton("📚 View All");
        JButton viewFavBtn = new JButton("❤️ Favorites");
        JButton watchlistBtn = new JButton("📺 Watchlist");
        JButton achievementsBtn = new JButton("🏆 Achievements");
        JButton browseBtn = new JButton("🔍 Browse");

        sidebar.add(addBtn);
        sidebar.add(viewAllBtn);
        sidebar.add(viewFavBtn);
        sidebar.add(watchlistBtn);
        sidebar.add(achievementsBtn);
        sidebar.add(browseBtn);

        getContentPane().add(sidebar, BorderLayout.WEST);

        centerPanel = new JPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        // Button Actions
        addBtn.addActionListener(e -> showAddMediaDialog(centerPanel));
        viewAllBtn.addActionListener(e -> showLibrary(centerPanel, false, false));
        viewFavBtn.addActionListener(e -> showLibrary(centerPanel, true, false));
        watchlistBtn.addActionListener(e -> showLibrary(centerPanel, false, true));
        achievementsBtn.addActionListener(e -> showAchievements(centerPanel));
        browseBtn.addActionListener(e -> showBrowseTab(centerPanel));
        logoutBtn.addActionListener(e -> {
            currentUsername = "";
            allEntries.clear();
            favoriteEntries.clear();
            unlockedAchievements.clear();
            loadLoginScreen();
        });
        manualBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "📘 User Manual:\n\n- Add Media: Add a new entry.\n- View All/Favorites/Watchlist: Filter your library.\n- Achievements: See unlocked milestones.\n- Browse: Search popular movies.\n- Logout: Return to login screen."
        ));

        revalidate();
        repaint();
    }

    
    private void unlockAchievements(MediaEntry entry) {
        if (allEntries.size() == 1) unlockedAchievements.add("First Media Logged");
        if (allEntries.size() == 10) unlockedAchievements.add("Logged 10 Entries");
        if (allEntries.size() == 100) unlockedAchievements.add("Logged 100 Entries");
        if (entry.rating == 10) unlockedAchievements.add("First Media Rated 10/10");
        if (entry.rating < 3) unlockedAchievements.add("First Media Rated Below 3");

        HashSet<String> genres = new HashSet<>();
        for (MediaEntry m : allEntries) {
            genres.add(m.genre);
        }
        if (genres.size() >= 5) {
            unlockedAchievements.add("Logged Media Across 5 Different Genres");
        }
    }
        private void showEditDialog(MediaEntry entry) {
        JTextField nameField = new JTextField(entry.name);
        JTextField linkField = new JTextField(entry.link);
        JTextField notesField = new JTextField(entry.notes);
        JCheckBox favCheck = new JCheckBox("Favorite", entry.favorite);
        JSlider ratingSlider = new JSlider(1, 10, entry.rating);

        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:")); panel.add(nameField);
        panel.add(new JLabel("Link:")); panel.add(linkField);
        panel.add(new JLabel("Notes:")); panel.add(notesField);
        panel.add(new JLabel("Rating:")); panel.add(ratingSlider);
        panel.add(favCheck);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Entry", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            entry.name = nameField.getText().trim();
            entry.link = linkField.getText().trim();
            entry.notes = notesField.getText().trim();
            entry.favorite = favCheck.isSelected();
            entry.rating = ratingSlider.getValue();
        }
    }
        
    private List<MediaEntry> loadMoviesFromJSON() {
        List<MediaEntry> entries = new ArrayList<>();
        try {
            File file = new File(MOVIES_FILE);
            if (!file.exists()) return entries;

            StringBuilder jsonText = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            reader.close();

            org.json.JSONArray array = new org.json.JSONArray(jsonText.toString());
            for (int i = 0; i < array.length(); i++) {
                org.json.JSONObject obj = array.getJSONObject(i);
                String title = obj.optString("Title");
                String desc = obj.optString("Description");
                int rating = (int) Math.round(obj.optDouble("Rating", 0));
                rating = Math.max(1, Math.min(10, rating));

                MediaEntry entry = new MediaEntry(title, "Movie", "Misc", rating, "", desc, false, "Incomplete");
                entries.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;
    }

        
        
    
    
    

    
    private void showAddMediaDialog(JPanel panel) {
        JTextField nameField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(genreMap.keySet().toArray(new String[0]));
        JComboBox<String> genreBox = new JComboBox<>();
        JComboBox<String> progressBox = new JComboBox<>(new String[]{"Watchlist", "Incomplete", "Watched"});
        JSlider ratingSlider = new JSlider(1, 10, 5);
        JTextField linkField = new JTextField();
        JTextField notesField = new JTextField();
        JCheckBox favCheck = new JCheckBox("Favorite");

        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);

        typeBox.addActionListener(e -> {
            genreBox.removeAllItems();
            for (String g : genreMap.get(typeBox.getSelectedItem())) {
                genreBox.addItem(g);
            }
        });
        typeBox.setSelectedIndex(0);

        JPanel form = new JPanel(new GridLayout(0, 1));
        form.add(new JLabel("Title:")); form.add(nameField);
        form.add(new JLabel("Type:")); form.add(typeBox);
        form.add(new JLabel("Genre:")); form.add(genreBox);
        form.add(new JLabel("Progress:")); form.add(progressBox);
        form.add(new JLabel("Rating:")); form.add(ratingSlider);
        form.add(new JLabel("Link:")); form.add(linkField);
        form.add(new JLabel("Notes:")); form.add(notesField);
        form.add(favCheck);

        int result = JOptionPane.showConfirmDialog(this, form, "Add Media", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String type = (String) typeBox.getSelectedItem();
            boolean duplicate = allEntries.stream().anyMatch(m -> m.name.equalsIgnoreCase(name) && m.type.equals(type));
            if (duplicate) {
                JOptionPane.showMessageDialog(this, "Duplicate entry.");
                return;
            }
            MediaEntry entry = new MediaEntry(
                name, type, (String) genreBox.getSelectedItem(), ratingSlider.getValue(),
                linkField.getText().trim(), notesField.getText().trim(),
                favCheck.isSelected(), (String) progressBox.getSelectedItem()
            );
            allEntries.add(entry);
            if (entry.favorite) favoriteEntries.add(entry);
            unlockAchievements(entry);
            saveUserData();
        }
    }
    private void showLibrary(JPanel panel, boolean favoritesOnly, boolean watchlistOnly) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JComboBox<String> typeFilter = new JComboBox<>(new String[]{"All", "Movie", "Book", "Game", "Music", "TV Show", "Other"});
        JComboBox<String> genreFilter = new JComboBox<>(new String[]{"All"});
        JComboBox<String> sortFilter = new JComboBox<>(new String[]{"Highest Rating", "Lowest Rating"});

        DefaultListModel<MediaEntry> model = new DefaultListModel<>();
        JList<MediaEntry> list = new JList<>(model);
        list.setCellRenderer((lst, val, i, sel, foc) -> {
            JLabel label = new JLabel(val.toString());
            label.setOpaque(true);
            if (val.rating >= 8) label.setBackground(Color.GREEN.brighter());
            else if (val.rating >= 5) label.setBackground(Color.ORANGE);
            else label.setBackground(Color.PINK);
            label.setForeground(Color.BLACK);
            return label;
        });

        JScrollPane scrollPane = new JScrollPane(list);
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");

        Runnable applyFilters = () -> {
            model.clear();
            for (MediaEntry m : allEntries) {
                if (favoritesOnly && !m.favorite) continue;
                if (watchlistOnly && !m.progress.equalsIgnoreCase("Watchlist")) continue;
                model.addElement(m);
            }

            String selectedType = (String) typeFilter.getSelectedItem();
            String selectedGenre = (String) genreFilter.getSelectedItem();
            String sortOrder = (String) sortFilter.getSelectedItem();

            List<MediaEntry> filtered = new ArrayList<>();
            for (int i = 0; i < model.getSize(); i++) {
                MediaEntry m = model.getElementAt(i);
                if (!selectedType.equals("All") && !m.type.equals(selectedType)) continue;
                if (!selectedGenre.equals("All") && !m.genre.equals(selectedGenre)) continue;
                filtered.add(m);
            }

            filtered.sort((a, b) -> sortOrder.equals("Lowest Rating") ? a.rating - b.rating : b.rating - a.rating);

            model.clear();
            for (MediaEntry m : filtered) model.addElement(m);
        };

        typeFilter.addActionListener(e -> {
            genreFilter.removeAllItems();
            genreFilter.addItem("All");
            String type = (String) typeFilter.getSelectedItem();
            if (genreMap.containsKey(type)) {
                for (String g : genreMap.get(type)) genreFilter.addItem(g);
            }
            applyFilters.run();
        });

        genreFilter.addActionListener(e -> applyFilters.run());
        sortFilter.addActionListener(e -> applyFilters.run());

        editBtn.addActionListener(e -> {
            MediaEntry entry = list.getSelectedValue();
            if (entry != null) {
                showEditDialog(entry);
                saveUserData();
                applyFilters.run();
            }
        });

        deleteBtn.addActionListener(e -> {
            MediaEntry entry = list.getSelectedValue();
            if (entry != null) {
                allEntries.remove(entry);
                favoriteEntries.remove(entry);
                saveUserData();
                applyFilters.run();
            }
        });

        JPanel filters = new JPanel();
        filters.add(new JLabel("Type:")); filters.add(typeFilter);
        filters.add(new JLabel("Genre:")); filters.add(genreFilter);
        filters.add(new JLabel("Sort:")); filters.add(sortFilter);

        JPanel buttons = new JPanel();
        buttons.add(editBtn); buttons.add(deleteBtn);

        panel.add(filters, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        panel.revalidate(); panel.repaint();

        applyFilters.run();
    }
    private void showAchievements(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JPanel list = new JPanel(new GridLayout(0, 1));
        for (String ach : List.of(
            "First Media Logged",
            "Logged 10 Entries",
            "Logged 100 Entries",
            "First Media Rated 10/10",
            "First Media Rated Below 3",
            "Logged Media Across 5 Different Genres"
        )) {
            boolean unlocked = unlockedAchievements.contains(ach);
            JLabel label = new JLabel((unlocked ? "✅ " : "🔒 ") + ach);
            list.add(label);
        }

        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
    private void showBrowseTab(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        DefaultListModel<MediaEntry> model = new DefaultListModel<>();
        JList<MediaEntry> list = new JList<>(model);
        list.setCellRenderer((lst, val, i, sel, foc) -> {
            JLabel label = new JLabel(val.toString());
            label.setOpaque(true);
            if (val.rating >= 8) label.setBackground(Color.GREEN.brighter());
            else if (val.rating >= 5) label.setBackground(Color.ORANGE);
            else label.setBackground(Color.PINK);
            label.setForeground(Color.BLACK);
            return label;
        });

        JScrollPane scrollPane = new JScrollPane(list);
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        JButton addBtn = new JButton("Add Selected to Library");

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Search movies:"), BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);
        top.add(searchBtn, BorderLayout.EAST);

        JPanel bottom = new JPanel();
        bottom.add(addBtn);

        searchBtn.addActionListener(e -> {
            model.clear();
            String query = searchField.getText().toLowerCase();
            for (MediaEntry entry : loadMoviesFromJSON()) {
                if (entry.name.toLowerCase().contains(query)) {
                    model.addElement(entry);
                }
            }
        });

        addBtn.addActionListener(e -> {
            MediaEntry selected = list.getSelectedValue();
            if (selected != null) {
                boolean duplicate = allEntries.stream().anyMatch(m -> m.name.equalsIgnoreCase(selected.name) && m.type.equals("Movie"));
                if (duplicate) {
                    JOptionPane.showMessageDialog(this, "This movie is already in your library.");
                    return;
                }
                allEntries.add(selected);
                if (selected.favorite) favoriteEntries.add(selected);
                unlockAchievements(selected);
                saveUserData();
                JOptionPane.showMessageDialog(this, "Added to your library!");
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        panel.revalidate(); panel.repaint();
    }
}
