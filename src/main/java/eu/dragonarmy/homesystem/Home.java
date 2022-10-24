package eu.dragonarmy.homesystem;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Home {
    private UUID uuid;
    private int number;

    public Home(UUID uuid, int number) {
        this.uuid = uuid;
        this.number = number;
    }

    public void setLocation(HomeLocation loc) {
        try (PreparedStatement stmt = HomeSystem.conn.prepareStatement("REPLACE INTO home (uuid, number, location) VALUES (?, ?, ?)")) {
            Gson gson = new Gson();
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, number);
            stmt.setString(3, gson.toJson(loc));
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean teleport() {
        try (PreparedStatement stmt = HomeSystem.conn.prepareStatement("SELECT * FROM home WHERE uuid = ? AND number = ? ")) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, number);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Gson gson = new Gson();
                HomeLocation loc = gson.fromJson(rs.getString("location"), HomeLocation.class);
                Player player = Bukkit.getServer().getPlayer(uuid);

                Bukkit.getScheduler().runTask(HomeSystem.getInstance(), new Runnable() {
                    public void run() {
                        player.teleport(loc.getLocation());
                    }
                });
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasHome() {
        try (PreparedStatement stmt = HomeSystem.conn.prepareStatement("SELECT * FROM home WHERE uuid = ? AND number = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, number);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteHome() {
        try (PreparedStatement stmt = HomeSystem.conn.prepareStatement("DELETE FROM home WHERE uuid = ? AND number = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, number);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}