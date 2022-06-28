package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//开启基于方法级别细粒度权限控制。即:在controller方法上加权限注解即可：例如：@PreAuthorize("hasAuthority('role.show')")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration //声明一个配置，相当于一个xml配置。
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的默认行为
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    /**
     * 认证：
     *      1.基于内存认证方式（了解）
     *      2.基于数据库的认证方式（重点）
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //1.基于内存认证方式（了解），写死用户名称和密码，分配空的角色（即无任何权限）
        /*auth.inMemoryAuthentication()
                .withUser("lucy")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("");*/

        //2.基于数据库的认证方式（重点）
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 授权： 自定义授权操作
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //默认授权： 不登录系统，所有资源都不允许访问
        //super.configure(http);

        //自定义权限控制

        //1.设置同源资源允许访问
        http.headers().frameOptions().sameOrigin(); //同源(资源父路径一致的,协议,ip,port)资源允许访问

        //2.授权静态资源不登录允许访问
        http.authorizeRequests().antMatchers("/static/**","/login").permitAll()
                .anyRequest().authenticated();
        //3.授权自定义登录页面
        //formLogin().loginPage("/login") 表示，通过这个映射跳转到自己的登录页
        // defaultSuccessUrl("/"); 登录成功后去哪里
        http.formLogin().loginPage("/login").defaultSuccessUrl("/");
        //4.授权注销路径
        // logoutUrl("/logout")  通过这个请求路径注销系统，即销毁session.
        //logoutSuccessUrl("/login") 注销系统后去哪里
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");

        //5.关闭跨站请求伪造功能
        // <input type="hidden" name="_csrf" value="40864ca4-bd1e-49da-b8d4-e15513a7664f"/>  默认开启
        http.csrf().disable();
    }

    //声明bean对象,等价于<bean id="passwordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder">
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
