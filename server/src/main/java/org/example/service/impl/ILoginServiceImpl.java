package org.example.service.impl;

import org.example.common.R;
import org.example.pojo.Admin;
import org.example.service.ICustomService;
import org.example.service.ILoginService;
import org.example.service.IRiderService;
import org.example.utlis.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:ILoginServiceImpl
 * Package:org.example.service.impl
 * Description:
 *
 * @Date:2022/8/6 21:03
 * @Author:cbb
 */
@Service
public class ILoginServiceImpl implements ILoginService {

    @Autowired
    private ICustomService customService;

    @Autowired
    private IRiderService riderService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    public R login(String username, String password) {

        String s1 = username;

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Admin admin =  (Admin)userDetails;

        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return R.error("用户名或者密码不正确");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);



        return R.success("登录成功", tokenMap);
    }
}
