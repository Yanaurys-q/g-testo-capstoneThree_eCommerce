package org.yearup.controllers;

import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private UserDao userDao;

    private int getCurrentUserId()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.findByUsername(username).getId();
    }

    @GetMapping
    public Profile getProfile()
    {
        return profileDao.getByUserId(getCurrentUserId());
    }

    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile)
    {
        profile.setUserId(getCurrentUserId());
        profileDao.update(profile);
        return profile;
    }

}