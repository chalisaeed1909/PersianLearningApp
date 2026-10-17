package data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.dao.ChapterDAO;
import model.Chapter;

public class MySQLChapterDAO implements ChapterDAO {

    @Override
    public Chapter create(String title) {
        String sql = "INSERT INTO chapter (title) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Chapter(keys.getInt(1), title);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Chapter save nahi ho saka: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Chapter> findAll() {
        List<Chapter> list = new ArrayList<>();
        String sql = "SELECT id, title FROM chapter ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Chapter(rs.getInt("id"), rs.getString("title")));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Chapters load nahi ho sakay: " + e.getMessage(), e);
        }
    }

    @Override
    public Chapter findById(int id) {
        String sql = "SELECT id, title FROM chapter WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new Chapter(rs.getInt("id"), rs.getString("title")) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Chapter load nahi ho saka: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateTitle(int id, String title) {
        String sql = "UPDATE chapter SET title = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Chapter update nahi ho saka: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        // Components aur glossary ka cascade delete database ke ON DELETE CASCADE se hoga.
        String sql = "DELETE FROM chapter WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Chapter delete nahi ho saka: " + e.getMessage(), e);
        }
    }
}