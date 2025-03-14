package com.endava.CrimeReportingSystem.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;
	
	public String projectUrl="/crime-reporting-system";

	@Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.csrf(customizer -> customizer.disable())
				.cors(cors->cors.configurationSource(request->{
					var corsConfig=new org.springframework.web.cors.CorsConfiguration();
					corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
					corsConfig.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
					corsConfig.setAllowedHeaders(List.of("Content-Type","Authorization"));
					return corsConfig;
					
				}))
				.authorizeHttpRequests(request -> request
						.requestMatchers(projectUrl+"/users/userLogin", projectUrl+"/users/userRegister",projectUrl+"/reports/getAllReports",
								projectUrl+"/APIcall/coordinates",projectUrl+"/news/getAllNews",projectUrl+"/APIcall/emergency-services/{lat}/{lon}",
								projectUrl+"/notifications/sendNotification",projectUrl+"/comments/getAllComments/{reportId}",projectUrl+"/notifications/emergencyAlertNotification"
								,projectUrl+"/notifications/recieveAlertNotifications",projectUrl+"/APIcall/verifyEmail/{email}").permitAll()
						
						.requestMatchers(HttpMethod.GET, projectUrl + "/users/getAllUsers").hasRole("Investigator")
                        .requestMatchers(HttpMethod.DELETE, projectUrl + "/users/**").hasRole("Investigator")
                       
                        .requestMatchers(HttpMethod.GET, projectUrl + "/user-profile/KycStatus/{userId}").hasRole("Informant")
                        .requestMatchers(HttpMethod.DELETE, projectUrl + "/user-profile/{userProfileId}").hasRole("Investigator")
                        .requestMatchers(HttpMethod.PUT, projectUrl + "/reports/vote/**").hasRole("Informant")
                        .requestMatchers(HttpMethod.GET, projectUrl + "/kyc-application/getAllKycApplications").hasRole("Investigator")
                        .requestMatchers(HttpMethod.GET, projectUrl + "/dashboard/*8").hasRole("Investigator")
                        .requestMatchers(HttpMethod.GET, projectUrl + "/investigation-centre/{investigationCentreCode}").hasRole("Investigator")
                        .requestMatchers(HttpMethod.POST, projectUrl + "/news/**").hasRole("Investigator")
                        .requestMatchers(HttpMethod.PUT, projectUrl + "/news/**").hasRole("Investigator")
                        .requestMatchers(HttpMethod.DELETE, projectUrl + "/news/**").hasRole("Investigator")
                        .requestMatchers(HttpMethod.POST, projectUrl + "/emergency").hasRole("Investigator")
                        .requestMatchers(HttpMethod.DELETE, projectUrl + "/emergency/**").hasRole("Investigator")
                        .requestMatchers(HttpMethod.PUT, projectUrl + "/emergency").hasRole("Investigator")
                        .requestMatchers(HttpMethod.GET, projectUrl + "/emergency").permitAll()
                
                        
                        .anyRequest()
						.authenticated())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();

	}

	@Bean
	 AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();

	}
	
	@Bean
	 PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	
}

}