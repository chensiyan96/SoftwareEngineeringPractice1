package com.example.demo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.codec.Hex;
public class loginController {

    if(user ==null || !user.getPassword().equals(new Sha256Hash(form.getPassword(),user.getSalt()).toHex())){
        // 用户名或密码错误
        return Result.error(ErrorEnum.USERNAME_OR_PASSWORD_WRONG);
    }
}
