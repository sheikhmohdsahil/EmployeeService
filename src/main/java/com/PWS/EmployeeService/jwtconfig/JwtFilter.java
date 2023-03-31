package com.PWS.EmployeeService.jwtconfig;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse  httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Get JWT Token from Authorization header
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String jwtToken = null;
            if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
            }

            if (StringUtils.hasText(jwtToken) && jwtUtil.validateToken(jwtToken)) {
                String username = jwtUtil.getUsernameFromToken(jwtToken);

                // Get user details from user service or repository
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                // Create an authentication token based on the user details
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Set the authentication in the Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            else {
                System.out.println("Could not set user authentication in security context");
            }
        }

        catch (SignatureException ex){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT signature");

        }
        catch (MalformedJwtException ex){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");

        }
        catch (ExpiredJwtException ex){
            String isRefreshToken = httpServletRequest.getHeader("isRefreshToken");
            String requestURL = httpServletRequest.getRequestURL().toString();
            // allow for Refresh Token creation if following conditions are true.
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
                allowForRefreshToken(ex, httpServletRequest);
            } else
                httpServletRequest.setAttribute("exception", ex);
        }
        catch (UnsupportedJwtException ex){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported JWT token");

        }
        catch (IllegalArgumentException ex){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT claims string is empty");

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());

    }

}
