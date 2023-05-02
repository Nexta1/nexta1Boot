package com.nexta1.common.config;

import com.nexta1.common.constant.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 读取项目相关配置
 *
 * @author valarchie
 */
@Component
@ConfigurationProperties(prefix = "nexta1boot")
@Data
public class Nexta1BootConfig {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private static boolean demoEnabled;

    /**
     * 上传路径
     */
    private static String fileBaseDir;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    /**
     * 验证码类型
     */
    private static String captchaType;

    /**
     * rsa private key  静态属性的注入！！ set方法一定不能是static 方法
     */
    private static String rsaPrivateKey;

    private static String apiDocsPathPrefix;

    public static String getFileBaseDir() {
        return fileBaseDir;
    }

    public void setFileBaseDir(String fileBaseDir) {
        Nexta1BootConfig.fileBaseDir = fileBaseDir + File.separator + Constants.RESOURCE_PREFIX;
    }

    public static String getApiDocsPathPrefix() {
        return apiDocsPathPrefix;
    }

    public void setApiDocsPathPrefix(String apiDocsPathPrefix) {
        Nexta1BootConfig.apiDocsPathPrefix = apiDocsPathPrefix;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        Nexta1BootConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        Nexta1BootConfig.captchaType = captchaType;
    }

    public static String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        Nexta1BootConfig.rsaPrivateKey = rsaPrivateKey;
    }

    public static boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        Nexta1BootConfig.demoEnabled = demoEnabled;
    }

}
