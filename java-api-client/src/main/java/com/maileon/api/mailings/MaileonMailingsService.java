package com.maileon.api.mailings;

import com.maileon.api.AbstractMaileonService;
import com.maileon.api.MaileonBadRequestException;
import com.maileon.api.MaileonClientException;
import com.maileon.api.MaileonConfiguration;
import com.maileon.api.MaileonException;
import com.maileon.api.MaileonNotFoundException;
import com.maileon.api.Page;
import com.maileon.api.ResponseWrapper;
import com.maileon.api.XmlUtils;
import com.maileon.api.mailings.trigger.DispatchOptions;
import com.maileon.api.mailings.trigger.DispatchOptionsXmlSerializer;
import com.maileon.api.mailings.xml.MailingsXmlSerializer;
import com.maileon.api.utils.PageUtils;
import com.maileon.api.utils.serialization.KeyValuePair;
import org.dom4j.Element;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>MaileonMailingsService</code> client sends operational requests to the <code>MailingsResource</code>.
 *
 */
public class MaileonMailingsService extends AbstractMaileonService {

    public static final String SERVICE = "MAILEON MAILINGS";

    public enum MailingType {
        REGULAR {
            @Override
            public String getApiString() {
                return "regular";
            }
        },
        TRIGGER {
            @Override
            public String getApiString() {
                return "trigger";
            }
        },
        DOI {
            @Override
            public String getApiString() {
                return "doi";
            }
        };

        public abstract String getApiString();
    };

    /**
     * Constructs a <code>MaileonMailingsService</code>.
     *
     * @param config Maileon API-Configuration.
     */
    public MaileonMailingsService(MaileonConfiguration config) {
        super(config, SERVICE);
    }

    /**
     * Creates a regular mailing and returns its ID.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/create-mailing/?lang=en">Documentation website</a></p>
     *
     * @param name name of mailing
     * @param subject subject of mailing
     * @return id of the underlying mailing
     * @throws com.maileon.api.MaileonException
     */
    public Long createMailing(String name, String subject) throws MaileonException {
        return createMailing(name, subject, MailingType.REGULAR);
    }

    /**
     * Creates a mailing of the given type and returns its ID.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/create-mailing/?lang=en">Documentation website</a></p>
     *
     * @param name name of mailing
     * @param subject subject of mailing
     * @param type type of mailing
     * @return id of the underlying mailing
     * @throws com.maileon.api.MaileonException
     */
    public Long createMailing(String name, String subject, MailingType type) throws MaileonException {
        QueryParameters params = new QueryParameters("name", name);
        params.add("subject", subject);
        params.add("type", type.getApiString());
        Element e = post("mailings", params, null).getEntityAsXml();
        return XmlUtils.getNullableLongValue(e);
    }

    /**
     * Removes mailing according to its id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/delete-mailing/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @throws MaileonException
     */
    public void deleteMailing(long mailingId) throws MaileonException {
        delete("mailings/" + mailingId);
    }

    /**
     * Sends the mailing with the provided id immediately.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-send-now/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @throws MaileonException
     */
    public void sendMailingNow(long mailingId) throws MaileonException {
        post("mailings/" + mailingId + "/sendnow", null);
    }

    /**
     * Returns the URL of the archive version of a sent mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-mailing-archive-url/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding URL of the mailing archive.
     * @throws MaileonException
     */
    public String getArchiveUrl(long mailingId) throws MaileonException {
        return get("mailings/" + mailingId + "/archiveurl").getEntityAsString();
    }

    /**
     * Returns the type of the mailing with the given id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-type/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the mailing type.
     * @throws MaileonException
     */
    public String getType(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/type").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the state of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-state/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the mailing state.
     * @throws MaileonException
     */
    public String getState(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/state").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the ID of the mailing with the provided name.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-by-name/?lang=en">Documentation website</a></p>
     *
     * @param mailingName the name of the mailing.
     * @return the mailing id.
     * @throws MaileonException e.g. if not found
     */
    public long getMailingId(String mailingName) throws MaileonException {
        Element e = get("mailings/name/" + mailingName).getEntityAsXml();
        String idAsString = XmlUtils.getNullableStringValue(e);
        return Long.parseLong(idAsString);
    }

    /**
     * Checks if the mailing with the provided name exists.
     *
     * @param mailingName the name of the mailing.
     * @return <code>true</code> if the mailing exists, <code>false</code> if not.
     * @throws MaileonException
     */
    public boolean existsMailing(String mailingName) throws MaileonException {
        try {
            ResponseWrapper responseWrapper = get("mailings/name/" + mailingName);
            return responseWrapper.getStatusCode() == 200;
        } catch (MaileonNotFoundException e) {
            return false;
        }

    }

    /**
     * Tells whether the mailing with the provided id is sealed. Sealed mailings have other states than "draft".
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-is-sealed/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return <code>true</code> if sealed, <code>true</code> otherwise.
     * @throws MaileonException
     */
    public Boolean isSealed(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/sealed").getEntityAsXml();
        return XmlUtils.getNullableBooleanValue(e);
    }

    /**
     * Returns the name of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-name/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the name of the mailing.
     * @throws MaileonException
     */
    public String getName(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/name").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the tags of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-tags/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the tags of the mailing.
     * @throws MaileonException
     */
    public List<String> getTags(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/tags").getEntityAsXml();
        String tagsJoined = XmlUtils.getNullableStringValue(e);
        ArrayList<String> tags = new ArrayList<>();
        if (tagsJoined != null) {
            for (String tag : tagsJoined.split("#")) {
                if (!tag.isEmpty()) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    /**
     * Returns the author of the mailing with the provided id.
     *
     * @param mailingId the id of the mailing.
     * @return the author name of the underlying mailing.
     * @throws MaileonException
     */
    public String getAuthor(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/author").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the target group id of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-target-group-id/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the id of the corresponding target group.
     * @throws MaileonException
     */
    public Long getTargetGroupId(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/targetgroupid").getEntityAsXml();
        return XmlUtils.getNullableLongValue(e);
    }

    /**
     * Returns the maximum allowed attachment size for the mailing with the provided id in KB.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-max-attachment-size/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the size in KB as a number.
     * @throws MaileonException
     */
    public Integer getMaxAttachmentSize(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/maxattachmentsize").getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Returns the maximal allowed content size for the mailing with the provided id in KB.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-max-content-size/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the size in KB as a number.
     * @throws MaileonException
     */
    public Integer getMaxContentSize(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/maxcontentsize").getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Returns the speed level of the mailing with the provided id. Possible values are: "low", "medium", "high", and "supersonic".
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-speed-level/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return string representing the speed categories above.
     * @throws MaileonException
     */
    public String getSpeedLevel(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/speedlevel").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Sets the speed level of the mailing with the provided id. Valid values are: "low", "medium", "high", and "supersonic".
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-speed-level/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param speedLevel the required level: "low", "medium", "high", and "supersonic".
     * @throws MaileonException
     */
    public void setSpeedLevel(long mailingId, String speedLevel) throws MaileonException {
        post("mailings/" + mailingId + "/settings/speedlevel", null, new KeyValuePair("speed_level", speedLevel).toXML());
    }

    /**
     * Checks if the permissions of contacts are ignored during the dispatch.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-ignore-permission-state/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return true if permissions are ignored
     * @throws MaileonException
     */
    public boolean getIgnorePermission(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/ignorepermission").getEntityAsXml();
        return XmlUtils.getNullableBooleanValue(e);
    }

    /**
     * Changes the setting if permissions of contacts should be ignored during the dispatch.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-ignore-permission-state/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param ignorePermission true if permissions should be ignored
     * @throws MaileonException
     */
    public void setIgnorePermission(long mailingId, boolean ignorePermission) throws MaileonException {
        post("mailings/" + mailingId + "/settings/ignorepermission", null,
                new KeyValuePair("ignore_permission", Boolean.toString(ignorePermission)).toXML());
    }

    /**
     * Sets the locale of the mailing with the provided id. Valid values are: "xx" and "xx_XX", e.g. "de" or "de_DE"
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-locale/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param locale the locale as "xx" or "xx_XX", e.g. "de" or "de_DE" .
     * @throws MaileonException
     */
    public void setLocale(long mailingId, String locale) throws MaileonException {
        post("mailings/" + mailingId + "/settings/locale", null, new KeyValuePair("locale", locale).toXML());
    }

    /**
     * Returns the locale of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-locale/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return locale of the mailings.
     * @throws MaileonException
     */
    public String getLocale(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/locale").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the tracking strategy of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-tracking-strategy/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding tracking strategy.
     * @throws MaileonException
     */
    public String getTrackingStrategy(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/trackingstrategy").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Sets the tracking strategy of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-tracking-strategy/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param alias the required tracking strategy.
     * @throws MaileonException
     */
    public void setTrackingStrategy(long mailingId, String alias) throws MaileonException {
        post("mailings/" + mailingId + "/settings/trackingstrategy", null, new KeyValuePair("tracking_strategy", alias).toXML());
    }

    /**
     * Gets the count of DOI-Confirmation links of the mailing with the provided id.
     *
     *
     * @param mailingId the id of the mailing.
     * @param format the mailing format: text, html.
     * @return the number of DOI-Confirmation links in the underlying mailing.
     * @throws MaileonException
     */
    public Integer getCountDOIConfirmationLinks(long mailingId, Format format) throws MaileonException {
        QueryParameters params = new QueryParameters();
        if (format != null) {
            params.add("format", format.getValue());
        }
        Element e = get("mailings/" + mailingId + "/contents/doiconfirmationlinks/count", params).getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Gets the count of online version links of the mailing with the provided id.
     *
     * @param mailingId the id of the mailing.
     * @param format the mailing format: text, html.
     * @return the number of the online version links in the underlying mailing.
     * @throws MaileonException
     */
    public Integer getCountOnlineVersionLinks(long mailingId, Format format) throws MaileonException {
        QueryParameters params = new QueryParameters();
        if (format != null) {
            params.add("format", format.getValue());
        }
        Element e = get("mailings/" + mailingId + "/contents/onlineversionlinks/count", params).getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Gets the count of unsubscribe links of the mailing with the provided id.
     *
     * @param mailingId the id of the mailing.
     * @param format the mailing format: text, html.
     * @return the number of unsubscribe links in the underlying mailing.
     * @throws MaileonException
     */
    public Integer getCountUnsubscribeLinks(long mailingId, Format format) throws MaileonException {
        QueryParameters params = new QueryParameters();
        if (format != null) {
            params.add("format", format.getValue());
        }
        Element e = get("mailings/" + mailingId + "/contents/unsubscribelinks/count", params).getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Returns the subject of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-subject/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding subject.
     * @throws MaileonException
     */
    public String getSubject(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/subject").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the preview text of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-preview-text/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding preview text.
     * @throws MaileonException
     */
    public String getPreviewText(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/previewtext").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the template of the mailing with the provided id. For templates from the same account, relative paths will be returned in the form "my template" of with sub folders
     * "someSubFolder/my template". For shared templates, an absolute path is returned.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-template/">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding template id.
     * @throws MaileonException
     */
    public String getTemplate(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/template").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the sender alias of the sender address of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-sender-alias/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding sender alias.
     * @throws MaileonException
     */
    public String getSenderAlias(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/senderalias").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the sender address of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-sender-address/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding sender address.
     * @throws MaileonException
     */
    public String getSenderAddress(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/sender").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the recipient alias of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-recipient-alias/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding recipient address.
     * @throws MaileonException
     */
    public String getRecipientAlias(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/recipientalias").getEntityAsXml();
        return XmlUtils.getNullableStringValue(e);
    }

    /**
     * Returns the size of the html content of the mailing.
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding size of the html content.
     * @throws MaileonException
     */
    public Long getHtmlContentSize(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/html/size").getEntityAsXml();
        return XmlUtils.getNullableLongValue(e);
    }

    /**
     * Resets the HTML/text contents of the mailing to its template state.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/reset-contents-to-template/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @throws MaileonException
     */
    public void resetContentsToTemplate(long mailingId) throws MaileonException {
        put("mailings/" + mailingId + "/contents/reset", null);
    }

    /**
     * Fill RSSSmartMailing tags in the given mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-fill-rss-smartmailing-tags/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @throws MaileonException
     */
    public void fillRssSmartContentTags(long mailingId) throws MaileonException {
        post("mailings/" + mailingId + "/contents/smartmailing/rss", null);
    }

    /**
     * Returns the html template of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-html/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the html content.
     * @throws MaileonException
     */
    public String getHtmlContent(long mailingId) throws MaileonException {
        return get("mailings/" + mailingId + "/contents/html", null, MediaType.TEXT_HTML_TYPE).getEntityAsString();
    }

    /**
     * Sets the html content of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-html/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param html content to set.
     * @throws MaileonException
     */
    public void setHtmlContent(long mailingId, String html) throws MaileonException {
        QueryParameters params = new QueryParameters("doImageGrabbing", true);
        params.add("doLinkTracking", true);

        post("mailings/" + mailingId + "/contents/html", params, MediaType.TEXT_HTML_TYPE, html);
    }

    /**
     * Sets the html content of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-html/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param html content to set.
     * @param doImageGrabbing if <code>true</code>, referenced images in the html content will be persistent in Maileon so that they no longer need to be available under the
     * original URL.
     * @param doLinkTracking if <code>true</code>, links will be changed to make them trackable, if <code>false</code>, links will not be changed.
     * @throws MaileonException
     */
    public void setHtmlContent(long mailingId, String html, boolean doImageGrabbing, boolean doLinkTracking) throws MaileonException {
        QueryParameters params = new QueryParameters();
        params.add("doImageGrabbing", doImageGrabbing);
        params.add("doLinkTracking", doLinkTracking);

        post("mailings/" + mailingId + "/contents/html", params, MediaType.TEXT_HTML_TYPE, html);
    }

    /**
     * Returns the settings for the reply-to address for this mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-the-reply-to-address/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return {@link ReplyToAddress} object that contain information about reply-to address
     * @throws MaileonException
     */
    public ReplyToAddress getReplyToAddress(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/replyto").getEntityAsXml();
        ReplyToAddress ra = new ReplyToAddress(e.asXML());
        return ra;
    }

    /**
     * Update the settings for the reply-to address for this mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/set-the-reply-to-address/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param active the state of the reply.
     * @param auto if <code>true</code>, Maileon will handle all mails and provide the replies in its webmail interface. If <code>false</code>, mails will be sent to a customers
     * email address.
     * @param customEmail the email of the customer. All replies should be forwarded when automatic processing is deactivated.
     * @throws MaileonException
     */
    public void setReplyToAddress(long mailingId, boolean active, boolean auto, String customEmail) throws MaileonException {
        QueryParameters params = new QueryParameters();
        params.add("active", active);
        params.add("auto", auto);
        params.add("customEmail", customEmail);

        post("mailings/" + mailingId + "/settings/replyto", params, null).getEntityAsString();
    }

    /**
     * Returns the size of the text content of the mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/set-the-reply-to-address/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding text content size.
     * @throws MaileonException
     */
    public Long getTextContentSize(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/text/size").getEntityAsXml();
        return XmlUtils.getNullableLongValue(e);
    }

    /**
     * Returns the text template of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-text/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding text content.
     * @throws MaileonException
     */
    public String getTextContent(long mailingId) throws MaileonException {
        return get("mailings/" + mailingId + "/contents/text", null, MediaType.TEXT_PLAIN_TYPE).getEntityAsString();
    }

    /**
     * Sets the text content of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-text/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param text content to set.
     * @throws MaileonException
     */
    public void setTextContent(long mailingId, String text) throws MaileonException {
        post("mailings/" + mailingId + "/contents/text", null, MediaType.TEXT_PLAIN_TYPE, text);
    }

    /**
     * Sets the target group id of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-target-group-id/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param targetgroupid the id of the target group to set
     * @throws MaileonException
     */
    public void setTargetGroupId(long mailingId, long targetgroupid) throws MaileonException {
        post("mailings/" + mailingId + "/targetgroupid", null, new KeyValuePair("targetgroupid", String.valueOf(targetgroupid)).toXML());
    }

    /**
     * Sets the maximum of emails for the given mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/set-limit-on-number-of-contacts-before-sending-mailing/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param limit the maximal number of emails to be sent with the given mailing
     * @throws MaileonException
     */
    public void setContactsLimit(long mailingId, int limit) throws MaileonException {
        post("mailings/" + mailingId + "/settings/contacts_limit", null, new KeyValuePair("contacts_limit", Integer.toString(limit)).toXML());
    }

    /**
     * Gets the maximum of emails for the given mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-limit-on-number-of-contacts/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the maximum of emails that will be sent with the given mailing.
     * @throws MaileonException
     */
    public int getContactsLimit(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/contacts_limit").getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Sets if target group should be updated before the sendout.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-enable-target-group-update/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param update false, if updating of target group should be disabled
     * @throws MaileonException
     */
    public void setTargetGroupUpdating(long mailingId, boolean update) throws MaileonException {
        post("mailings/" + mailingId + "/settings/targetgroup_update", null, new KeyValuePair("targetgroup_update", Boolean.toString(update)).toXML());
    }

    /**
     * Gets if target group will be updated before the sendout.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-target-group-update-status/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return false, if the updating of target group will be skipped
     * @throws MaileonException
     */
    public boolean getTargetGroupUpdating(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/targetgroup_update").getEntityAsXml();
        return XmlUtils.getNullableBooleanValue(e);
    }

    /**
     * Updates the name of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-name/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param name the name to set.
     * @throws MaileonException
     */
    public void setName(long mailingId, String name) throws MaileonException {
        post("mailings/" + mailingId + "/name", null, new KeyValuePair("name", name).toXML());
    }

    /**
     * Updates the tags of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-tags/?lang=en">Documentation website</a></p>
     *
     * The tags must be separated by '#'
     *
     * @param mailingId the id of the mailing.
     * @param tags the name to set.
     * @throws MaileonException
     */
    public void setTags(long mailingId, String tags) throws MaileonException {
        post("mailings/" + mailingId + "/settings/tags", null, new KeyValuePair("tags", tags).toXML());
    }

    /**
     * Updates the tags of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-tags/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param tags the name to set.
     * @throws MaileonException
     */
    public void setTags(long mailingId, Iterable<String> tags) throws MaileonException {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = tags.iterator();
        boolean first = true;
        while (it.hasNext()) {
            String tag = it.next();
            if (tag != null && !tag.isEmpty()) {
                if (first) {
                    first = false;
                } else {
                    sb.append('#');
                }
                sb.append(tag);
            }
        }
        post("mailings/" + mailingId + "/settings/tags", null, new KeyValuePair("tags", sb.toString()).toXML());
    }

    /**
     * Updates the sender address of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-sender-address/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param sender the sender address to set.
     * @throws MaileonException
     */
    public void setSenderAddress(long mailingId, String sender) throws MaileonException {
        post("mailings/" + mailingId + "/contents/sender", null, new KeyValuePair("sender", sender).toXML());
    }

    /**
     * Sets the subject of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-subject/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param subject the subject to set.
     * @throws MaileonException
     */
    public void setSubject(long mailingId, String subject) throws MaileonException {
        post("mailings/" + mailingId + "/contents/subject", null, new KeyValuePair("subject", subject).toXML());
    }

    /**
     * Sets the preview text of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/set-preview-text/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param previewText the preview text to set.
     * @throws MaileonException
     */
    public void setPreviewText(long mailingId, String previewText) throws MaileonException {
        post("mailings/" + mailingId + "/contents/previewtext", null, new KeyValuePair("previewtext", previewText).toXML());
    }

    /**
     * Sets the template for a mailing. Be careful, all HTML/text contents will be reset. For templates from the same account, relative paths can be used in the form "my *
     * template" of with sub folders "someSubFolder/my template". For shared templates, an absolute path is required. The easiest way to find the correct path is to set the
     * template manually and use getTemplate() to retrieve the name.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/set-template/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param templateId the template id (=template name) to set.
     * @throws MaileonException
     */
    public void setTemplate(long mailingId, String templateId) throws MaileonException {
        put("mailings/" + mailingId + "/template", null, new KeyValuePair("templateId", templateId).toXML());
    }

    /**
     * Updates the sender-alias of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-sender-alias/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param alias the sender-alias to set
     * @throws MaileonException
     */
    public void setSenderAlias(long mailingId, String alias) throws MaileonException {
        post("mailings/" + mailingId + "/contents/senderalias", null, new KeyValuePair("senderalias", alias).toXML());
    }

    /**
     * Updates the recipient-alias of the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-recipient-alias/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param alias the recipient-alias to set
     * @throws MaileonException
     */
    public void setRecipientAlias(long mailingId, String alias) throws MaileonException {
        post("mailings/" + mailingId + "/contents/recipientalias", null, new KeyValuePair("recipientalias", alias).toXML());
    }

    /**
     * Gets the trackable links within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @param format mailing format: text, html.
     * @return list of {@link Link}.
     * @throws MaileonException
     */
    public List<Link> getTrackableLinks(long mailingId, Format format) throws MaileonException {
        QueryParameters params = new QueryParameters();
        if (format != null) {
            params.add("format", format.getValue());
        }
        Element response = get("mailings/" + mailingId + "/contents/links/trackable", params).getEntityAsXml();
        return LinkAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the number of trackable links within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @param format mailing format: text, html.
     * @return the count of trackable links.
     * @throws MaileonException
     */
    public Integer getCountTrackableLinks(long mailingId, Format format) throws MaileonException {
        QueryParameters params = new QueryParameters();
        if (format != null) {
            params.add("format", format.getValue());
        }
        Element e = get("mailings/" + mailingId + "/contents/links/trackable/count", params).getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Gets the external links within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @param format mailing format: text, html.
     * @return list of {@link Link}.
     * @throws MaileonException
     */
    public List<Link> getExternalLinks(long mailingId, Format format) throws MaileonException {
        QueryParameters params = new QueryParameters();
        if (format != null) {
            params.add("format", format.getValue());
        }
        Element response = get("mailings/" + mailingId + "/contents/links/external", params).getEntityAsXml();
        return LinkAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the number of external links within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @param format mailing format: text, html.
     * @return the count of external links.
     * @throws MaileonException
     */
    public Integer getCountExternalLinks(long mailingId, Format format) throws MaileonException {
        QueryParameters params = new QueryParameters();
        if (format != null) {
            params.add("format", format.getValue());
        }
        Element e = get("mailings/" + mailingId + "/contents/links/external/count", params).getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Returns the used personalizations within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Personalization}.
     * @throws MaileonException
     */
    public List<Personalization> getPersonalizations(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/personalizations").getEntityAsXml();
        return PersonalizationAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the used subject personalizations within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Personalization}.
     * @throws MaileonException
     */
    public List<Personalization> getSubjectPersonalizations(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/subject/personalizations").getEntityAsXml();
        return PersonalizationAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the used recipient-alias personalizations within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Personalization}.
     * @throws MaileonException
     */
    public List<Personalization> getRecipientAliasPersonalizations(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/recipientalias/personalizations").getEntityAsXml();
        return PersonalizationAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the used sender-alias personalizations within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Personalization}.
     * @throws MaileonException
     */
    public List<Personalization> getSenderAliasPersonalizations(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/senderalias/personalizations").getEntityAsXml();
        return PersonalizationAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the used html personalizations within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Personalization}.
     * @throws MaileonException
     */
    public List<Personalization> getHtmlPersonalizations(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/html/personalizations").getEntityAsXml();
        return PersonalizationAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the used text personalizations within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Personalization}.
     * @throws MaileonException
     */
    public List<Personalization> getTextPersonalizations(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/text/personalizations").getEntityAsXml();
        return PersonalizationAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the used images within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Image}.
     * @throws MaileonException
     */
    public List<Image> getImages(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/images").getEntityAsXml();
        return ImageAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the number of the hosted images within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return the count of hosted images.
     * @throws MaileonException
     */
    public Integer getCountHostedImages(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/images/hosted/count").getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Returns the hosted images within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Image}.
     * @throws MaileonException
     */
    public List<Image> getHostedImages(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/images/hosted").getEntityAsXml();
        return ImageAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the number of the external images within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return the count of external images.
     * @throws MaileonException
     */
    public Integer getCountExternalImages(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/contents/images/external/count").getEntityAsXml();
        return XmlUtils.getNullableIntegerValue(e);
    }

    /**
     * Returns the external images within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Image}.
     * @throws MaileonException
     */
    public List<Image> getExternalImages(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/contents/images/external").getEntityAsXml();
        return ImageAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Returns the JPG thumbnail within a particular mailing.
     *
     * @param mailingId the id of the mailing.
     * @return the corresponding thumbnail as byte array.
     * @throws MaileonException
     */
    public byte[] getThumbnail(long mailingId) throws MaileonException {
        return get("mailings/" + mailingId + "/contents/thumbnail", null, MediaType.WILDCARD_TYPE).getEntity();
    }

    /**
     * Returns a list of the registered attachments for the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-attachments/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return list of {@link Attachment}.
     * @throws MaileonException
     */
    public List<Attachment> getAttachments(long mailingId) throws MaileonException {
        Element response = get("mailings/" + mailingId + "/attachments").getEntityAsXml();
        return AttachmentAdaptor.fromXML((List<Element>) response.elements());
    }

    /**
     * Deletes all the attachments that belong to the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-delete-attachments/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @throws MaileonException
     */
    public void deleteAttachments(long mailingId) throws MaileonException {
        delete("mailings/" + mailingId + "/attachments");
    }

    /**
     * Deletes an attachment according to a provided id from the mailing.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-delete-attachment/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param attachmentId the id of the attachment to delete.
     * @throws MaileonException
     */
    public void deleteAttachment(long mailingId, long attachmentId) throws MaileonException {
        delete("mailings/" + mailingId + "/attachments/" + attachmentId);
    }

    /**
     * Adds an attachment to the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-add-attachment/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param in {@link InputStream} to read in the attachment.
     * @param mimeType the mime type of the attachment is allowed
     * @param filename filename of the attachment to be displayed in sent emails
     * @throws MaileonException
     */
    public void addAttachment(long mailingId, InputStream in, String mimeType, String filename) throws MaileonException {
        post("mailings/" + mailingId + "/attachments", new QueryParameters("filename", filename), MediaType.valueOf(mimeType), in);
    }

    /**
     * Updates the corresponding filename of an attachment.
     *
     * @param mailingId the id of the mailing.
     * @param attachmentId the id of the required attachment.
     * @param filename filename of the attachment.
     * @throws MaileonException
     */
    public void updateAttachmentFilename(long mailingId, long attachmentId, String filename) throws MaileonException {
        put("mailings/" + mailingId + "/attachments/" + attachmentId, new QueryParameters("filename", filename), null);
    }

    /**
     * Copies the attachments of a source mailing into a target mailing
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-copy-attachments/?lang=en">Documentation website</a></p>
     *
     * @param srcMailingId the source mailing id.
     * @param destMailingId the destination mailing id.
     * @throws MaileonException
     */
    public void copyAttachments(long srcMailingId, long destMailingId) throws MaileonException {
        put("mailings/" + destMailingId + "/attachments", new QueryParameters("src_mailing_id", srcMailingId), null);
    }

    /**
     * Returns the count of available attachments in the mailing with the provided id.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-count-attachments/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the count of the attachments.
     * @throws MaileonException
     */
    public int getCountAttachments(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/attachments/count").getEntityAsXml();
        try {
            return Integer.parseInt(e.getText());
        } catch (Throwable t) {
            throw new MaileonClientException("unexpected response format", t);
        }
    }

    /**
     * Returns the attachment with the provided id as a file (array of bytes).
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-attachment/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing
     * @param attachmentId the id of the required attachment.
     * @return the corresponding attachment as a byte array.
     * @throws MaileonException
     */
    public byte[] getAttachment(long mailingId, long attachmentId) throws MaileonException {
        return get("mailings/" + mailingId + "/attachments/" + attachmentId, null, MediaType.WILDCARD_TYPE).getEntity();
    }

    /**
     * Returns a page of mailings in the account that match the provided scheduling time (one or a list).
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-mailings-by-schedule-time/?lang=en">Documentation website</a></p>
     *
     * @param scheduleTime date and time string that defines the filter for the mailings. Date format is : "yyyy-MM-dd HH:mm:ss"
     * @param beforeSchedulingTime if <code>true</code>, the mailings before the given time will be returned, if <code>false</code>, the mailings at or after the given time will be
     * returned.
     * @param fields the fields that shall be returned with the result (state, name, type, ..) see {@link MailingFields}
     * @param pageIndex the page index starting from 1.
     * @param pageSize the required number of mailings in a page.
     * @return a page containing the returned {@link Mailing} objects.
     * @throws MaileonException
     */
    public Page<Mailing> getMailingsBySchedulingTime(String scheduleTime, boolean beforeSchedulingTime, List<String> fields, int pageIndex, int pageSize) throws MaileonException {
        if (pageIndex < 1) {
            throw new MaileonBadRequestException("pageIndex must be > 0 - found: " + pageIndex);
        }
        if (pageSize < 1) {
            throw new MaileonBadRequestException("pageSize must be > 0 - found: " + pageSize);
        }
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add("scheduleTime", scheduleTime);
        params.add("beforeSchedulingTime", beforeSchedulingTime);
//        params.add("order", "DESC");
        params.add(generateParameters("fields", fields));

        ResponseWrapper response = get("mailings/filter/scheduletime", params);
        Page<Mailing> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        try {
            page.setItems(MailingsXmlSerializer.deserialize(xml));
        } catch (Exception e) {
            throw new MaileonException("Unable to deserialize mailings.", e);
        }
        return page;
    }

    /**
     * Returns a page of mailings in the account that match the given types (one or a list).
     * <p>
     * Types can be selected from 'doi','trigger', 'trigger_template' or 'regular' </p>
     *
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-mailings-by-types/?lang=en">Documentation website</a></p>
     *
     * @param types the required types to match against.
     * @param fields the fields that shall be returned with the result (state, name, type, ..) @see MailingFields
     * @param pageIndex the page index starting from 1.
     * @param pageSize the required number of mailings in a page.
     * @return a page containing the returned {@link Mailing} objects.
     * @throws MaileonException
     */
    public Page<Mailing> getMailingsByTypes(List<String> types, List<String> fields, int pageIndex, int pageSize) throws MaileonException {
        if (pageIndex < 1) {
            throw new MaileonBadRequestException("pageIndex must be > 0 - found: " + pageIndex);
        }
        if (pageSize < 1) {
            throw new MaileonBadRequestException("pageSize must be > 0 - found: " + pageSize);
        }
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add(generateParameters("fields", fields));
        params.add(generateParameters("types", types));

        ResponseWrapper response = get("mailings/filter/types", params);
        Page<Mailing> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        try {
            page.setItems(MailingsXmlSerializer.deserialize(xml));
        } catch (Exception e) {
            throw new MaileonException("Unable to deserialize mailings.", e);
        }
        return page;
    }

    /**
     * <p>
     * Returns a page of mailings in the account that match the given states (one or a list). Valid states are
     * 'draft','failed','queued','checks','blacklist','preparing','sending','paused','done','canceled','archiving','archived','released' </p>
     *
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-mailings-by-states/?lang=en">Documentation website</a></p>
     *
     *
     * @param states the required states to match against.
     * @param fields the fields that shall be returned with the result (state, name, type, ..) @see MailingFields
     * @param pageIndex the page index starting from 1.
     * @param pageSize the required number of mailings in a page.
     * @return a page containing the returned {@link Mailing} objects.
     * @throws MaileonException
     */
    public Page<Mailing> getMailingsByStates(List<String> states, List<String> fields, int pageIndex, int pageSize) throws MaileonException {
        if (pageIndex < 1) {
            throw new MaileonBadRequestException("pageIndex must be > 0 - found: " + pageIndex);
        }
        if (pageSize < 1) {
            throw new MaileonBadRequestException("pageSize must be > 0 - found: " + pageSize);
        }
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add(generateParameters("fields", fields));
        params.add(generateParameters("states", states));

        ResponseWrapper response = get("mailings/filter/states", params);
        Page<Mailing> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        try {
            page.setItems(MailingsXmlSerializer.deserialize(xml));
        } catch (Exception e) {
            throw new MaileonException("Unable to deserialize mailings.", e);
        }
        return page;
    }

    /**
     * Returns a page of mailings in the account that match given keywords (one or a list).
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-mailings-by-keywords/?lang=en">Documentation website</a></p>
     *
     * @param keywords the required keywords to match against.
     * @param keywordsOp this is the operation that has to be applied. Valid are: <code>and</code>, <code>or</code>
     * @param fields the fields that shall be returned with the result (state, name, type, ..) @see MailingFields
     * @param pageIndex the page index starting from 1.
     * @param pageSize the required number of mailings in a page.
     * @return a page containing the returned {@link Mailing} objects.
     * @throws MaileonException
     */
    public Page<Mailing> getMailingsByKeywords(List<String> keywords, String keywordsOp, List<String> fields, int pageIndex, int pageSize) throws MaileonException {
        if (pageIndex < 1) {
            throw new MaileonBadRequestException("pageIndex must be > 0 - found: " + pageIndex);
        }
        if (pageSize < 1) {
            throw new MaileonBadRequestException("pageSize must be > 0 - found: " + pageSize);
        }
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add(generateParameters("fields", fields));
        params.add(generateParameters("keywords", keywords));
        params.add("keywordsOp", keywordsOp);

        ResponseWrapper response = get("mailings/filter/keywords", params);
        Page<Mailing> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        try {
            page.setItems(MailingsXmlSerializer.deserialize(xml));
        } catch (Exception e) {
            throw new MaileonException("Unable to deserialize mailings.", e);
        }
        return page;
    }

    /**
     * Returns a page of mailings in the account that match a given creator (one or a list).
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-mailings-by-creator-name/?lang=en">Documentation website</a></p>
     *
     * @param creatorName the required creator name to match against.
     * @param creatorNameOp this is the string operator for the creator name. Valid are: <code>contains</code>, <code>equals</code>, <code>starts_with</code>,
     * <code>ends_with</code>.
     * @param fields the fields that shall be returned with the result (state, name, type, ..) @see MailingFields
     * @param pageIndex the page index starting from 1.
     * @param pageSize the required number of mailings in a page.
     * @return a page containing the returned {@link Mailing} objects.
     * @throws MaileonException
     */
    public Page<Mailing> getMailingsByCreatorName(String creatorName, String creatorNameOp, List<String> fields, int pageIndex, int pageSize) throws MaileonException {
        if (pageIndex < 1) {
            throw new MaileonBadRequestException("pageIndex must be > 0 - found: " + pageIndex);
        }
        if (pageSize < 1) {
            throw new MaileonBadRequestException("pageSize must be > 0 - found: " + pageSize);
        }
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add(generateParameters("fields", fields));
        params.add("creatorName", creatorName);
        params.add("creatorNameOp", creatorNameOp);

        ResponseWrapper response = get("mailings/filter/creatorname", params);
        Page<Mailing> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        try {
            page.setItems(MailingsXmlSerializer.deserialize(xml));
        } catch (Exception e) {
            throw new MaileonException("Unable to deserialize mailings.", e);
        }
        return page;
    }

    /**
     * Returns a page of mailings in the account that match a given subject (one or a list).
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/get-mailings-by-subject/?lang=en">Documentation website</a></p>
     *
     * @param subject the required subject name to match against.
     * @param subjectOp this is the string operator for the subject. Valid are: <code>contains</code>, <code>equals</code>, <code>starts_with</code>, <code>ends_with</code>.
     * @param fields the fields that shall be returned with the result (state, name, type, ..) @see MailingFields
     * @param pageIndex the page index starting from 1.
     * @param pageSize the required number of mailings in a page.
     * @return a page containing the returned {@link Mailing} objects.
     * @throws MaileonException
     */
    public Page<Mailing> getMailingsBySubject(String subject, String subjectOp, List<String> fields, int pageIndex, int pageSize) throws MaileonException {
        if (pageIndex < 1) {
            throw new MaileonBadRequestException("pageIndex must be > 0 - found: " + pageIndex);
        }
        if (pageSize < 1) {
            throw new MaileonBadRequestException("pageSize must be > 0 - found: " + pageSize);
        }
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add(generateParameters("fields", fields));
        params.add("subject", subject);
        params.add("subjectOp", subjectOp);

        ResponseWrapper response = get("mailings/filter/subject", params);
        Page<Mailing> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        try {
            page.setItems(MailingsXmlSerializer.deserialize(xml));
        } catch (Exception e) {
            throw new MaileonException("Unable to deserialize mailings.", e);
        }
        return page;
    }

    /**
     * Fetches the DOI mailing key of the mailing identified by the given ID.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-doi-mailing-key/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @return the key of the DOI mailing
     * @throws MaileonException if there was a connection problem or a server error occurred
     */
    public String getDoiMailingKey(long mailingId) throws MaileonException {
        Element e = get("mailings/" + mailingId + "/settings/doi_key").getEntityAsXml();
        try {
            return e.getText();
        } catch (Throwable t) {
            throw new MaileonClientException("unexpected response format", t);
        }
    }

    /**
     * Sets the key of the DOI mailing identified by the given ID.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-set-doi-mailing-key/?lang=en">Documentation website</a></p>
     *
     * @param mailingId the id of the mailing.
     * @param doiKey the key of the DOI mailing
     * @throws MaileonException if there was a connection problem or a server error occurred
     */
    public void setDoiMailingKey(long mailingId, String doiKey) throws MaileonException {
        post("mailings/" + mailingId + "/settings/doi_key", null, new KeyValuePair("doi_key", doiKey).toXML());
    }

    /**
     * Deactivates a trigger mailing by ID.
     *
     * @param mailingId the id of the mailing.
     * @throws MaileonException if there was a connection problem or a server error occurred
     */
    public void deactivateTriggerMailing(long mailingId) throws MaileonException {
        delete("mailings/" + mailingId + "/dispatching");
    }

    /**
     * Get the dispatch data for a trigger mailing by the mailing ID.
     *
     * @param mailingId the id of the mailing.
     * @return the response wrapper with a serialized string of the dispatch schedule
     * @throws MaileonException if there was a connection problem or a server error occurred
     */
    public DispatchOptions getTriggerDispatchSchedule(long mailingId) throws MaileonException {
        ResponseWrapper responseWrapper = get("mailings/" + mailingId + "/dispatching");
        return DispatchOptionsXmlSerializer.deserialize(responseWrapper.getEntityAsXml());
    }

    /**
     * Sets the dispatch options
     *
     * @param mailingId the id of the mailing.
     * @param options the dispatch logic key of the DOI mailing
     * @throws MaileonException if there was a connection problem or a server error occurred
     */
    public void setTriggerDispatchOptions(long mailingId, DispatchOptions options) throws MaileonException {
        put("mailings/" + mailingId + "/dispatching", DispatchOptionsXmlSerializer.serialize(options).asXML());
    }

    /**
     * Used to activate DOI Mailings
     *
     * @param mailingId the id of the mailing.
     * @throws MaileonException if there was a connection problem or a server error occurred
     */
    public void setTriggerActive(long mailingId) throws MaileonException {
        post("mailings/" + mailingId + "/dispatching/activate", "");
    }

    /**
     * Sends a test mailing for the mailing with the provided id to a given email address. If the email address does not exist within your contacts, the personalization is done
     * according to your default personalization user configured in Maileon.
     * <p>
     * <a href="https://dev.maileon.com/mailing-send-testmail-to-single-emailaddress">Documentation website</a></p>
     *
     * @param mailingId id of existing mailing
     * @param email email address
     * @throws MaileonException
     */
    public void sendTestEmail(long mailingId, String email) throws MaileonException {
        post("mailings/" + mailingId + "/sendtestemail", new QueryParameters("email", email), null);
    }

    /**
     * Retrieve list of custom mailing properties.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-get-list-of-custom-mailing-properties/">Documentation website</a></p>
     *
     * @param mailingId id of existing mailing
     * @return
     * @throws MaileonException
     */
    public Properties getMailingProperties(long mailingId) throws MaileonException {
        try {
            String response = get("mailings/" + mailingId + "/settings/properties").getEntityAsString();
            JAXBContext jaxbContext = JAXBContext.newInstance(Properties.class);
            Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(response);
            return (Properties) jaxbMarshaller.unmarshal(reader);
        } catch (Exception e) {
            throw new MaileonException(e.getMessage(), e);
        }
    }

    /**
     * Disables all the QoS checks for a given Mailing. This can be used if an Account has mandatory checks and you want to create and send a mail by API completely.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-disable-qos-checks">Documentation website</a></p>
     *
     * @param mailingId id of existing mailing
     * @throws MaileonException
     */
    public void disableChecks(long mailingId) throws MaileonException {
        put("mailings/" + mailingId + "/settings/disableQosChecks", null);
    }

    /**
     * Add custom mailing properties.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-add-custom-mailing-properties/">Documentation website</a></p>
     *
     * @param mailingId id of mailing
     * @param properties mailing properties
     * @throws MaileonException
     */
    public void addMailingProperties(long mailingId, Properties properties) throws MaileonException {
        try {
            // Get current properties
            Properties currentProperties = getMailingProperties(mailingId);
            currentProperties.getProperties().addAll(properties.getProperties());

            JAXBContext jaxbContext = JAXBContext.newInstance(Properties.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            jaxbMarshaller.marshal(currentProperties, writer);
            String xml = writer.toString();
            post("mailings/" + mailingId + "/settings/properties", null, xml);
        } catch (Exception e) {
            throw new MaileonException(e.getMessage(), e);
        }
    }

    /**
     * Update custom mailing property.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-update-custom-mailing-property/">Documentation website</a></p>
     *
     * @param mailingId id of mailing
     * @param name the name of a property
     * @param value the new value of the property
     * @throws MaileonException
     */
    public void updateMailingProperty(long mailingId, String name, String value) throws MaileonException {
        QueryParameters parameters = new QueryParameters("name", name).add("value", value);
        put("mailings/" + mailingId + "/settings/properties", parameters, null);
    }

    /**
     * Remove custom mailing property
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/mailings/mailing-remove-custom-mailing-property/">Documentation website</a></p>
     *
     * @param mailingId id of mailing
     * @param name the name of a property
     * @throws MaileonException
     */
    public void deleteMailingProperty(long mailingId, String name) throws MaileonException {
        QueryParameters parameters = new QueryParameters("name", name);
        delete("mailings/" + mailingId + "/settings/properties", parameters, null);
    }

    /**
     * Field parameters.
     *
     * @param values the fields
     * @return the query parameters
     */
    private QueryParameters generateParameters(String name, List<String> values) {
        QueryParameters parameters = new QueryParameters();
        if (values != null) {
            for (String value : values) {
                if (value != null) {
                    parameters.add(name, value);
                }
            }
        }
        return parameters;
    }

}
