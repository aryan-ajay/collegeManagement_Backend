package com.avit.collegemanagementsystem.security.jwt;

import com.avit.collegemanagementsystem.security.services.FacultyDetailsServiceImpl;
import com.avit.collegemanagementsystem.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private FacultyDetailsServiceImpl facultyDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
//        try {
//            String jwt = parseJwt(request);
//            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
//                String username = jwtUtils.getUserNameFromJwtToken(jwt);
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
//                        null,
//                userDetails.getAuthorities());
//
//                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
//
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }catch (Exception e) {
//            logger.error("Cannot set user authentication: {}", e);
//        }

        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                JwtResponse jwtResponse = jwtUtils.validateJwtToken(jwt);
                if (jwtResponse.isValid()) {
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UserDetails facultyDetails = facultyDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authentication2 = new UsernamePasswordAuthenticationToken(
                            facultyDetails, null, facultyDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    authentication2.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication2);
                } else if ("JWT token is expired".equals(jwtResponse.getMessage())) {
                    logger.error("JWT token is expired");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired");
                    return;  // Do not proceed with the filter chain
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, jwtResponse.getMessage());
                    return;  // Do not proceed with the filter chain
                }
            }
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired");
            return;  // Do not proceed with the filter chain

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnExpected Exception Occured , Please Login again");
            return;
        }

        logger.debug("Proceeding with filter chain.");
        filterChain.doFilter(request, response);
        logger.debug("Finished filter chain.");
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }


    // this method is for JWT Cookies based authetication
//    private String parseJwt(HttpServletRequest request) {
//        String jwt = jwtUtils.getJwtFromCookies(request);
//        logger.debug("AuthTokenFilter.java: {}", jwt);
//        return jwt;
//    }
}
