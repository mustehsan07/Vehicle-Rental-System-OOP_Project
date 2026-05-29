package utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.RentalRequest;

/**
 * Email helper. Uses JavaMail via reflection if available on the classpath.
 * If JavaMail is not present, methods will log a message and return.
 */
public final class EmailService {
    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "email-sender");
        t.setDaemon(true);
        return t;
    });

    private EmailService() {}

    public static void sendNewRequestNotification(RentalRequest request) {
        if (request == null) return;
        String subject = "New rental request: " + request.getRequestId();
        StringBuilder body = new StringBuilder();
        body.append("A new rental request was submitted:\n\n");
        body.append("Request ID: ").append(request.getRequestId()).append("\n");
        body.append("Customer: ").append(request.getCustomerDisplayName()).append("\n");
        body.append("Vehicle: ").append(request.getVehicleDisplayName()).append("\n");
        body.append("Days: ").append(request.getDays()).append("\n");
        body.append("Total: PKR ").append(String.format("%,.2f", request.getTotalCost())).append("\n");
        body.append("Date: ").append(request.getRequestDate()).append("\n\n");
        body.append("Please review the request in the admin panel.");

        for (String adminEmail : SmtpConfig.ADMIN_NOTIFICATION_EMAILS) {
            sendEmailAsync(adminEmail, subject, body.toString());
        }
    }

    public static void sendRequestStatusToCustomer(RentalRequest request, String status) {
        if (request == null || request.getCustomerEmail() == null) return;
        String subject = "Your rental request " + request.getRequestId() + " was " + status;
        StringBuilder body = new StringBuilder();
        body.append("Hello ").append(request.getCustomerName()).append(",\n\n");
        body.append("Your rental request (ID: ").append(request.getRequestId()).append(") has been ").append(status).append(".\n\n");
        body.append("Vehicle: ").append(request.getVehicleDisplayName()).append("\n");
        body.append("Days: ").append(request.getDays()).append("\n");
        body.append("Total: PKR ").append(String.format("%,.2f", request.getTotalCost())).append("\n\n");
        body.append("Thank you for using our service.");

        sendEmailAsync(request.getCustomerEmail(), subject, body.toString());
    }

    private static void sendEmailAsync(String to, String subject, String body) {
        EXEC.submit(() -> {
            try {
                sendEmailReflective(to, subject, body);
            } catch (ClassNotFoundException cnf) {
                System.err.println("JavaMail not on classpath; email not sent: " + cnf.getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void sendEmailReflective(String to, String subject, String body) throws Exception {
        // Try to load Jakarta Mail (new package) first, fall back to javax.mail if necessary
        String mailPkg;
        Class<?> sessionClass;
        Class<?> addressClass;
        Class<?> internetAddressClass;
        Class<?> mimeMessageClass;
        Class<?> transportClass;
        Class<?> messageClass;
        try {
            mailPkg = "jakarta.mail";
            sessionClass = Class.forName(mailPkg + ".Session");
            addressClass = Class.forName(mailPkg + ".Address");
            internetAddressClass = Class.forName(mailPkg + ".internet.InternetAddress");
            mimeMessageClass = Class.forName(mailPkg + ".internet.MimeMessage");
            transportClass = Class.forName(mailPkg + ".Transport");
            messageClass = Class.forName(mailPkg + ".Message");
        } catch (ClassNotFoundException e) {
            mailPkg = "javax.mail";
            sessionClass = Class.forName(mailPkg + ".Session");
            addressClass = Class.forName(mailPkg + ".Address");
            internetAddressClass = Class.forName(mailPkg + ".internet.InternetAddress");
            mimeMessageClass = Class.forName(mailPkg + ".internet.MimeMessage");
            transportClass = Class.forName(mailPkg + ".Transport");
            messageClass = Class.forName(mailPkg + ".Message");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", String.valueOf(SmtpConfig.SMTP_AUTH));
        props.put("mail.smtp.starttls.enable", String.valueOf(SmtpConfig.SMTP_STARTTLS));
        props.put("mail.smtp.host", SmtpConfig.SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SmtpConfig.SMTP_PORT));

        // Session.getInstance(props)
        Method getInstance = sessionClass.getMethod("getInstance", Properties.class);
        Object session = getInstance.invoke(null, props);

        // new MimeMessage(session)
        Constructor<?> mimeCtor = mimeMessageClass.getConstructor(sessionClass);
        Object message = mimeCtor.newInstance(session);

        // setFrom
        Constructor<?> addrCtor = internetAddressClass.getConstructor(String.class);
        Object fromAddr = addrCtor.newInstance(SmtpConfig.SMTP_FROM);
        Method setFrom = mimeMessageClass.getMethod("setFrom", addressClass);
        setFrom.invoke(message, fromAddr);

        // setRecipients
        Object toAddr = addrCtor.newInstance(to);
        Object addrArray = Array.newInstance(addressClass, 1);
        Array.set(addrArray, 0, toAddr);
        Class<?> recipientTypeClass = Class.forName(mailPkg + ".Message$RecipientType");
        Object toEnum = recipientTypeClass.getField("TO").get(null);
        Method setRecipients = mimeMessageClass.getMethod("setRecipients", recipientTypeClass, Array.newInstance(addressClass, 0).getClass());
        setRecipients.invoke(message, toEnum, addrArray);

        // setSubject & setText
        Method setSubject = mimeMessageClass.getMethod("setSubject", String.class);
        Method setText = mimeMessageClass.getMethod("setText", String.class);
        setSubject.invoke(message, subject);
        setText.invoke(message, body);

        // Transport transport = session.getTransport("smtp"); transport.connect(host, port, user, pass); transport.sendMessage(message, message.getAllRecipients()); transport.close();
        Method getTransport = sessionClass.getMethod("getTransport", String.class);
        Object transport = getTransport.invoke(session, "smtp");
        Method connect = transport.getClass().getMethod("connect", String.class, int.class, String.class, String.class);
        connect.invoke(transport, SmtpConfig.SMTP_HOST, SmtpConfig.SMTP_PORT, SmtpConfig.SMTP_USERNAME, SmtpConfig.SMTP_PASSWORD);
        Method getAllRecipients = messageClass.getMethod("getAllRecipients");
        Object recipients = getAllRecipients.invoke(message);
        Class<?> addressArrayClass = Array.newInstance(addressClass, 0).getClass();
        Method sendMessage = transport.getClass().getMethod("sendMessage", messageClass, addressArrayClass);
        sendMessage.invoke(transport, message, recipients);
        Method close = transport.getClass().getMethod("close");
        close.invoke(transport);
    }
}

