package org.yearup.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.yearup.models.Profile;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class ProfileDaoJdbc implements ProfileDao
{
    private final JdbcTemplate jdbcTemplate;

    public ProfileDaoJdbc(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Profile> getAll()
    {
        String sql = "SELECT * FROM profiles";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Profile getByUserId(int userId)
    {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), userId);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                profile.getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getEmail(),
                profile.getAddress(),
                profile.getCity(),
                profile.getState(),
                profile.getZip()
        );
        return profile; // you might want to fetch and return after insert in a real app
    }

    @Override
    public void update(Profile profile)
    {
        String sql = "UPDATE profiles SET first_name=?, last_name=?, phone=?, email=?, address=?, city=?, state=?, zip=? WHERE user_id=?";
        jdbcTemplate.update(sql,
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getEmail(),
                profile.getAddress(),
                profile.getCity(),
                profile.getState(),
                profile.getZip(),
                profile.getUserId()
        );
    }

    @Override
    public void delete(int userId)
    {
        String sql = "DELETE FROM profiles WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    private Profile mapRow(java.sql.ResultSet rs) throws java.sql.SQLException
    {
        return new Profile(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip")
        );
    }
}