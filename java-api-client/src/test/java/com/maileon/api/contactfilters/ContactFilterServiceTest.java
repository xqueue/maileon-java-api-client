package com.maileon.api.contactfilters;


import com.maileon.api.MaileonConfiguration;
import com.maileon.api.MaileonException;
import com.maileon.api.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ContactFilterServiceTest {

    private static MaileonContactfiltersService service;
    
    private static final String TEST_FILTER_NAME = "My JUnit Testfilter";

    @BeforeAll
    public static void beforeAll() {
        MaileonConfiguration configuration = new MaileonConfiguration("https://api.maileon.com/1.0", "secret");

        service = new MaileonContactfiltersService(configuration);
        service.setDebug(true);
        
        //Make sure to delete the apropriate filter(s)
        Page<Contactfilter> contactfilters = service.getContactfilters(1000, 1);
        for (Contactfilter contactfilter : contactfilters.getItems()) {
            if (TEST_FILTER_NAME.equals(contactfilter.getName())) {
                try {
                    service.deleteContactfilter(contactfilter.getId());
                    break;
                } catch (MaileonException e) {
                    System.out.println("Error deleting filter: " + e.getMessage());
                }
                
            }
        }
    }

    @Test
    public void testCreatingFilter() {

        String newFilter = "{" +
                            " \"name\": \""+TEST_FILTER_NAME+"\"," +
                            " \"rules\": [" +
                            " {" +
                            " \"startset\": \"empty\"," +
                            " \"operation\": \"add\"," +
                            " \"selection\": {" +
                            " \"selection_base\": \"contactfield\"," +
                            " \"criterion\": {" +
                            " \"field_name\": \"firstname\"," +
                            " \"operation\": \"equals\"," +
                            " \"value\": \"test\"" +
                            " }" +
                            " }" +
                            " }" +
                            " ]" +
                            "}";
        
        ContactfilterTargetgroupTuple result = service.createContactfilter(newFilter, false);

        Assertions.assertTrue(result.getContactfilterId() > 0);
        Assertions.assertEquals(-1, result.getTargetgroupId());

    }

}
