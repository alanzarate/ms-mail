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
        String mess = getEvent();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            Context context = new Context();
            context.setVariable("message","this is a message from variable");
            context.setVariable("message2", mess);
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
    public String getEvent(Integer eventId) throws Exception{
        EventEntity eve = eventRepository.findByEventId(eventId);
        if(eve == null) throw new Exception("Any event founded with id");
        CategoryEntity cat = categoryRepository.findByCategoryId(eve.getCategoryId());
        if(cat == null) throw new Exception("Any category founded");

        String mailFormat = cat.getEmailFormat();
        Map<String, Object> valores = new HashMap<>();
        valores.put("nombreEvento", eve.getName());
        valores.put("fechaInicioEvento", eve.getInitialDate().toString());
        valores.put("horaInicioEvento", eve.getInitialDate().getTime());
        valores.put("fechaFinEvento", "aqui no que pedo");
        valores.put("horaFinEvento", "aqui tampoco");

        String regex = "%\\{\\{(.*?)}}%";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(mailFormat);
        StringBuilder resultado = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = valores.get(key).toString();
            matcher.appendReplacement(resultado, Objects.requireNonNullElse(value, "______"));
        }
        matcher.appendTail(resultado);

        return resultado.toString();
    }

    public void sendMailWithAttachmentAndHtml(EmailOrderDto emailOrderDto) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try{
            String mess = getEvent(emailOrderDto.getEventId());
            Context context = new Context();
            context.setVariable("message","this is a message from variable");
            context.setVariable("message2", mess);
            helper.setFrom("no.reply.ucb");
            helper.setTo("alnzarate@gmail.com");
            helper.setSubject("subject");
        }
    }

    }
