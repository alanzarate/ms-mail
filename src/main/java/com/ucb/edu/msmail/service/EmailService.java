package com.ucb.edu.msmail.service;

import com.ucb.edu.msmail.dto.EmailOrderDto;
import com.ucb.edu.msmail.entity.CategoryEntity;
import com.ucb.edu.msmail.entity.EventEntity;
import com.ucb.edu.msmail.repository.CategoryRepository;
import com.ucb.edu.msmail.repository.EventRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Service
public class EmailService {

    @Value("{spring.mail.username}")
    private String emailSender = null;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    public EmailService(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine,
                        EventRepository eventRepository,
                        CategoryRepository categoryRepository){
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    public void sendMailWithAttachment(String toEmail,
                                       String body,
                                       String subject,
                                       String attachment) throws Exception {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(emailSender);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setText(body);
            mimeMessageHelper.setSubject(subject);


            if(attachment != null){
                FileSystemResource fileSystemResource =
                        new FileSystemResource(new File(attachment));
                mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            }


            javaMailSender.send(mimeMessage);



        }catch ( MessagingException mex){
            throw new Exception("Error enviando el mail "+  mex.getMessage());
        }

    }

    public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle exception
        }
    }

    public void sendEmailHtmlTwo(MultipartFile file) throws IOException {
        byte[] bu = file.getBytes();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            Context context = new Context();
            context.setVariable("message","this is a message from variable");
            helper.setFrom("no.reply.ucb");
            helper.setTo("alnzarate@gmail.com");
            helper.setSubject("subject");
            String htmlContent = templateEngine.process("second-template", context);
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle exception
        }
    }
    public String getEvent(EventEntity eve) throws Exception{
        CategoryEntity cat = categoryRepository.findByCategoryId(eve.getCategoryId());
        if(cat == null) throw new Exception("Any category founded");

        String mailFormat = cat.getEmailFormat();
        LocalDateTime init = eve.getInitialDate().toLocalDateTime();
        LocalDateTime finalD = eve.getInitialDate().toLocalDateTime();
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        Map<String, Object> valores = new HashMap<>();
        valores.put("name", eve.getName());
        valores.put("description", eve.getDescription());
        String initString = init.getDayOfMonth() + " de "  +months[init.getMonth().getValue()-1] + " de "+init.getYear();
        String finalString = finalD.getDayOfMonth() + " de "  +months[finalD.getMonth().getValue()-1] + " de "+finalD.getYear();

        valores.put("initalDate",initString);
        valores.put("finalDate", finalString);
        valores.put("initialTime", init.getHour() + ":"+init.getMinute());

        String regex = "%\\{\\{(.*?)}}%";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(mailFormat);
        StringBuilder resultado = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            if(valores.get(key) != null){
                String value = valores.get(key).toString();
                //matcher.appendReplacement(resultado, Objects.requireNonNullElse(value, "______"));
                matcher.appendReplacement(resultado, value);
            }else{
                matcher.appendReplacement(resultado, "____");
            }

        }
        matcher.appendTail(resultado);

        return String.valueOf(resultado);
    }

    public void sendMailWithAttachmentAndHtml(EmailOrderDto emailOrderDto) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try{
            EventEntity eve = eventRepository.findByEventId(emailOrderDto.getEventId());
            if(eve == null) throw new Exception("Any event founded with id");
            String mess = getEvent(eve);
            Context context = new Context();
            String newMess = mess.replace("\\n", "<br>");

            context.setVariable("message","this is a message from variable");
            context.setVariable("message2", newMess);
            context.setVariable("title", eve.getName());
            helper.setFrom("no.reply.ucb");
            helper.setTo(emailOrderDto.getMail().trim());
            helper.setSubject(eve.getName());
            String htmlContent = templateEngine.process("second-template", context);
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    }
