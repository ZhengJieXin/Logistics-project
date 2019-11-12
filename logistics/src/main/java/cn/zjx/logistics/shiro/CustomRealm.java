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
	//	��֤
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.��ȡ�����Ϣ(�˺�)
		String username = (String) token.getPrincipal();
		//*�ȸ��ݵ�ǰ����ݣ��˺ţ�ȥ���ݿ��в�ѯ����Ӧ��User���� 
		User user= userService.selectUserByUsername(username);
		System.out.println(user);
		//UnknownAccountException�쳣
		if(user==null) {
			return null;
		}
		//��ȡ����
		String password = user.getPassword();
		//��
		String salt=user.getSalt();
		//
		//2.��������֤����
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,password,ByteSource.Util.bytes(salt), this.getName());
		return authenticationInfo;
	}
	

	/*
	 * �Զ�����Ȩ����
	 * 
	 * �Զ�����Ȩ˼·
	 * ------------------------------------
	 * 
	 *1�� ��ȡ��ݵĽ�ɫ�� Ȩ��id 
	 * User user = principals.getPrimaryPrincipal();
	 * String roleId = user.getPermissionIds; //1,3,5,8,9,10...
	 * 
	 *2,�и�Ȩ��id�ַ�����ȡÿһ��Ȩ�޵�idֵ
	 *
	 *3������ÿһ��Ȩ�޵�idֵ��ȡ��Ӧ��Ȩ�ޱ��ʽ,
	 * List<String> permissions = permissionService.serlectPermissionByIds();
	 *	�磺
	 *		user:list
	 *		user:insert
	 *		role:delete
	 *		role:update
	 *		....
	 *	
	 * 4,������Ȩ��Ϣ����
	 * 	SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	 * 
	 * 5������������ѯ����Ȩ�ޱ��ʽ��ӵ�Ȩ��Ȩ����Ϣ����
	 * 		authorizationInfo.addStringPermission("user:list");
	 * 
	 * 6����������ʱ��������Ȩ���жϵط���ȡȨ�ޱ��ʽ�� ����Shiro���Ȩ�ޱ��ʽ���ݽ��бȶ�
	 * 	�У�����
	 *  û�У�������
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//��token��ȡ��ݣ��˺ţ���Ϣ
		User user = (User) principals.getPrimaryPrincipal();
		System.out.println("user:"+user);
		String permissionIds = user.getPermissionIds();
		System.out.println("permissionIds:"+permissionIds);
//		2,�и�Ȩ��id�ַ�����ȡÿһ��Ȩ�޵�idֵ
		String[] split = permissionIds.split(",");
		//����List<Long>
		List<Long> permissionIdsList  = new ArrayList<>();
		for (String permissionId : split) {
//			System.out.println("permissionId:"+permissionId);
			permissionIdsList.add(Long.valueOf(permissionId));
		}
		System.out.println("permissionIdsList:"+permissionIdsList);
		

		//����ÿһ��Ȩ�޵�idֵ��ȡ��Ӧ��Ȩ�ޱ��ʽ,
		List<String> permissionByIds = permissionService.serlectPermissionByIds(permissionIdsList);
		System.out.println(permissionByIds);
		//������Ȩ��Ϣ����
		SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
		 //����������ѯ����Ȩ�ޱ��ʽ��ӵ�Ȩ��Ȩ����Ϣ����
		authenticationInfo.addStringPermissions(permissionByIds);
		return authenticationInfo;
	}


}
