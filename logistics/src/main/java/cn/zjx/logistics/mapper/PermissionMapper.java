package cn.zjx.logistics.mapper;

import cn.zjx.logistics.pojo.Permission;
import cn.zjx.logistics.pojo.PermissionExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long permissionId);

    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectByExample(PermissionExample example);

    Permission selectByPrimaryKey(Long permissionId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

	List<String> serlectPermissionByIds(@Param("permissionIds")List<Long> permissionIdsList);
}