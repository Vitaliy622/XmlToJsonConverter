package com.ssi;

import com.ssi.service.XmlConverterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootApplication
public class XmlToJsonApplication implements CommandLineRunner {

    @Resource
    private XmlConverterService xmlConverterService;

    public static void main(String[] args) {
        SpringApplication.run(XmlToJsonApplication.class, args);
    }

    @Override
    public void run(String... arg) throws IOException {
        xmlConverterService.deleteAll();
        xmlConverterService.init();
    }

}
