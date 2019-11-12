package cn.zjx.logistics.service;

import java.util.List;

import cn.zjx.logistics.pojo.Permission;
import cn.zjx.logistics.pojo.PermissionExample;

public interface PermissionService {
	int deleteByPrimaryKey(Long permissionId);

	int insert(Permission record);
	
	int updateByPrimaryKeySelective(Permission record);

	List<Permission> selectByExample(PermissionExample example);

	Permission selectByPrimaryKey(Long permissionId);

	List<String> serlectPermissionByIds(List<Long> permissionIdsList);

	

}
