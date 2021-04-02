package com.ling.generator.controller;

import com.ling.generator.model.RespBean;
import com.ling.generator.model.TableClass;
import com.ling.generator.service.GeneratorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhangling 2021/4/2 11:46
 */
@RestController
public class GeneratorCodeController {

    @Autowired
    GeneratorCodeService generatorCodeService;

    @PostMapping("/generateCode")
    public RespBean generatorCode(@RequestBody List<TableClass> tableClasses, HttpServletRequest request) {
        return generatorCodeService.generatorCode(tableClasses, request.getServletContext().getRealPath("/"));
    }
}
