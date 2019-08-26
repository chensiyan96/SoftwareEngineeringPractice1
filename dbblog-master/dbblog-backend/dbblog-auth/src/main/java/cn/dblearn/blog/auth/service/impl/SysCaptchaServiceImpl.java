package cn.dblearn.blog.auth.service.impl;

import cn.dblearn.blog.auth.service.SysCaptchaService;
import cn.dblearn.blog.common.constants.RedisKeyConstants;
import cn.dblearn.blog.common.exception.MyException;
import cn.dblearn.blog.common.exception.enums.ErrorEnum;
import cn.dblearn.blog.common.util.RedisUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * SysCaptchaServiceImpl
 *
 * @author bobbi
 * @date 2018/10/19 18:56
 * @email 571002217@qq.com
 * @description
 */
@Service
public class SysCaptchaServiceImpl implements SysCaptchaService {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JavaMailSenderImpl mailSender;

    /**
     * 验证码过期时长5秒
     */
    public final static long CAPTCHA_EXPIRE = 60 * 5;
    /**
     * 邮箱验证码过期时长5秒
     */
    public final static long MAIL_CAPTCHA_EXPIRE = 60 * 10;

    /**
     * 获取验证码
     *
     * @param uuid
     * @return
     */
    @Override
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new MyException(ErrorEnum.NO_UUID);
        }
        //生成文字验证码
        String code = producer.createText();
        // 存进redis,5分钟后过期
        redisUtils.set(genRedisKey(uuid), code, CAPTCHA_EXPIRE);
        return producer.createImage(code);
    }

    private void mailSender(String checkCode, String mailAddr) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("通知by-OurBlog");
        message.setText("OurBlog：您的验证码为" + checkCode);

        message.setTo(mailAddr);
        message.setFrom("3365221601@qq.com");

        mailSender.send(message);
    }

    /**
     * 获取注册邮箱验证码
     */
    public String getMailCaptcha(String mail, String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new MyException(ErrorEnum.NO_UUID);
        }
        //生成文字验证码
        String code = (new Integer((int) ((Math.random() * 9 + 1) * 100000))).toString();
        // 存进redis,10分钟后过期
        mailSender(code, mail);
        redisUtils.set(genRedisKey(uuid), code, MAIL_CAPTCHA_EXPIRE);
        return code;
    }

    /**
     * 验证验证码
     *
     * @param uuid
     * @param code
     * @return
     */
    @Override
    public boolean validate(String uuid, String code) {
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(code)) {
            return false;
        }
        // 从redis中取
        String redisKey = genRedisKey(uuid);
        String captchaCode = redisUtils.get(redisKey);
        //删除验证码
        redisUtils.delete(redisKey);
        if (code.equalsIgnoreCase(captchaCode)) {
            return true;
        }
        return false;
    }

    /**
     * 生成redis key
     *
     * @param uuid
     * @return
     */
    private String genRedisKey(String uuid) {
        return RedisKeyConstants.MANAGE_SYS_CAPTCHA + uuid;
    }
}
