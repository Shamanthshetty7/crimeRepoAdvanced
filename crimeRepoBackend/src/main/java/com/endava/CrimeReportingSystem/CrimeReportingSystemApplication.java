package com.endava.CrimeReportingSystem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;import org.hibernate.type.internal.UserTypeJavaTypeWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.endava.CrimeReportingSystem.entity.InvestigationCentre;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.repository.InvestigationCentreRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;

@SpringBootApplication
public class CrimeReportingSystemApplication {

	public static void main(String[] args) {
		 SpringApplication.run(CrimeReportingSystemApplication.class, args);
		
		
		
		
		
		
		
//		InvestigationCentreRepository investigationsCentreRepository=context.getBean(InvestigationCentreRepository.class);
//		InvestigationCentre investigationCentre=new InvestigationCentre();
//		investigationCentre.setInvestigationCentreName("Cyber CrimeRepo");
//		investigationCentre.setInvestigationCentreCode("abc#123");
//		investigationCentre.setCreatedAt(LocalDateTime.now());
//		investigationsCentreRepository.save(investigationCentre);
		
	}

}
