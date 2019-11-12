package cn.zjx.logistics.controller;


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
import cn.zjx.logistics.pojo.RoleExample.Criteria;
import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.pojo.UserExample;
import cn.zjx.logistics.service.RoleService;
import cn.zjx.logistics.service.UserService;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/rolePage")
	public String rolePage() {

		return "rolePage";
	}
	

	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("role:list")
	public PageInfo<Role> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String keyword) {

		PageHelper.startPage(pageNum, pageSize);

		// 条件查询对象
		RoleExample example = new RoleExample();

		if (StringUtils.isNotBlank(keyword)) {
			// 创建条件限制对象
			Criteria criteria = example.createCriteria();
			criteria.andRolenameLike("%" + keyword + "%");
		}

		List<Role> roles = roleService.selectByExample(example);
		for (Role role : roles) {
			System.out.println(role);
		}

		PageInfo<Role> pageInfo = new PageInfo<Role>(roles);

		return pageInfo;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("role:delete")
	public MessageObject delete(Long roleId) {
		
		MessageObject mo = new MessageObject(0, "删除数据失败，请联系管理员");
		//删除角色之前先根据要被删除的角色去用户表中查询是否还拥有此角色的用户，如果有，不能删除
		
		UserExample example = new UserExample();
		cn.zjx.logistics.pojo.UserExample.Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		
		List<User> users = userService.selectByExample(example );
		
		if(users.size() > 0) {
			mo = new MessageObject(0, "此角色还拥有用户，不能直接删除");
		}else {
			// 执行删除操作
			int row = roleService.deleteByPrimaryKey(roleId);
			if (row == 1) {
				mo = new MessageObject(1, "删除数据成功");
			}

		}
		

		

		
		return mo;
	}

	// 编辑功能
	@RequestMapping("/edit")
	public String edit(Model m, Long roleId) {

		// 根据id查询出Role对象，以供修改的回显
		if (roleId != null) {
			Role role = roleService.selectByPrimaryKey(roleId);
			m.addAttribute("role", role);
		}


		return "roleEdit";
	}

	// 检查权限是否存在
	@RequestMapping("/checkRolename")
	@ResponseBody
	
	public boolean checkRolename(String rolename) {

		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRolenameEqualTo(rolename);

		List<Role> roles = roleService.selectByExample(example);

		return roles.size() > 0 ? false : true;
	}

	// 新增操作
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("role:insert")
	public MessageObject insert(Role role) {

		MessageObject mo = new MessageObject(0, "新增数据失败，请联系管理员");

		// 执行新增操作
		int row = roleService.insert(role);
		if (row == 1) {
			mo = new MessageObject(1, "新增数据成功");
		}

		return mo;
	}

	// 修改操作
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("role:update")
	public MessageObject update(Role role) {

		MessageObject mo = new MessageObject(0, "修改数据失败，请联系管理员");

		// 执行修改操作
		int row = roleService.updateByPrimaryKeySelective(role);
		if (row == 1) {
			mo = new MessageObject(1, "修改数据成功");
		}
		return mo;
	}
}
