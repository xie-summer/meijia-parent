package com.simi.vo;

import java.util.Date;
import java.util.List;

public class UserLeaveSearchVo {
	
	private Long leaveId;
	
	private Long companyId;
	
	private Long userId;
		
	private List<Long> status;
	
	private Date startDate;
	
	private Date endDate;

	private Long startTime;

	private Long endTime;

	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Long> getStatus() {
		return status;
	}

	public void setStatus(List<Long> status) {
		this.status = status;
	}



}
