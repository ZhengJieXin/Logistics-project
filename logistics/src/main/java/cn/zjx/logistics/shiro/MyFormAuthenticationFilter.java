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
		 * Shiro��ܵľ������ʷ���
		 * 	����������� true��������һ������
		 * 	����������� false ��ֱ����ֹshiro�����������
		 * 
		 *     �����ڷ���������֤��Ĵ���
		 *  1���ȴ�request��������л�ȡ �û��ύ������ ��֤��
		 *  2����Session��ȡ����� �����
		 *  3���ȶ��û��ύ����֤���Session�й������֤���Ƿ���ͬ����֤�벻���ִ�Сд��
		 *   3.1������ͬ ��return false 
		 *   3.2����ͬ��ֱ�ӵ��ø��෽������������������
		 * 
		 */
		HttpServletRequest req = (HttpServletRequest) request;
		// 1���ȴ�request��������л�ȡ �û��ύ������ ��֤��
		String verifyCode = req.getParameter("verifyCode");
		System.out.println("verifyCode:"+verifyCode);
		//2����Session��ȡ����� �����
		String randCode  = (String) req.getSession().getAttribute("rand");
		System.out.println("randCode :" + randCode);
		if(verifyCode=="") {
			if(!verifyCode.toLowerCase().equals(randCode.toLowerCase())) {
				//��ת����¼ҳ�棬���ҹ��������Ϣ
				req.setAttribute("erroyMsg", "��������֤��");
				req.getRequestDispatcher("/login.jsp").forward(req, response);
				return false;
			}
		}
		if(StringUtils.isNotBlank(verifyCode)&&StringUtils.isNotBlank(randCode)) {
			//�����໥�Ա��Ƿ����
			if(!verifyCode.toLowerCase().equals(randCode.toLowerCase())) {
				//��ת����¼ҳ�棬���ҹ��������Ϣ
				req.setAttribute("erroyMsg", "��֤���������");
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
			 * �����Shiro��¼����һ������·������֤�ɹ��Ժ���ת��
			 * <property name="successUrl" value="/index.do"/>
			 */
			
			//��ʽһ���������Լ��ֶ����
			//1.��ȡsesssion
			/*
			 * Session session = subject.getSession(false); if(session != null) {
			 * //���shiro�������һ�ε�ַ ://shiroSavedRequest
			 * session.removeAttribute(WebUtils.SAVED_REQUEST_KEY); }
			 */
			
			//��ʽ��������WebUtils��������д�õ�����ķ���
		 WebUtils.getAndClearSavedRequest(request);
		 
		return super.onLoginSuccess(token, subject, request, response);
	}
	 
	 //��ס��   ��cookie�н������õ�session
		@Override
		protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
			//�������л�ȡShiro�� ����
			Subject subject = getSubject(request, response);
			//�������л�ȡShiro��ܵ�Session
			Session session = subject.getSession();
			//�������û����֤��Session����֤������ �����Ѿ����ü�ס����
			if(!subject.isAuthenticated()&&subject.isRemembered()) {
				//��ȡ�������ݣ��Ӽ�ס�ҵ�Cookie�л�ȡ�ģ�
				User user = (User) subject.getPrincipal();
				//�������֤��Ϣ���� Session��
				session.setAttribute("user", user);
			}
			
			return subject.isAuthenticated() || subject.isRemembered();
		}
}
