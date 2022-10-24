package eu.dragonarmy.homesystem;

import eu.dragonarmy.homesystem.commands.HomeCommand;
import eu.dragonarmy.homesystem.listener.GuiListener;
import eu.dragonarmy.homesystem.listener.MoveListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class HomeSystem extends JavaPlugin {
    private static final File listFile;
    private static final YamlConfiguration config;
    static {
        listFile = new File("plugins" + File.separator + "Home" + File.separator + "config.yml");
        config = YamlConfiguration.loadConfiguration(listFile);
    }

    public static Connection conn;

    private static HomeSystem instance;
    public static HomeSystem getInstance() {
        return instance;
    }

    private String db_sw;
    private String db_ip;
    private String db_port;
    private String db_name;
    private String db_user;
    private String db_pass;

    @Override
    public void onEnable() {
        instance = this;
        loadSettings();

        if (db_sw == null || db_sw.equals("")) {
            db_sw = "mysql";
        }
        if (db_ip == null || db_ip.equals("")) {
            db_ip = "localhost";
        }
        if (db_port == null || db_port.equals("")) {
            db_port = "3306";
        }
        if (db_name == null || db_name.equals("")) {
            db_name = "name";
        }
        if (db_user == null || db_user.equals("")) {
            db_user = "minecraft";
        }
        if (db_pass == null || db_pass.equals("")) {
            db_pass = "password";
        }

        String url = "jdbc:" + db_sw + "://" + db_ip + ":" + db_port + "/" + db_name;

        try {
            getLogger().info("Stelle Verbindung mit der Datenbank her");
            conn = DriverManager.getConnection(url, db_user, db_pass);

            PreparedStatement stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS home (uuid char(36), number INTEGER, location TEXT);");
            stmt.execute();
            stmt.close();

        } catch(SQLException e) {
            getLogger().info("Es konnte keine Verbindung mit der DatenBank hergestellt werden. Plugin wird deaktiviert!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
        getCommand("home").setExecutor(new HomeCommand());
    }

    @Override
    public void onDisable() {
        saveSettings();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadSettings() {
        db_sw = config.getString("database-software");
        db_ip = config.getString("database-ip");
        db_port = config.getString("database-port");
        db_name = config.getString("database-name");
        db_user = config.getString("database-user");
        db_pass = config.getString("database-password");
    }

    public void saveSettings() {
        config.set("database-software", db_sw);
        config.set("database-ip", db_ip);
        config.set("database-port", db_port);
        config.set("database-name", db_name);
        config.set("database-user", db_user);
        config.set("database-password", db_pass);

        try {
            config.save(listFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save file '" + listFile.getName() + "");
            throw new RuntimeException(e);
        }
    }
}