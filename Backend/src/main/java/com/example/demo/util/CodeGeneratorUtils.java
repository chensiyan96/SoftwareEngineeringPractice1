package com.example.demo.util;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;

import java.util.Scanner;

/**
 * CodeGenerator
 *
 * @author bobbi
 * @date 2018/10/08 20:24
 * @email 571002217@qq.com
 * @description mybatis-plus提供的代码生成器
 */
public class CodeGeneratorUtils {

    /**
     * <p>
     * 读取控制台内容
     * </p >
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        String moduleName = scanner("请输入模块名");
        String tableName = scanner("请输入表名");
        String entityName = "";
        if (tableName.indexOf("_") > 0) {
            entityName = tableName.split("_")[1];
        } else {
            entityName = tableName;
        }

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("bobbi");
        ;
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
    }
}