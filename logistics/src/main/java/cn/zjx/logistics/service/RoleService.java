package cn.zjx.logistics.service;

import java.util.List;

import cn.zjx.logistics.pojo.Role;
import cn.zjx.logistics.pojo.RoleExample;

public interface RoleService {
	int deleteByPrimaryKey(Long roleId);

	int insert(Role record);
	
	int updateByPrimaryKeySelective(Role record);

	List<Role> selectByExample(RoleExample example);

	Role selectByPrimaryKey(Long roleId);

}
