package com.xust.sims;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@MapperScan("com.xust.sims.dao")
@EnableCaching
public class SimsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimsApplication.class, args);
        System.out.println("O(∩_∩)O  Student information management system started successfully  (#^.^#)\n" +
                "   .-'''-. .-./`) ,---.    ,---.   .-'''-.  \n" +
                "  / _     \\\\ .-.')|    \\  /    |  / _     \\ \n" +
                " (`' )/`--'/ `-' \\|  ,  \\/  ,  | (`' )/`--' \n" +
                "(_ o _).    `-'`\"`|  |\\_   /|  |(8_ o _).    \n" +
                " (_,_). '.  .---. |  _( )_/ |  | (_,_). '.  \n" +
                ".---.  \\  : |   | | (_ o _) |  |.---.  \\  : \n" +
                "\\    `-'  | |   | |  (_,_)  |  |\\    `-'  | \n" +
                " \\       /  |   | |  |      |  | \\       /  \n" +
                "  `-...-'   '---' '--'      '--'  `-...-'   ");
    }

}
