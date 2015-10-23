package com.simi.service.impl.user;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simi.po.dao.user.TagUsersMapper;
import com.simi.po.dao.user.TagsMapper;
import com.simi.po.model.user.TagUsers;
import com.simi.po.model.user.Tags;
import com.simi.service.user.TagsService;
import com.simi.service.user.TagsUsersService;
import com.sun.javadoc.Tag;
@Service
public class TagsUsersServiceImpl implements TagsUsersService {


	@Autowired
	private TagUsersMapper tagUsersMapper;

	@Override
	public int insertByTagUsers(TagUsers tagUsers) {
		
		return tagUsersMapper.insertSelective(tagUsers);
	}


	
}