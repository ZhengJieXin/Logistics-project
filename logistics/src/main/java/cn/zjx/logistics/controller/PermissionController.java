package cn.zjx.logistics.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zjx.logistics.mo.MessageObject;
import cn.zjx.logistics.pojo.Role;
import cn.zjx.logistics.pojo.RoleExample;
import cn.zjx.logistics.pojo.Permission;
import cn.zjx.logistics.pojo.PermissionExample;
import cn.zjx.logistics.pojo.PermissionExample.Criteria;
import cn.zjx.logistics.service.RoleService;
import cn.zjx .logistics.service.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/permissionPage")
	public String permissionPage() {
		
		return "permissionPage";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("permission:list")
	public PageInfo<Permission> list(@RequestParam(defaultValue = "1")Integer pageNum,
								@RequestParam(defaultValue = "10")Integer pageSize,
								String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		//条件查询对象
		PermissionExample example = new PermissionExample();
		
		
		if(StringUtils.isNotBlank(keyword)) {
			//创建条件限制对象
			Criteria criteria = example.createCriteria();
			criteria.andNameLike("%"+keyword+"%");
		}
		
		
		List<Permission> permissions = permissionService.selectByExample(example );
		for (Permission permission : permissions) {
			System.out.println(permission);
		}
		
		PageInfo<Permission> pageInfo = new PageInfo<Permission>(permissions);
		
		return pageInfo;
	}
	@RequestMapping("/getAllPermissions")
	@ResponseBody
	public List<Permission> getAllPermissions() {
		PermissionExample example=new PermissionExample();
		List<Permission> permissions = permissionService.selectByExample(example);
		
		return permissions;
	}
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("permission:delete")
	public MessageObject delete(Long permissionId) {
		
		MessageObject mo = new MessageObject(0, "删除数据失败，请联系管理员");
		
		
		//删除之前先判断 当前被删除的权限是否还有子权限
		
		PermissionExample example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(permissionId);
		
		List<Permission> children = permissionService.selectByExample(example);
		
		//有子权限，不能删除
		if(children.size() > 0) {
			mo = new MessageObject(0, "此权限还有子权限，不能删除");
		}else {
			//执行删除操作
			int row = permissionService.deleteByPrimaryKey(permissionId);
			if(row == 1) {
				mo = new MessageObject(1, "删除数据成功");
			}
		}
		
		
		
		return mo;
	}
	//编辑功能
	@RequestMapping("/edit")
	public String edit(Model m,Long permissionId) {
		
		//根据id查询出Permission对象，以供修改的回显
		if(permissionId !=null) {
			Permission permission = permissionService.selectByPrimaryKey(permissionId);
			m.addAttribute("permission", permission);
		}
		
		
		//查询所有的权限作为父权限选择
		PermissionExample example = new PermissionExample();
		List<Permission> permissions = permissionService.selectByExample(example );
		m.addAttribute("permissions", permissions);
		
		return "permissionEdit";
	}
	
	//检查权限是否存在
	@RequestMapping("/checkPermissionName")
	@ResponseBody
	public boolean checkPermissionname(String permissionname) {
		
		PermissionExample example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(permissionname);
		
		List<Permission> permissions = permissionService.selectByExample(example);
		
		return permissions.size() > 0 ? false : true; 
	}
	
	
	
	
	//新增操作
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("permission:insert")
	public MessageObject insert(Permission permission) {
		
		MessageObject mo = new MessageObject(0, "新增数据失败，请联系管理员");
		
		//执行新增操作
		int row = permissionService.insert(permission);
		if(row == 1) {
			mo = new MessageObject(1, "新增数据成功");
		}
		
		return mo;
	}
	
	//修改操作
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("permission:update")
	public MessageObject update(Permission permission) {
		
		MessageObject mo = new MessageObject(0, "修改数据失败，请联系管理员");
		
		//执行修改操作
		int row = permissionService.updateByPrimaryKeySelective(permission);
		if(row == 1) {
			mo = new MessageObject(1, "修改数据成功");
		}
		return mo;
	}	
}
