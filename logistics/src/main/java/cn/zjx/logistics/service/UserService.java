package cn.zjx.logistics.service;

import java.util.List;

import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.pojo.UserExample;

public interface UserService {
	int deleteByPrimaryKey(Long userId);

	int insert(User record);
	
	int updateByPrimaryKeySelective(User record);

	List<User> selectByExample(UserExample example);

	User selectByPrimaryKey(Long userId);

	User selectUserByUsername(String username);

	

}
