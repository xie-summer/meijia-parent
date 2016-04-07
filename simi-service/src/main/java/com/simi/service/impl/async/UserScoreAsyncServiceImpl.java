package com.simi.service.impl.async;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.meijia.utils.TimeStampUtil;
import com.simi.po.model.card.Cards;
import com.simi.po.model.user.UserDetailScore;
import com.simi.po.model.user.Users;
import com.simi.po.model.xcloud.Xcompany;
import com.simi.service.async.UserScoreAsyncService;
import com.simi.service.card.CardService;
import com.simi.service.user.UserDetailScoreService;
import com.simi.service.user.UsersService;
import com.simi.service.xcloud.XCompanyService;
import com.simi.utils.CardUtil;
import com.simi.vo.card.CardSearchVo;
import com.simi.vo.xcloud.CompanySearchVo;

@Service
public class UserScoreAsyncServiceImpl implements UserScoreAsyncService {

	@Autowired
	public UsersService usersService;
	
	@Autowired
	public UserDetailScoreService userDetailScoreService;
	
	@Autowired
	private XCompanyService xCompanyService;
	
	@Autowired
	private CardService cardService;
	
	/**
	 * 异步积分明细记录
	 */
	@Async
	@Override
	public Future<Boolean> sendScore(Long userId, Integer score, String action, String params, String remarks) {
		
		Users u = usersService.selectByPrimaryKey(userId);
		
		if (u == null) return new AsyncResult<Boolean>(true);
		
		//记录积分明细
		UserDetailScore record = userDetailScoreService.initUserDetailScore();
		record.setUserId(userId);
		record.setMobile(u.getMobile());
		record.setScore(score);
		record.setAction(action);
		record.setParams(params);
		record.setRemarks(remarks);
		
		userDetailScoreService.insert(record);
		
		//更新总积分
		u.setScore(u.getScore() + score);
		usersService.updateByPrimaryKeySelective(u);
		
		return new AsyncResult<Boolean>(true);
	}
	
	/**
	 * 异步积分明细记录，判断创建公司每日上限
	 */
	@Async
	@Override
	public Future<Boolean> sendScoreCompany(Long userId, Long companyId) {
		
		Users u = usersService.selectByPrimaryKey(userId);
		
		if (u == null) return new AsyncResult<Boolean>(true);
		
		String mobile = u.getMobile();
		Long startTime = TimeStampUtil.getBeginOfToday();
		Long endTime = TimeStampUtil.getEndOfToday();
		
		CompanySearchVo searchVo = new CompanySearchVo();
		searchVo.setUserName(mobile);
		searchVo.setStartTime(startTime);
		searchVo.setEndTime(endTime);
		
		List<Xcompany> rs = xCompanyService.selectBySearchVo(searchVo);
		if (rs.size() > 50) return new AsyncResult<Boolean>(true);
		
		Integer score = 30;
		//记录积分明细
		UserDetailScore record = userDetailScoreService.initUserDetailScore();
		record.setUserId(userId);
		record.setMobile(u.getMobile());
		record.setScore(score);
		record.setAction("company_reg");
		record.setParams(companyId.toString());
		record.setRemarks("创建企业/团队");
		
		userDetailScoreService.insert(record);
		
		//更新总积分
		u.setScore(u.getScore() + score);
		usersService.updateByPrimaryKeySelective(u);
		
		return new AsyncResult<Boolean>(true);
	}

	/**
	 * 异步积分明细记录，判断创建卡片每日上限
	 */
	@Async
	@Override
	public Future<Boolean> sendScoreCard(Long userId, Long cardId, Short cardType) {
		
		Users u = usersService.selectByPrimaryKey(userId);
		
		if (u == null) return new AsyncResult<Boolean>(true);
		
		Long startTime = TimeStampUtil.getBeginOfToday();
		Long endTime = TimeStampUtil.getEndOfToday();
		
		CardSearchVo searchVo = new CardSearchVo();
		searchVo.setUserId(userId);
		searchVo.setCardType(cardType);
		searchVo.setStartTime(startTime);
		searchVo.setEndTime(endTime);
		
		List<Cards> rs = cardService.selectBySearchVo(searchVo);
		if (rs.size() > 10) return new AsyncResult<Boolean>(true);
		
		Integer score = 1;
		
		String cardTypeName = CardUtil.getCardTypeName(cardType);
		String action = CardUtil.getCardAction(cardType);
		//记录积分明细
		UserDetailScore record = userDetailScoreService.initUserDetailScore();
		record.setUserId(userId);
		record.setMobile(u.getMobile());
		record.setScore(score);
		record.setAction(action);
		record.setParams(cardId.toString());
		record.setRemarks(cardTypeName);
		
		userDetailScoreService.insert(record);
		
		//更新总积分
		u.setScore(u.getScore() + score);
		usersService.updateByPrimaryKeySelective(u);
		
		return new AsyncResult<Boolean>(true);
	}
	
}
