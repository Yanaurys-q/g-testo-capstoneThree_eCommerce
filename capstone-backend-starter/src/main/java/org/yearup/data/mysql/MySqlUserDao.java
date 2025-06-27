package org.yearup.data.mysql;

import org.springframework.stereotype.Repository;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MySqlUserDao extends MySqlDaoBase implements UserDao
{
    public MySqlUserDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile getByUserId(int userId) {
        return null;
    }

    @Override
    public User create(User newUser) {
        String sql = "INSERT INTO users (username, hashed_password, role) VALUES (?, ?, ?)";
        String hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newUser.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, newUser.getRole());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                newUser.setId(id);
                newUser.setPassword(""); // Don’t return the plain password
                return newUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public List<User> getAll()
    {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet row = statement.executeQuery();

            while (row.next())
            {
                User user = mapRow(row);
                users.add(user);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User getUserById(int id)
    {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public User getByUserName(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("hashed_password"));
                user.setRole(rs.getString("role"));
                return user;
            } else {
                throw new UsernameNotFoundException("User not found: " + username);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getIdByUsername(String username) {
        return 0;
    }

    @Override
    public User findByUsername(String username)
    {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean exists(String username)
    {
        return findByUsername(username) != null;
    }

    private User mapRow(ResultSet row) throws SQLException
    {
        int userId = row.getInt("user_id");
        String username = row.getString("username");
        String hashedPassword = row.getString("hashed_password");
        String role = row.getString("role");
        return new User(userId, username, hashedPassword, role);
    }
}