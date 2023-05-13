package com.nexta1.core.web.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.code.kaptcha.Producer;
import com.nexta1.common.config.Nexta1BootConfig;
import com.nexta1.common.constant.Constants;
import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import com.nexta1.common.utils.ServletHolderUtil;
import com.nexta1.common.utils.i18n.MessageUtils;
import com.nexta1.core.cache.guava.GuavaCacheService;
import com.nexta1.core.cache.redis.RedisCacheService;
import com.nexta1.core.thread.AsyncTaskFactory;
import com.nexta1.core.thread.ThreadPoolManager;
import com.nexta1.core.web.domain.login.DTO.CaptchaDTO;
import com.nexta1.core.web.domain.login.DTO.LoginDTO;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import com.nexta1.orm.common.enums.ConfigKeyEnum;
import com.nexta1.orm.common.enums.LoginStatusEnum;
import com.nexta1.orm.system.entity.SysUserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class LoginService {

    @NonNull
    private TokenService tokenService;

    @NonNull
    private GuavaCacheService guavaCache;

    @NonNull
    private RedisCacheService redisCache;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @NonNull
    private AuthenticationManager authenticationManager;

    /**
     * Description:
     * Login
     *
     * @author nexta1
     * @date 2023/5/3 22:24
     */
    public String login(LoginDTO loginDTO) {
        // 验证码开关
        if (isCaptchaOn()) {
            validateCaptcha(loginDTO.getUsername(), loginDTO.getCode(), loginDTO.getUuid());
        }
        String decryptPassword = decryptPassword(loginDTO.getPassword());
        try {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            // 该方法会去调用 UserDetailsServiceImpl#loadUserByUsername  校验用户名和密码  认证鉴权
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), decryptPassword));
            // 把当前登录用户 放入上下文中
            context.setAuthentication(authentication);
            // 这里获取的loginUser是LoginUserDetailsService#loadUserByUsername方法返回的LoginUser
            LoginUserDetail loginUserDetail = (LoginUserDetail) authentication.getPrincipal();
            recordLoginInfo(loginUserDetail);
            // 生成token
            return tokenService.createTokenAndPutUserInCache(loginUserDetail);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginDTO.getUsername(), LoginStatusEnum.LOGIN_FAIL,
                        MessageUtils.message("user.password.not.match")));
                throw new ApiException(ErrorCode.Business.LOGIN_WRONG_USER_PASSWORD);
            } else {
                ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginDTO.getUsername(), LoginStatusEnum.LOGIN_FAIL, e.getMessage()));
                throw new ApiException(e.getCause(), ErrorCode.Business.LOGIN_ERROR, e.getMessage());
            }
        }

    }

    /**
     * Description:
     * 获取验证码
     *
     * @author nexta1
     * @date 2023/5/3 12:58
     */
    public CaptchaDTO generateCaptchaImg() {
        CaptchaDTO captchaDTO = new CaptchaDTO();

        boolean isCaptchaOn = isCaptchaOn();
        captchaDTO.setIsCaptchaOn(isCaptchaOn);
        if (!isCaptchaOn) {
            return captchaDTO;
        }
        String expression, answer = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = Nexta1BootConfig.getCaptchaType();

        if (Constants.Captcha.MATH_TYPE.equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            String[] expressionAndAnswer = capText.split("@");
            expression = expressionAndAnswer[0];
            answer = expressionAndAnswer[1];
            image = captchaProducerMath.createImage(expression);
        }

        if (Constants.Captcha.CHAR_TYPE.equals(captchaType)) {
            expression = answer = captchaProducer.createText();
            image = captchaProducer.createImage(expression);
        }

        if (image == null) {
            throw new ApiException(ErrorCode.Internal.LOGIN_CAPTCHA_GENERATE_FAIL);
        }


        String uuid = IdUtil.simpleUUID();
        // 保存验证码信息
        redisCache.captchaCache.set(uuid, answer);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImgUtil.writeJpg(image, os);

        captchaDTO.setUuid(uuid);
        captchaDTO.setImg(Base64.encode(os.toByteArray()));


        return captchaDTO;
    }

    public void validateCaptcha(String username, String code, String uuid) {
        String captcha = redisCache.captchaCache.getObjectById(uuid);
        redisCache.captchaCache.delete(uuid);
        if (captcha == null) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(username, LoginStatusEnum.LOGIN_FAIL,
                    ErrorCode.Business.LOGIN_CAPTCHA_CODE_EXPIRE.message()));
            throw new ApiException(ErrorCode.Business.LOGIN_CAPTCHA_CODE_EXPIRE);
        }
        if (!code.equalsIgnoreCase(captcha)) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(username, LoginStatusEnum.LOGIN_FAIL,
                    ErrorCode.Business.LOGIN_CAPTCHA_CODE_WRONG.message()));
            throw new ApiException(ErrorCode.Business.LOGIN_CAPTCHA_CODE_WRONG);
        }
    }

    private boolean isCaptchaOn() {
        return Convert.toBool(guavaCache.configCache.get(ConfigKeyEnum.CAPTCHA.getValue()));
    }

    public String decryptPassword(String originalPassword) {
        byte[] decryptBytes = SecureUtil.rsa(Nexta1BootConfig.getRsaPrivateKey(), null)
                .decrypt(Base64.decode(originalPassword), KeyType.PrivateKey);

        return StrUtil.str(decryptBytes, CharsetUtil.CHARSET_UTF_8);
    }

    public void recordLoginInfo(LoginUserDetail loginUserDetail) {
        ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginUserDetail.getUsername(), LoginStatusEnum.LOGIN_SUCCESS,
                LoginStatusEnum.LOGIN_SUCCESS.description()));

        SysUserEntity entity = redisCache.userCache.getObjectById(loginUserDetail.getUserId());
        entity.setLoginIp(ServletUtil.getClientIP(ServletHolderUtil.getRequest()));
        entity.setLoginDate(DateUtil.date());
        entity.updateById();
    }
}
