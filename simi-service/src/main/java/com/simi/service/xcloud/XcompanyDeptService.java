package com.simi.service.xcloud;

import java.util.List;

import com.simi.po.model.xcloud.XcompanyDept;
import com.simi.vo.xcloud.XcompanyDeptVo;
import com.simi.vo.xcloud.company.DeptSearchVo;

public interface XcompanyDeptService {

	XcompanyDept initXcompanyDept();

	int deleteByPrimaryKey(Long id);

	int insertSelective(XcompanyDept record);

	XcompanyDept selectByPrimaryKey(Long id);

	int updateByPrimaryKey(XcompanyDept record);

	int updateByPrimaryKeySelective(XcompanyDept record);

	int insert(XcompanyDept record);

	List<XcompanyDept> selectByXcompanyId(Long xcompanyId);

	List<XcompanyDept> selectByParentId(Long parentId);

	List<XcompanyDept> selectByParentIdAndXcompanyId(Long parentId,
			Long xcompanyId);

	XcompanyDept selectByXcompanyIdAndDeptName(Long parentId, String name);

	XcompanyDept selectByXcompanyIdAndDeptId(Long companyId, Long deptId);
	
	
	List<XcompanyDept> selectBySearchVo(DeptSearchVo searchVo);
	
	XcompanyDeptVo initVo();
	
	XcompanyDeptVo transToVo(XcompanyDept dept);
	
	List<XcompanyDept> selectByListPage(DeptSearchVo searchVo,int pageNo , int pageSize);
}
