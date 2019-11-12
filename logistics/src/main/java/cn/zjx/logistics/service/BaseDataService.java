package cn.zjx.logistics.service;

import java.util.List;

import cn.zjx.logistics.pojo.BaseData;
import cn.zjx.logistics.pojo.BaseDataExample;


public interface BaseDataService {
	int deleteByPrimaryKey(Long baseId);

	int insert(BaseData record);
	
	int updateByPrimaryKeySelective(BaseData record);

	List<BaseData> selectByExample(BaseDataExample example);

	BaseData selectByPrimaryKey(Long baseId);

	List<BaseData> selectBaseDataByParentName(String parentName);

	

}
