package com.avit.collegemanagementsystem.security.services;

import com.avit.collegemanagementsystem.model.Faculty.Faculty;
import com.avit.collegemanagementsystem.model.User.User;
import com.avit.collegemanagementsystem.repository.Faculty.FacultyRepository;
import com.avit.collegemanagementsystem.repository.User.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FacultyDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyDetailsServiceImpl.class);

    @Autowired
    private FacultyRepository facultyRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Faculty user = facultyRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+ username));
        logger.debug("UserDetails data:{}", user);
        return FacultyDetailsImpl.build(user);
    }
}
