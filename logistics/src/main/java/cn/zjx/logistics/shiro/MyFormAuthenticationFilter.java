package cn.zjx.logistics.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import cn.zjx.logistics.pojo.User;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		/*
		 * Shiro框架的决绝访问访问
		 * 	如果方法返回 true，进行下一步操作
		 * 	如果方法返回 false ，直接终止shiro后面代码运行
		 * 
		 *     可以在方法进行验证码的处理
		 *  1，先从request请求对象中获取 用户提交表单参数 验证码
		 *  2，从Session获取共享的 随机码
		 *  3，比对用户提交的验证码和Session中共享的验证码是否相同（验证码不区分大小写）
		 *   3.1，不相同 ：return false 
		 *   3.2，相同：直接调用父类方法，继续父类代码操作
		 * 
		 */
		HttpServletRequest req = (HttpServletRequest) request;
		// 1，先从request请求对象中获取 用户提交表单参数 验证码
		String verifyCode = req.getParameter("verifyCode");
		System.out.println("verifyCode:"+verifyCode);
		//2，从Session获取共享的 随机码
		String randCode  = (String) req.getSession().getAttribute("rand");
		System.out.println("randCode :" + randCode);
		if(verifyCode=="") {
			if(!verifyCode.toLowerCase().equals(randCode.toLowerCase())) {
				//跳转到登录页面，并且共享错误信息
				req.setAttribute("erroyMsg", "请输入验证码");
				req.getRequestDispatcher("/login.jsp").forward(req, response);
				return false;
			}
		}
		if(StringUtils.isNotBlank(verifyCode)&&StringUtils.isNotBlank(randCode)) {
			//两者相互对比是否相等
			if(!verifyCode.toLowerCase().equals(randCode.toLowerCase())) {
				//跳转到登录页面，并且共享错误信息
				req.setAttribute("erroyMsg", "验证码输入错误");
				req.getRequestDispatcher("/login.jsp").forward(req, response);
				return false;
			}
		}
	
		return super.onAccessDenied(request, response);
	}
	
	
	 @Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {

			/*
			 * 清除掉Shiro记录的上一个请求路径，认证成功以后跳转到
			 * <property name="successUrl" value="/index.do"/>
			 */
			
			//方式一：开发者自己手动清除
			//1.获取sesssion
			/*
			 * Session session = subject.getSession(false); if(session != null) {
			 * //清除shiro共享的上一次地址 ://shiroSavedRequest
			 * session.removeAttribute(WebUtils.SAVED_REQUEST_KEY); }
			 */
			
			//方式二：调用WebUtils工具类中写好的清除的方法
		 WebUtils.getAndClearSavedRequest(request);
		 
		return super.onLoginSuccess(token, subject, request, response);
	}
	 
	 //记住我   从cookie中将数据拿到session
		@Override
		protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
			//从请求中获取Shiro的 主体
			Subject subject = getSubject(request, response);
			//从主体中获取Shiro框架的Session
			Session session = subject.getSession();
			//如果主体没有认证（Session中认证）并且 主体已经设置记住我了
			if(!subject.isAuthenticated()&&subject.isRemembered()) {
				//获取主体的身份（从记住我的Cookie中获取的）
				User user = (User) subject.getPrincipal();
				//将身份认证信息共享到 Session中
				session.setAttribute("user", user);
			}
			
			return subject.isAuthenticated() || subject.isRemembered();
		}
}
