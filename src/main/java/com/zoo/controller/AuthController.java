package com.zoo.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.model.system.user.SystemUser;
import com.zoo.model.system.user.UserInfo;
import com.zoo.properties.JwtProperties;
import com.zoo.service.system.user.SystemUserService;
import com.zoo.utils.CookieUtils;
import com.zoo.utils.JwtUtils;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
	@Autowired
    private JwtProperties props;
	@Autowired
	SystemUserService systemUserService;
	@PostMapping("auth")
	public ResponseEntity<SystemUser> auth(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String,Object> map = systemUserService.login(username,password);
		String token = map.get("token").toString();
		SystemUser user = (SystemUser) map.get("user");
		if(StringUtils.isBlank(token)) {
			throw new ZooException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
		}
		//将Token写入cookie中
        CookieUtils.newBuilder(response).httpOnly().maxAge(props.getCookieMaxAge()).request(request).build(props.getCookieName(), token);
		return ResponseEntity.ok(user);
	}
	/**
     * 验证用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("ZOO_TOKEN") String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            //从Token中获取用户信息
            UserInfo userInfo = JwtUtils.getUserInfo(props.getPublicKey(), token);
            //成功，刷新Token
            String newToken = JwtUtils.generateToken(userInfo, props.getPrivateKey(), props.getExpire());
            //将新的Token写入cookie中，并设置httpOnly
            CookieUtils.newBuilder(response).httpOnly().maxAge(props.getCookieMaxAge()).request(request).build(props.getCookieName(), newToken);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //Token无效
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @GetMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();//获取cookie数组
        for (Cookie c:cookies) {
            if (c.getName().equals("ZOO_TOKEN")){
                Cookie cookie = new Cookie(c.getName(), null);//删除前必须要new 一个valu为空；
                //cookie.setPath("/");//路径要相同
                cookie.setMaxAge(0);//生命周期设置为0
                response.addCookie(cookie);
            }
        }

    }
}
