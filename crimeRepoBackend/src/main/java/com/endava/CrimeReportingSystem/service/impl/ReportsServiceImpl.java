package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.ReportVoteConstants;
import com.endava.CrimeReportingSystem.constants.ReportsConstants;
import com.endava.CrimeReportingSystem.entity.Notifications;
import com.endava.CrimeReportingSystem.entity.ReportVote;
import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;
import com.endava.CrimeReportingSystem.enums.ReportStatus;
import com.endava.CrimeReportingSystem.mapper.ReportsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.NotificationsRepository;
import com.endava.CrimeReportingSystem.repository.ReportVoteRepository;
import com.endava.CrimeReportingSystem.repository.ReportsRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.ReportsService;

@Service
public class ReportsServiceImpl implements ReportsService{

    private final ReportsRepository reportsRepository;
    private final NotificationsRepository notificationsRepository;
    private final UsersRepository usersRepository;
    private final ReportsMapper reportsMapper;
    private final ReportVoteRepository reportVoteRepository;
    private final UsersMapper usersMapper;

    // Constructor injection
    public ReportsServiceImpl(ReportsRepository reportsRepository,
                              NotificationsRepository notificationsRepository,
                              UsersRepository usersRepository,
                              ReportsMapper reportsMapper,
                              ReportVoteRepository reportVoteRepository,
                              UsersMapper usersMapper) {
        this.reportsRepository = reportsRepository;
        this.notificationsRepository = notificationsRepository;
        this.usersRepository = usersRepository;
        this.reportsMapper = reportsMapper;
        this.reportVoteRepository = reportVoteRepository;
        this.usersMapper = usersMapper;
    }



	/** 
	 * Method to fetch a report by its ID.
	 * It returns the report data along with the associated user information.
	 */
	@Override
	public ApiGenericResponse<ReportsDTO> getReportById(int reportId) {
		Optional<Reports> report=reportsRepository.findById(reportId);
		ApiGenericResponse<ReportsDTO> response=new ApiGenericResponse<>(null,null);
		if(report.isEmpty()) {
			response.setMessage(ReportsConstants.ERROR_NO_REPORTS_FOUND.getMessage());
			return response;
		}
		ReportsDTO reportsDTO=reportsMapper.reportToReportDTO(report.get());
		reportsDTO.setUser(usersMapper.usersToUsersDTO(report.get().getUser()));
		response.setData(reportsDTO);
		return response;
	}

	/** 
	 * Method to fetch all reports and return them as a list of ReportsDTO.
	 * Sorts the reports based on the net vote count (upvotes - downvotes).
	 */
	@Override
	public List<ReportsDTO> getAllReports() {
		List<Reports> reportsList = reportsRepository.findAll();

		List<ReportsDTO> reportsDtoList = reportsList.stream().map((report) -> {

			ReportsDTO reportDTO = reportsMapper.reportToReportDTO(report);
			if (report.getReportImage() != null) {
				String base64Image = Base64.getEncoder().encodeToString(report.getReportImage());
				reportDTO.setReportImage(CommonConstants.BASE64_IMAGE_JPEG_PREFIX + base64Image);
			}
			reportDTO.setUser(usersMapper.usersToUsersDTO(report.getUser()));
			return reportDTO;
		}).collect(Collectors.toList());

		// Sort reports based on net votes (upvotes - downvotes)
		reportsDtoList.sort((report1, report2) -> 
		Integer.compare(
				(report2.getReportUpvoteCount() - report2.getReportDownvoteCount()),
				(report1.getReportUpvoteCount() - report1.getReportDownvoteCount())
				)
				);
		return reportsDtoList;
	}


	/** 
	 * Method to save a new report.
	 * Sets initial values such as creation time, update time, and report status.
	 */
	@Override
	public ApiGenericResponse<ReportsDTO> saveReport(ReportsDTO reportsDTO) {
		Reports report=reportsMapper.reportDtoToReport(reportsDTO);
		report.setUser(usersMapper.usersDTOtoUsers(reportsDTO.getUser()) );
		ApiGenericResponse<ReportsDTO> response=new ApiGenericResponse<>(null,null);
		report.setCreatedAt(LocalDateTime.now());
		report.setUpdatedAt(LocalDateTime.now());
		report.setIsActive(true);
		report.setReportStatus(ReportStatus.newReport);
		Reports savedReport=reportsRepository.save(report);
		//sending notiication to the users who are all in the location where report is done

		List <Users> matcheduserList=usersRepository.findUsersByLiveLocationContainingIgnoreCase(reportsDTO.getReportLocation(),reportsDTO.getUser().getUserId());
		List<Notifications> notifications =matcheduserList.stream().map((user)->{
			Notifications notification=new Notifications();
			notification.setIsActive(true);
			notification.setCreatedAt(LocalDateTime.now());
			notification.setUpdatedAt(LocalDateTime.now());
			notification.setNotificationTitle(ReportsConstants.SUCCESS_NEW_REPORT_NEAR_YOUR_AREA.getMessage());
			notification.setNotificationMessage(reportsDTO.getReportTitle()+" is reported near your area");
			notification.setUser(user);

			return notification;
		}).toList();
		notificationsRepository.saveAll(notifications);

		response.setData(reportsMapper.reportToReportDTO(savedReport));
		response.setMessage(ReportsConstants.SUCCESS_REPORT_SAVED.getMessage());
		return response;
	}


	/** 
	 * Method to save an image for an existing report.
	 * Updates the report with the new image.
	 */
	@Override
	public ApiGenericResponse<ReportsDTO> saveReportImage(int reportId, MultipartFile reportImage) throws Exception {
		ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
		Optional<Reports> updatingReport = reportsRepository.findById(reportId);


		if (updatingReport.isEmpty()) {
			response.setMessage(ReportsConstants.ERROR_NO_REPORTS_FOUND.getMessage());
			return response;
		}

		updatingReport.get().setReportImage(reportImage.getBytes());

		Reports savedReport = reportsRepository.save(updatingReport.get());

		response.setData(reportsMapper.reportToReportDTO(savedReport));
		response.setMessage(ReportsConstants.SUCCESS_REPORT_IMAGE_SAVED.getMessage());

		return response;
	}


	/** 
	 * Method to update a report based on the provided data.
	 * It allows updates to report details, including its image.
	 */
	@Override
	public ApiGenericResponse<ReportsDTO> updateReport(ReportsDTO reportsDTO) {

		Reports report=reportsMapper.reportDtoToReport(reportsDTO);
		if (reportsDTO.getReportImage() == null) {
			report.setReportImage(reportsRepository.findReportImageByReportId(reportsDTO.getReportId())); 
		}

		report.setUser(usersMapper.usersDTOtoUsers(reportsDTO.getUser()) );
		ApiGenericResponse<ReportsDTO> response=new ApiGenericResponse<>(null,null);
		report.setUpdatedAt(LocalDateTime.now());
		Reports savedReport=reportsRepository.save(report);
		response.setData(reportsMapper.reportToReportDTO(savedReport));
		response.setMessage(ReportsConstants.SUCCESS_REPORT_UPDATED.getMessage());
		return response;
	}

	/** 
	 * Method to logically delete a report by setting its status to "removed".
	 * This is achieved by marking the report as inactive.
	 */
	@Override
	public Boolean deleteReport(int reportId) {
		Optional<Reports> deletingReport=reportsRepository.findById(reportId);
		if(!deletingReport.isEmpty()) {
			deletingReport.get().setIsActive(false);
			deletingReport.get().setReportStatus(ReportStatus.removedReport);
			
		Optional.ofNullable(reportsRepository.save(deletingReport.get())).orElseThrow(()->new RuntimeException(ReportsConstants.ERROR_SAVING_REPORT.getMessage()));
			return true;
		}
		return false;
	}

	/** 
	 * Method to update the vote count for a report (either upvote or downvote).
	 * It also saves the vote details in the ReportVote table.
	 */
	@Override
	public ApiGenericResponse<ReportsDTO> updateReportVote(int reportId, ReportsDTO reportsDTO) {
		ApiGenericResponse<ReportsDTO> response=new ApiGenericResponse<>(null,null);
		Optional<Reports> existingReport=reportsRepository.findById(reportId);
		if(existingReport.isEmpty()) {
			response.setMessage("No report found with report id "+ reportId);
			return response;
		}

		ReportVote reportVote=new ReportVote();
		// Update the report's vote counts based on the vote status
		if(reportsDTO.getVoteStatus().equals("up")) {
			existingReport.get().setReportUpvoteCount(existingReport.get().getReportUpvoteCount()+1);
			reportVote.setUpVoted(true);

		}else {
			existingReport.get().setReportDownvoteCount(existingReport.get().getReportDownvoteCount()+1);
			reportVote.setDownVoted(true);
		}
		Reports updatedReport=Optional.of(reportsRepository.save(existingReport.get())).orElseThrow(()->new RuntimeException(ReportVoteConstants.ERROR_UPDATING_VOTE_STATUS.getMessage()));
		reportVote.setReport(updatedReport);
		reportVote.setUser(usersMapper.usersDTOtoUsers(reportsDTO.getUser()));
		reportVote.setCreatedAt(LocalDateTime.now());
		reportVote.setUpdatedAt(LocalDateTime.now());
		//saving vote to reportvote table
		Optional.of(reportVoteRepository.save(reportVote)).orElseThrow(()->new RuntimeException(ReportVoteConstants.ERROR_UPDATING_VOTE_STATUS.getMessage()));
	    response.setData(reportsMapper.reportToReportDTO(updatedReport));
	
		return response;
	}

}
