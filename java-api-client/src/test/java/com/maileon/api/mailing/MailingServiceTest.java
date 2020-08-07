package com.maileon.api.mailing;

import com.maileon.api.MaileonBadRequestException;
import com.maileon.api.MaileonConfiguration;
import com.maileon.api.mailings.MaileonMailingsService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ischuraev
 */
public class MailingServiceTest {

    private static MaileonMailingsService mailingService;

    @BeforeAll
    public static void beforeAll() {
        MaileonConfiguration configuration = new MaileonConfiguration("https://api.maileon.com/1.0", "secret");

        mailingService = new MaileonMailingsService(configuration);
        mailingService.setDebug(false);
    }
    private static final long ONLY_IGOR_TARGET_GROUP_ID = 3;

    private static final String TEXT_CONTENT = "Das ist Textcontent von [[MAILING|NAME]]";
    private static final String HTML_CONTENT = "<html><head><title>[[MAILING|NAME]]</title></head><body>API-TEST [[MAILING|NAME]]</body></html>";

    @Test
    public void testSetMailingFunctions() {
        mailingService.setDebug(true);
        String mailingName = "api test_" + UUID.randomUUID();
        String mailingSubject = "api test_" + UUID.randomUUID();
        Long mailingId = mailingService.createMailing(mailingName, mailingSubject, MaileonMailingsService.MailingType.REGULAR);
        assertNotNull(mailingId);

        try {
            assertEquals(mailingName, mailingService.getName(mailingId), "name of mailing is wrong");
            assertEquals(mailingSubject, mailingService.getSubject(mailingId));
            assertEquals("regular", mailingService.getType(mailingId));
            assertEquals("draft", mailingService.getState(mailingId));
            assertFalse(mailingService.isSealed(mailingId));

            assertEquals(mailingId, mailingService.getMailingId(mailingName));

            String newName = "api test - NICHT LÖSCHEN IGOR " + new Date();
            String newSubject = "api test subject 👣 " + new Date() + "<bold>Test</bold>";
            String newSenderAdress = "apisenderadress" + System.currentTimeMillis();
            String mailingDomain = "letsencrypttest4.e-mailnews.de";

            mailingService.setName(mailingId, newName);
            assertEquals(newName, mailingService.getName(mailingId), "name of mailing is wrong");
            mailingService.setSubject(mailingId, newSubject);
            assertEquals(newSubject, mailingService.getSubject(mailingId));
            mailingService.setSenderAddress(mailingId, newSenderAdress + "@" + mailingDomain);
            assertEquals(newSenderAdress + "@" + mailingDomain, mailingService.getSenderAddress(mailingId));

            // mailing tags
            List<String> mailingTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            mailingService.setTags(mailingId, mailingTags);
            assertEquals(mailingTags, mailingService.getTags(mailingId));

            // speed level
            for (String speedLevel : new String[]{"low", "medium", "high", "supersonic"}) {
                mailingService.setSpeedLevel(mailingId, speedLevel);
                assertEquals(speedLevel, mailingService.getSpeedLevel(mailingId));
            }

            // ignore permission
            for (boolean ignorePermission : new boolean[]{Boolean.FALSE, Boolean.TRUE}) {
                mailingService.setIgnorePermission(mailingId, ignorePermission);
                assertEquals(ignorePermission, mailingService.getIgnorePermission(mailingId));
            }
            // tracking strategy
            for (String trackingStrategy : new String[]{"highest-permission", "none", "anonymous", "single-recipient"}) {
                mailingService.setTrackingStrategy(mailingId, trackingStrategy);
                assertEquals(trackingStrategy, mailingService.getTrackingStrategy(mailingId));
            }

            mailingService.setTargetGroupId(mailingId, ONLY_IGOR_TARGET_GROUP_ID);
            assertEquals(ONLY_IGOR_TARGET_GROUP_ID, mailingService.getTargetGroupId(mailingId));

            assertTrue(mailingService.getTargetGroupUpdating(mailingId));
            mailingService.setTargetGroupUpdating(mailingId, false);
            assertFalse(mailingService.getTargetGroupUpdating(mailingId));

            assertEquals(Integer.MAX_VALUE, mailingService.getContactsLimit(mailingId));
            mailingService.setContactsLimit(mailingId, 1000);
            assertEquals(1000, mailingService.getContactsLimit(mailingId));

            assertEquals(0, mailingService.getCountAttachments(mailingId));
            assertEquals("API-6243cb16", mailingService.getAuthor(mailingId));

            mailingService.setHtmlContent(mailingId, HTML_CONTENT);
            assertEquals(HTML_CONTENT, mailingService.getHtmlContent(mailingId));
            mailingService.setTextContent(mailingId, TEXT_CONTENT);
            assertEquals(TEXT_CONTENT, mailingService.getTextContent(mailingId));

            String previewText = "It's Preview Text " + UUID.randomUUID();
            mailingService.setPreviewText(mailingId, previewText);
            assertEquals(previewText, mailingService.getPreviewText(mailingId));

            mailingService.sendTestEmail(mailingId, "igor.schuraev@xqueue.com");

            String templateId = mailingService.getTemplate(mailingId);
            System.out.println("Template: " + templateId);

            mailingService.setTemplate(mailingId, "igor/igor standard vorlage");
            templateId = mailingService.getTemplate(mailingId);
            System.out.println("Changed template: " + templateId);

        } finally {
            mailingService.deleteMailing(mailingId);
        }
    }

    @Test
    public void testIllegalMailingName() {
        mailingService.setDebug(true);
        String mailingName = "api test_" + UUID.randomUUID();
        String mailingSubject = "api test_" + UUID.randomUUID();
        Long mailingId = mailingService.createMailing(mailingName, mailingSubject, MaileonMailingsService.MailingType.REGULAR);
        assertNotNull(mailingId);
        try {
            MaileonBadRequestException bre = assertThrows(MaileonBadRequestException.class, () -> {
                mailingService.setName(mailingId, "001<h1>escape me</h1><script>alert('escape me')</script>");
            });
            assertNotNull(bre.getResponseWrapper());
            assertEquals("illegal name", bre.getMessage());
        } finally {
            mailingService.deleteMailing(mailingId);
        }

    }
}
