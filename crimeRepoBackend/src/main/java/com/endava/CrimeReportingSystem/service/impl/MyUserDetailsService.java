package com.endava.CrimeReportingSystem.service.impl;




import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.entity.UserPrincipal;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.repository.UsersRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private final UsersRepository usersRepository;

    // Constructor injection
    public MyUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


  
    /**
     * loading user by user email 
     */
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		 Users user = usersRepository.selectUserByUserEmail(userEmail);
	        if (user == null) {
	            
	            throw new UsernameNotFoundException("user not found");
	        }
	        
	        return new UserPrincipal(user);
	}
}
