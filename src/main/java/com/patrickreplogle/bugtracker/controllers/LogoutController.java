package com.patrickreplogle.bugtracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;


@Controller
public class LogoutController
{

    @Autowired
    private TokenStore tokenStore;

    // logout user and remove token from tokenStore
    @RequestMapping(value = {"/oauth/revoke-token", "/logout"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void logoutSelf(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null)
        {
            // find the token
            String tokenValue = authHeader.replace("Bearer",
                    "")
                    .trim();
            System.out.println(tokenValue);
            // and remove it!
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
    }
}
