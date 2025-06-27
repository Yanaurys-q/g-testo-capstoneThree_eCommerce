package org.yearup.data;

import org.yearup.models.Profile;

import java.util.List;

public interface ProfileDao
{
    List<Profile> getAll();
    Profile getByUserId(int userId);
    Profile create(Profile profile);
    void update(Profile profile);
    void delete(int userId);
}