package com.example.backend.config;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MySQL8DialectCustom extends MySQL8Dialect {

    public MySQL8DialectCustom(){
        super();

//        registerFunction(
//                "match",
//                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2)")
//        );
//        registerFunction(
//                "matchs",
//                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1, ?2) against (?3)")
//        );
        registerFunction(
                "match",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in boolean mode)")
        );
        registerFunction(
                "matchs",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1, ?2) against (?3 in boolean mode)")
        );
    }

}
