package com.progressoft.fxdeals;

import org.springframework.boot.SpringApplication;

public class TestFxDealsWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.from(FxDealsWarehouseApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
