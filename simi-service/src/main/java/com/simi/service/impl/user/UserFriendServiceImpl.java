package com.simi.service.impl.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simi.service.user.UserFriendService;
import com.simi.service.user.UserRef3rdService;
import com.simi.service.user.UsersService;
import com.simi.vo.UserFriendSearchVo;
import com.simi.vo.user.UserFriendViewVo;
import com.simi.po.dao.user.UserFriendsMapper;
import com.simi.po.model.user.UserFriends;
import com.simi.po.model.user.UserRef3rd;
import com.simi.po.model.user.Users;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserFriendServiceImpl implements UserFriendService {

	@Autowired
	private UsersService userService;
	
	@Autowired
	private UserFriendsMapper userFriendsMapper;
	
	@Autowired
	private UserRef3rdService userRef3rdService;		

	@Override
	public int deleteByPrimaryKey(Long id) {
		return userFriendsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserFriends record) {
		return userFriendsMapper.insert(record);
	}

	@Override
	public int insertSelective(UserFriends record) {
		return userFriendsMapper.insertSelective(record);
	}

	@Override
	public UserFriends selectByPrimaryKey(Long id) {
		return userFriendsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(UserFriends record) {
		return userFriendsMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeySelective(UserFriends record) {
		return userFriendsMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public UserFriends initUserFriend() {
		UserFriends record = new UserFriends();
		record.setId(0L);
		record.setUserId(0L);
		record.setFriendId(0L);
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		return record;
	}	
	
	@Override
	public PageInfo selectByListPage(UserFriendSearchVo searchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<UserFriends> list = userFriendsMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}
	
	@Override
	public List<UserFriends> selectByUserId(Long userId) {
		List<UserFriends> result = userFriendsMapper.selectByUserId(userId);
		return result;
	}
	
	@Override
	public List<UserFriendViewVo> changeToUserFriendViewVos(List<UserFriends> userFriends) {
		List<UserFriendViewVo> result = new ArrayList<UserFriendViewVo>();
		
		if (userFriends.isEmpty()) return result;
		
		List<Long> userIds = new ArrayList<Long>();
		for (UserFriends item: userFriends) {
			if (!userIds.contains(item.getFriendId()))
				userIds.add(item.getFriendId());
		}
		
		List<Users> list = userService.selectByUserIds(userIds);
		List<UserRef3rd> userRef3Rds = userRef3rdService.selectByUserIds(userIds);
				
		UserFriends item = null;
		for (int i =0; i < userFriends.size(); i++) {
			item = userFriends.get(i);
			UserFriendViewVo vo = new UserFriendViewVo();
			
			for (Users u : list) {
				if (u.getId().equals(item.getFriendId())) {
					vo.setFriendId(item.getFriendId());
					vo.setName(u.getName());
					vo.setSex(u.getSex());
					
					String headImg = u.getHeadImg();
					if (headImg.indexOf("http://img.51xingzheng.cn") >= 0) {
						headImg = headImg + "?w=100&h=100";
					}
					
					vo.setHeadImg(headImg);
					vo.setMobile(u.getMobile());
					break;
				}
			}
			
			for(UserRef3rd ur : userRef3Rds) {
				if (ur.getUserId().equals(item.getFriendId())) {
					vo.setImUsername(ur.getUsername());
					break;
				}
			}
			result.add(vo);
		}
		
		
		return result;
	}	
	
	@Override
	public UserFriends selectByIsFirend(UserFriendSearchVo searchVo) {
		UserFriends result = userFriendsMapper.selectByIsFirend(searchVo);
		return result;
	}	
	
	@Override
	public void addFriends(Users u, Users friendUser) {
		
		UserFriendSearchVo searchVo = new UserFriendSearchVo();
		searchVo.setUserId(u.getId());
		searchVo.setFriendId(friendUser.getId());
		UserFriends userFriend = this.selectByIsFirend(searchVo);		
		if (userFriend == null) {
			userFriend = this.initUserFriend();
			userFriend.setUserId(u.getId());
			userFriend.setFriendId(friendUser.getId());
			userFriend.setAddTime(TimeStampUtil.getNowSecond());
			userFriend.setUpdateTime(TimeStampUtil.getNowSecond());
			this.insert(userFriend);
		}
		
		searchVo = new UserFriendSearchVo();
		searchVo.setUserId(friendUser.getId());
		searchVo.setFriendId(u.getId());
		userFriend = this.selectByIsFirend(searchVo);
		if (userFriend == null) {
			userFriend = this.initUserFriend();
			userFriend.setUserId(friendUser.getId());
			userFriend.setFriendId(u.getId());
			userFriend.setAddTime(TimeStampUtil.getNowSecond());
			userFriend.setUpdateTime(TimeStampUtil.getNowSecond());
			this.insert(userFriend);
		}
	}
	
}