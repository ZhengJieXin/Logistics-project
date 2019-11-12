package cn.zjx.logistics.mapper;

import cn.zjx.logistics.pojo.BaseData;
import cn.zjx.logistics.pojo.BaseDataExample;
import java.util.List;

public interface BaseDataMapper {
    int deleteByPrimaryKey(Long baseId);

    int insert(BaseData record);

    int insertSelective(BaseData record);

    List<BaseData> selectByExample(BaseDataExample example);

    BaseData selectByPrimaryKey(Long baseId);

    int updateByPrimaryKeySelective(BaseData record);

    int updateByPrimaryKey(BaseData record);

	List<BaseData> selectBaseDataByParentName(String parentName);
}