package cn.zjx.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zjx.logistics.mapper.UserMapper;
import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.pojo.UserExample;
import cn.zjx.logistics.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public int deleteByPrimaryKey(Long userId) {
		return userMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(User record) {
		return userMapper.insert(record);
	}


	@Override
	public List<User> selectByExample(UserExample example) {
		return userMapper.selectByExample(example);
	}

	@Override
	public User selectByPrimaryKey(Long userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public User selectUserByUsername(String username) {
		
		return userMapper.selectUserByUsername(username);
	}


}
