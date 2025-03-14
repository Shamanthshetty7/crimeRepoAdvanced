package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.CrimeReportingSystem.entity.KycApplication;
import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.enums.ReportStatus;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.repository.KycApplicationRepository;
import com.endava.CrimeReportingSystem.repository.ReportsRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private ReportsRepository reportsRepository;

	@Mock
	private KycApplicationRepository kycApplicationRepository;

	@InjectMocks
	private DashboardServiceImpl dashboardService;

	private Users activeUser;
	private Users blockedUser;
	private Reports newReport;
	private KycApplication underVerificationKycApplication;

	@BeforeEach
	void setUp() {
		activeUser = new Users();
		activeUser.setUserId(1);
		activeUser.setIsActive(true);
		activeUser.setUserType(UserType.Informant);

		blockedUser = new Users();
		blockedUser.setUserId(2);
		blockedUser.setIsActive(false);
		blockedUser.setUserType(UserType.Informant);

		newReport = new Reports();
		newReport.setReportId(1);
		newReport.setReportStatus(ReportStatus.newReport);
		newReport.setCreatedAt(LocalDateTime.now());

		underVerificationKycApplication = new KycApplication();
		underVerificationKycApplication.setKycId(1);
		underVerificationKycApplication.setKycVerificationStatus(KycVerificationStatus.underVerification);
	}

	@Test
	void testGetAllDatCount_Success() {
		List<Users> usersList = new ArrayList<>();
		usersList.add(activeUser);
		usersList.add(blockedUser);

		List<Reports> reportsList = new ArrayList<>();
		reportsList.add(newReport);

		List<KycApplication> kycApplicationsList = new ArrayList<>();
		kycApplicationsList.add(underVerificationKycApplication);

		when(usersRepository.findByUserType(UserType.Informant)).thenReturn(usersList);
		when(reportsRepository.findAll()).thenReturn(reportsList);
		when(kycApplicationRepository.findAll()).thenReturn(kycApplicationsList);

		ApiGenericResponse<?> response = dashboardService.getAllDatCount();
		Map<String, Integer> dataCounts = (Map<String, Integer>) response.getData();

		assertNotNull(dataCounts);
		assertEquals(1, dataCounts.get("activeUsers"));
		assertEquals(1, dataCounts.get("blockedUser"));
		assertEquals(1, dataCounts.get("newReport"));
		assertEquals(1, dataCounts.get("underVerificationKyc"));
	}

	@Test
	void testGetAllReportByMonth_Success() {
		List<Reports> reportsList = new ArrayList<>();
		reportsList.add(newReport);

		when(reportsRepository.findAll()).thenReturn(reportsList);

		ApiGenericResponse<?> response = dashboardService.getAllReportByMonth(LocalDateTime.now().getYear());
		Map<String, Long> monthlyReportCounts = (Map<String, Long>) response.getData();

		assertNotNull(monthlyReportCounts);
		assertEquals(1, monthlyReportCounts.get(LocalDateTime.now().getMonth().toString()));
	}

	@Test void testGetAllReportByMonth_NoReports() 
	{
		List<Reports> reportsList = new ArrayList<>();
		when(reportsRepository.findAll()).thenReturn(reportsList); 
		ApiGenericResponse<?> response = dashboardService.getAllReportByMonth(LocalDateTime.now().getYear()); 
		Map<String, Long> monthlyReportCounts = (Map<String, Long>) response.getData(); 
		
		assertNotNull(monthlyReportCounts);
		for (Month month : Month.values()) {
			assertEquals(0, monthlyReportCounts.get(month.toString())); 
			} 
		}
}
