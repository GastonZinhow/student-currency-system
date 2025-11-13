package student.currency.services;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendEmail(String to, String subject, String htmlContent) {
        Email from = new Email("academicamoeda@gmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("STATUS: " + response.getStatusCode());
            System.out.println("BODY: " + response.getBody());
            System.out.println("HEADERS: " + response.getHeaders());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}