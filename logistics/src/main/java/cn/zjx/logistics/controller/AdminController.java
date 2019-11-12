package cn.zjx.logistics.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.management.relation.RoleStatus;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import cn.zjx.logistics.mo.MessageObject;
import cn.zjx.logistics.pojo.Role;
import cn.zjx.logistics.pojo.RoleExample;
import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.pojo.UserExample;
import cn.zjx.logistics.pojo.UserExample.Criteria;
import cn.zjx.logistics.service.RoleService;
import cn.zjx.logistics.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	
	@RequestMapping("/login")
	public String login(HttpServletRequest req,Model model) {
		// 获取认证失败的错误信息，在Shiro框架的 FormAuthenticationFilter 过滤器中共享
				// 共享的属性名称 shiroLoginFailure
				// 共享的 shiro 异常的字节码 类型
		System.out.println("AdminController.login()");
		String attribute = (String) req.getAttribute("shiroLoginFailure");
		System.out.println("异常类型："+attribute);
		if(attribute!=null) {
			if(UnknownAccountException.class.getName().equals(attribute)) {
				model.addAttribute("erroyMsg", "亲，账号不存在！！！");
			}else if (IncorrectCredentialsException.class.getName().equals(attribute)) {
				model.addAttribute("erroyMsg", "亲，密码错误！！！");
			}
		}
		return "forward:/login.jsp";
	}
	
	@RequestMapping("/adminPage")
	public String adminPage() {
		
		return "adminPage";
	}
	
	@RequiresPermissions("admin:list")
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<User> list(@RequestParam(defaultValue = "1")Integer pageNum,
								@RequestParam(defaultValue = "10")Integer pageSize,
								String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		UserExample example = new UserExample();
		
//		条件
		if(StringUtils.isNotBlank(keyword)) {
			
			Criteria criteria = example.createCriteria();
			
			criteria.andUsernameLike("%"+keyword+"%");
		
			Criteria criteria2 = example.createCriteria();
			criteria2.andRealnameLike("%"+keyword+"%");
			
			example.or(criteria2);
		}
		
		
		List<User> users = userService.selectByExample(example );
		for (User user : users) {
			System.out.println(user);
		}
		
		PageInfo<User> pageInfo = new PageInfo<User>(users);
		
		return pageInfo;
	}
      //单个删除
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("admin:delete")
	public MessageObject delete(Long userId) {
		
		MessageObject mo = new MessageObject(0, "删除数据失败，请联系管理员");
		
		int row = userService.deleteByPrimaryKey(userId);
		if(row == 1) {
			mo = new MessageObject(1, "删除数据成功");
		}
		
		return mo;
	}
	
    //批量删除
	@RequestMapping("/deletes")
	@ResponseBody
	public MessageObject deletes(Long ids) {
		MessageObject mo = new MessageObject(0, "删除数据失败，请联系管理员");
		System.out.println(ids);	
	
		int row = userService.deleteByPrimaryKey(ids);
		if(row == 1) {
			mo = new MessageObject(1, "删除数据成功");
		}
		
		return mo;
	}
	
	
	//编辑
	@RequestMapping("/edit")
	public String edit(Long userId,Model mo) {
	
		if(userId!=null) {
			User user = userService.selectByPrimaryKey(userId);
			mo.addAttribute("user", user);
		}
		
		//查出所有的角色，已供给新增和修改的管理员的时候选择
		RoleExample example = new RoleExample();
		List<Role> roles = roleService.selectByExample(example);
		mo.addAttribute("roles", roles);
		return "adminEdit";
	}
	
	//检查用户名是否存在
	@RequestMapping("/checkUsername")
	@ResponseBody
	@RequiresPermissions("admin:checkUsername")
	public boolean checkUsername(String username) {
		UserExample example=new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		
		List<User> users = userService.selectByExample(example);
		
		return users.size()>0?false:true;
	}
	//新增操作
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("admin:insert")
	public MessageObject  insert(User user) {
		//自动添加时间
		user.setCreateDate(new Date());
		// 生成随机盐
		String salt = (String) UUID.randomUUID().toString().subSequence(0, 5);
		user.setSalt(salt);
		//设置密码
		Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 3);
		user.setPassword(md5Hash.toString());
		MessageObject mo =new MessageObject(0,"新增失败,请联系管理员" );
		int row = userService.insert(user);
		if(row==1) {
			mo=new MessageObject(1, "新增成功");
		}
		return mo;
	}
	//修改操作
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("admin:update")
	public MessageObject update(User user) {
		
		
		MessageObject mo= new MessageObject(0, "修改失败，请联系管理员");
		
		// 生成随机盐
		String salt = (String) UUID.randomUUID().toString().subSequence(0, 5);
		user.setSalt(salt);
		//设置密码
		Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 3);
		user.setPassword(md5Hash.toString());
		int row = userService.updateByPrimaryKeySelective(user);
		if(row==1) {
			mo=new MessageObject(1, "修改成功");
			
		}
		return mo;
	}
	
}
