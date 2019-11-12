package cn.zjx.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zjx.logistics.mapper.RoleMapper;
import cn.zjx.logistics.pojo.Role;
import cn.zjx.logistics.pojo.RoleExample;
import cn.zjx.logistics.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public int deleteByPrimaryKey(Long roleId) {
		return roleMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public int insert(Role record) {
		return roleMapper.insert(record);
	}


	@Override
	public List<Role> selectByExample(RoleExample example) {
		return roleMapper.selectByExample(example);
	}

	@Override
	public Role selectByPrimaryKey(Long roleId) {
		return roleMapper.selectByPrimaryKey(roleId);
	}

	@Override
	public int updateByPrimaryKeySelective(Role record) {
		return roleMapper.updateByPrimaryKeySelective(record);
	}


}
