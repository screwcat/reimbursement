package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "@@@@@@@@@@@@@/OOooooooooOO\\@@@@@@@@@@@@    \n" +
                "@@@@@@@@@@/oooooooooooooooooo\\@@@@@@@@@    \n" +
                "@@@@@@@/oooooooooooooooooooooooo\\@@@@@    \n" +
                "@@@@@oooooooooooooooooooooooooooooo\\@@@    \n" +
                "@@@OooooooooooooooooooooooooooooooooO@@    \n" +
                "@@/oooo/                        =oooo^@    \n" +
                "@/ooooo^  ....................  =o/o//.    \n" +
                "Ooooooo^  ....................  *******    \n" +
                "ooooooo^  ......   ........ ..  .******    \n" +
                "oooo\\o\\*  ....   .   ...   ...  .******    \n" +
                "********  ..  .......   ......  .******    \n" +
                "********  ....................  .******    \n" +
                "`*******  ....................  .******    \n" +
                "^*******                        .******    \n" +
                "@.***************.    .***************,    \n" +
                "@^ **************.    .************** @    \n" +
                "@@^ *********.            .*********,@@    \n" +
                "@@@@\\]****************************/@@@@    \n" +
                "@@@@@@@@`**********************,@@@@@@@    \n" +
                "@@@@@@@@@@@].**************.]@@@@@@@@@@    ");
    }
}
