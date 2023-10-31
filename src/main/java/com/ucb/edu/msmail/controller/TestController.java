package com.ucb.edu.msmail.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucb.edu.msmail.dto.EmailOrderDto;
import com.ucb.edu.msmail.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    private final EmailService emailService;
    @Autowired
    public TestController(EmailService emailService){this.emailService = emailService;}
    @GetMapping("/")
    public String createFoodOrder() throws Exception {
        log.info("create food order request received");
        emailService.sendMailWithAttachment("alnzarate@gmail.com", "asdkhgbajsd", "subject", null);
        return "SENT";
    }
    @GetMapping("/2")
    public String sendEmail() throws Exception{
        Context context = new Context();
        context.setVariable("message","this is a message from variable");
        emailService.sendEmailWithHtmlTemplate("alnzarate@gmail.com", "this is subject 1", "email-template", context);
        return "Sent";
    }
    @PostMapping("/3")
    public ResponseEntity<?> sendEmailWithAtt(
            @RequestParam("file")MultipartFile file
            ) throws Exception{
        try{
            emailService.sendEmailHtmlTwo(file);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/4")
    public ResponseEntity<?> sendEmailWithAtt2(
            @RequestBody EmailOrderDto body
            ) throws Exception{
        try{

            emailService.sendMailWithAttachmentAndHtml(body);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}















