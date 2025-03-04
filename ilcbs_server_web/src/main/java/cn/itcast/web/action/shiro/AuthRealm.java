package cn.itcast.web.action.shiro;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.domain.Module;
import cn.itcast.domain.Role;
import cn.itcast.domain.User;
import cn.itcast.service.UserService;

public class AuthRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		System.out.println("调用了授权方法");
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		User user = (User) arg0.getPrimaryPrincipal();
		//获取用户的角色及角色的模块
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			Set<Module> modules = role.getModules();
			for (Module module : modules) {
				info.addStringPermission(module.getCpermission());
			}
		}
		
/*		Set<String> stringPermissions = info.getStringPermissions();
		System.out.println(stringPermissions);*/
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		// TODO Auto-generated method stub、
		System.out.println("调用了认证方法");
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		final String username = token.getUsername();
		
		Specification<User> spec = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("userName").as(String.class), username);
			}
		};
		List<User> userList = userService.find(spec);
		
		if(userList!=null && userList.size() > 0){
			User user = userList.get(0);
			//principal:主要对象（登录的用户）   credentials:密码      realm的名字可以通过getName获取类名作为区分
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}
		return null;
	}

}
