package cn.zjx.logistics.shiro;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.tribes.util.Arrays;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.sql.parser.Token;

import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.service.PermissionService;
import cn.zjx.logistics.service.UserService;

public class CustomRealm  extends AuthorizingRealm{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService;
	//	认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.获取身份信息(账号)
		String username = (String) token.getPrincipal();
		//*先根据当前的身份（账号）去数据库中查询出对应的User对象 
		User user= userService.selectUserByUsername(username);
		System.out.println(user);
		//UnknownAccountException异常
		if(user==null) {
			return null;
		}
		//获取密码
		String password = user.getPassword();
		//盐
		String salt=user.getSalt();
		//
		//2.创建简单认证对象
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,password,ByteSource.Util.bytes(salt), this.getName());
		return authenticationInfo;
	}
	

	/*
	 * 自定义授权方法
	 * 
	 * 自定义授权思路
	 * ------------------------------------
	 * 
	 *1， 获取身份的角色的 权限id 
	 * User user = principals.getPrimaryPrincipal();
	 * String roleId = user.getPermissionIds; //1,3,5,8,9,10...
	 * 
	 *2,切割权限id字符串获取每一个权限的id值
	 *
	 *3，根据每一个权限的id值获取对应的权限表达式,
	 * List<String> permissions = permissionService.serlectPermissionByIds();
	 *	如：
	 *		user:list
	 *		user:insert
	 *		role:delete
	 *		role:update
	 *		....
	 *	
	 * 4,创建授权信息对象
	 * 	SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	 * 
	 * 5，将第三步查询出的权限表达式添加到权限权限信息对中
	 * 		authorizationInfo.addStringPermission("user:list");
	 * 
	 * 6，程序运行时候会根据有权限判断地方获取权限表达式和 设置Shiro框架权限表达式数据进行比对
	 * 	有：放行
	 *  没有：不放行
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//从token获取身份（账号）信息
		User user = (User) principals.getPrimaryPrincipal();
		System.out.println("user:"+user);
		String permissionIds = user.getPermissionIds();
		System.out.println("permissionIds:"+permissionIds);
//		2,切割权限id字符串获取每一个权限的id值
		String[] split = permissionIds.split(",");
		//创建List<Long>
		List<Long> permissionIdsList  = new ArrayList<>();
		for (String permissionId : split) {
//			System.out.println("permissionId:"+permissionId);
			permissionIdsList.add(Long.valueOf(permissionId));
		}
		System.out.println("permissionIdsList:"+permissionIdsList);
		

		//根据每一个权限的id值获取对应的权限表达式,
		List<String> permissionByIds = permissionService.serlectPermissionByIds(permissionIdsList);
		System.out.println(permissionByIds);
		//创建授权信息对象
		SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
		 //将第三步查询出的权限表达式添加到权限权限信息对中
		authenticationInfo.addStringPermissions(permissionByIds);
		return authenticationInfo;
	}


}
