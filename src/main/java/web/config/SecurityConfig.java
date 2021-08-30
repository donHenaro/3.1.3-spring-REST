package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import web.config.handler.LoginSuccessHandler;
import web.service.UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private UserService us;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (us.listUsers().isEmpty()) {
            //Если база пользователей пуста - используем по умолчанию: [Admin:123]
            auth.inMemoryAuthentication().withUser("Admin").password("$2y$10$Oedz6pMQNyI9iLjaPn15E.SHIIplJNxNZ7PMcsspjUf5xqv2Jes7O").roles("ADMIN");
        } else {
            //При наличии в базе пользователей - аутентификация по содержимому БД
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                // указываем страницу с формой логина
                //.loginPage("/login") <-- отключаем корявую форму, используем форму по умолчанию
                //указываем логику обработки при логине
                .successHandler(loginSuccessHandler)//new LoginSuccessHandler()
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll();

        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                //.logoutSuccessUrl("/login?logout") <-- отключаем корявую форму
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/login").anonymous()
                //для Юзера
                .antMatchers("/user").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
                // защищенные Админа
                .antMatchers("/admin/**", "/modal/**", "/user/**").access("hasAnyRole('ROLE_ADMIN')").anyRequest().authenticated()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//NoOpPasswordEncoder.getInstance();
    }
}

