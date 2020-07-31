package com.maileon.api.contacts;

import com.maileon.api.MaileonBadRequestException;
import com.maileon.api.MaileonConfiguration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ischuraev
 */
public class ContactFieldsServiceTest {

    private static MaileonContactFieldsService contactFieldService;

    @BeforeAll
    public static void beforeAll() {
        MaileonConfiguration configuration = new MaileonConfiguration("https://api-test.maileon.com/1.0", "secret");

        contactFieldService = new MaileonContactFieldsService(configuration);
        contactFieldService.setDebug(false);
    }

    @Test
    public void testCRUD() {

        final String fieldName = "apiclienttest";
        contactFieldService.createCustomField(fieldName, "string");

        List<CustomContactFieldDefinition> contactFieldResponse = contactFieldService.getCustomFields();

        for (CustomContactFieldDefinition cfd : contactFieldResponse) {
            if (fieldName.equals(cfd.getName())) {
                System.out.println(fieldName + " is found");
                break;
            }
        }

        contactFieldService.renameCustomField(fieldName, "apiclienttestnew");

        contactFieldService.deleteCustomField("apiclienttestnew");
    }

    @Test
    public void testFieldWrongDataType() {
        final String fieldName = "apiclienttestduplib";

        MaileonBadRequestException bre = assertThrows(MaileonBadRequestException.class, () -> {
            contactFieldService.createCustomField(fieldName, "none");
        });
        System.out.println(bre.getResponseWrapper().getEntityAsString());
        assertNotNull(bre.getReasonPhrase());
        assertFalse(bre.getStatusCode() <= 0);
        assertNotNull(bre.getResponseWrapper());

    }
}
