package net.javierjimenez.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MongoAuthenticatorProvider mongoAuthenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/new_venta/**", "/categoria/**", "/products", "/login", "/contact", "/about",
						"/register", "/newregister", "/producto/**", "/imagenes/**")
				.permitAll().antMatchers("/newAdmin", "/listClients", "/addproduct", "/dashboard")
				.hasAuthority("ROLE_ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/login")
				.defaultSuccessUrl("/").permitAll().and().logout().permitAll();
	}

	/**
	 * Seguretat web no necessària per les adreces de recursos bàsics: CSS,
	 * Javascript, Imatges, tipus de lletres.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/fonts/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
		auth.authenticationProvider(mongoAuthenticationProvider);
	}
}