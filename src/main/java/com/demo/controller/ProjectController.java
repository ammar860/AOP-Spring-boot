package com.demo.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/myprojrct")
public class ProjectController {

    @RequestMapping(method = RequestMethod.POST,value = "/add")
    public ResponseEntity<?> add(@RequestParam int value1, @RequestParam int value2){
        JSONObject responseObject = new JSONObject();
        int sum=value1+value2;
        responseObject.put("sum",sum);
        return  new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/sub")
    public ResponseEntity<?> sub(@RequestParam int value1, @RequestParam int value2){
        JSONObject responseObject = new JSONObject();
        int diff=value1/0;
        responseObject.put("diff",diff);

        return  new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
    }

}
