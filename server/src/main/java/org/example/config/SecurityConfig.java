package org.example.config;

import org.example.pojo.Admin;
import org.example.service.ICustomService;
import org.example.service.IRiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * ClassName:SecurityConfig
 * Package:org.example.config
 * Description:
 *
 * @Date:2022/8/6 20:22
 * @Author:cbb
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {



    @Autowired
    private ICustomService customService;

    @Autowired
    private IRiderService riderService;


    @Autowired
    private RestAuthorizationEntryPoint authorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //将从写的 UserDetailsService 放入配置
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return username->{
            Admin  admin =riderService.getAdminByUserName(username) ;

            if (null != admin){
                return admin;
            }else if (null == admin){
                 admin =customService.getAdminByUserName(username) ;
                 if (null != admin) {
                     return admin;
                 }
            }

            throw new UsernameNotFoundException("账户不存在，请检查，用户名或密码");
        };
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用jwt 不需要使用 csrf
        http.csrf().disable()
                //基于token 不需要 session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //开启权限访问
                .authorizeRequests()
//                //允许被访问
//                .antMatchers("/login","/logout")
//                .permitAll()
                .anyRequest()
                .authenticated()
                //动态权限配置
                .and()
                //禁用缓存
                .headers()
                .cacheControl();


        //添加 jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authorizationEntryPoint);


    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //配置 静态资源 放行
        web.ignoring().antMatchers("/login",
                "/logout","/css/**",
                "/js/**","/index.html",
                "/favicon.ico","/doc.html",
                "/webjars/**","/swagger-resources/**"
                ,"/v2/api-docs/**","/captcha","/examine/add"


        );
    }


    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return  new JwtAuthencationTokenFilter();
    }




}
