package com.maileon.api.transactions;

import com.maileon.api.AbstractMaileonService;
import com.maileon.api.MaileonConfiguration;
import com.maileon.api.MaileonException;
import com.maileon.api.Page;
import com.maileon.api.ResponseWrapper;
import com.maileon.api.transactions.serializer.TransactionProcessingReportsJsonSerializer;
import com.maileon.api.transactions.serializer.TransactionTypeXmlSerializer;
import com.maileon.api.transactions.serializer.TransactionTypesXmlSerializer;
import com.maileon.api.transactions.serializer.TransactionsJsonSerializer;
import com.maileon.api.utils.PageUtils;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The <code>MaileonTransactionsService</code> client send operational requests to the <code>TransactionsResource</code>.
 *
 */
public class MaileonTransactionsService extends AbstractMaileonService {

    public static final String SERVICE = "MAILEON TRANSACTIONS SERVICE";

    /**
     * Instantiates a new instance of transaction service.
     *
     * @param config the configuration of Maileon API
     */
    public MaileonTransactionsService(MaileonConfiguration config) {
        super(config, SERVICE);
    }

    /**
     * Returns the overall number of transaction types in the account.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/get-transaction-types-count/?lang=en">Documentation website</a></p>
     *
     * @return the count of transaction types
     * @throws MaileonException
     */
    public int getTransactionTypesCount() throws MaileonException {
        ResponseWrapper response = get("transactions/types/count");
        return Integer.parseInt(response.getEntityAsXml().getText());
    }

    /**
     * Returns a page (list) of all transaction types defined in the account.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/get-all-transaction-types/?lang=en">Documentation website</a></p>
     *
     * @param pageIndex stating from 1
     * @param pageSize the number transaction types of within a page
     * @return the page containing the found transaction types as {@link TransactionType} objects
     * @throws MaileonException
     */
    public Page<TransactionType> getTransactionTypes(int pageIndex, int pageSize) throws MaileonException {
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        ResponseWrapper response = get("transactions/types", params);
        Page<TransactionType> page = PageUtils.createPage(pageIndex, pageSize, response);
        page.setItems(TransactionTypesXmlSerializer.deserialize(response.getEntityAsXml()));
        return page;
    }

    /**
     * Creates a new transaction type with the given name in the account.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/create-transaction-type/?lang=en">Documentation website</a></p>
     *
     * @param type the definition of transaction type
     * @return id of created transaction type.
     * @throws MaileonException
     */
    public long createTransactionType(TransactionType type) throws MaileonException {
        ResponseWrapper response = post("transactions/types", TransactionTypeXmlSerializer.serialize(type).asXML());
        return Long.parseLong(response.getEntityAsXml().getText());
    }

    /**
     * Creates a new transaction type with the given name in the account.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/create-transaction-type/?lang=en">Documentation website</a></p>
     *
     * @param type the definition of transaction type
     * @param example an example transaction
     * @return id of created transaction type
     * @throws MaileonException
     */
    public long createTransactionTypeByExample(TransactionType type, String example) throws MaileonException {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        // perform preliminary check. The full requirements can be found in the documentation.
        if (type.getName() == null || type.getName().trim().length() < 3) {
            throw new IllegalArgumentException("the name of type is too short");
        }
        Object parsedExample;
        try {
            parsedExample = new JSONParser().parse(example);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("example isn't valid JSON");
        }
        JSONObject transactionType = new JSONObject();
        transactionType.put("name", type.getName());
        transactionType.put("storeOnly", type.isStoreOnly());
        if (type.getArchivingDuration() != null) {
            transactionType.put("archivingDuration", type.getArchivingDuration());
        }
        transactionType.put("content", parsedExample);
        ResponseWrapper response = post("transactions/types", null, MediaType.APPLICATION_JSON_TYPE, MAILEON_XML_TYPE, transactionType.toJSONString());
        return Long.parseLong(response.getEntityAsXml().getText());

    }

    /**
     * Deletes a transaction type.
     *
     * @param typeName the name of transaction type
     * @throws MaileonException
     */
    public void deleteTransactionTypeByName(String typeName) throws MaileonException {
        delete("transactions/types/" + typeName);
    }

    /**
     * Deletes a transaction type.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/delete-transaction-type/?lang=en">Documentation website</a></p>
     *
     * @param id the id of transaction type
     * @throws MaileonException
     */
    public void deleteTransactionType(long id) throws MaileonException {
        delete("transactions/types/" + id);
    }

    /**
     * Retrieves the transaction type with the given id.
     *
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/get-transaction-type/?lang=en">Documentation website</a></p>
     *
     * @param id the id of the required transaction type
     * @return the corresponding transaction type
     * @throws MaileonException
     */
    public TransactionType getTransactionType(long id) throws MaileonException {
        ResponseWrapper response = get("transactions/types/" + id);
        return TransactionTypeXmlSerializer.deserialize(response.getEntityAsXml());
    }

    /**
     * Retrieves the transaction type with the given name.
     *
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/get-transaction-type/?lang=en">Documentation website</a></p>
     *
     * @param name the name of the required transaction type
     * @return the definition of transaction type
     * @throws MaileonException
     */
    public TransactionType getTransactionType(String name) throws MaileonException {
        ResponseWrapper response = get("transactions/types/" + name);
        return TransactionTypeXmlSerializer.deserialize(response.getEntityAsXml());
    }

    /**
     * Creates a single transaction.
     *
     * @param transaction the transaction data
     * @param ignoreInvalidTransactions if set to <code>false</code>, exceptions like invalid contacts will cause the service to return <code>400 Bad request</code>
     * @return a report of type {@link TransactionProcessingReport} hat indicates if the queuing was successful for the underlying transaction
     * @throws MaileonException
     */
    public TransactionProcessingReport createTransaction(Transaction transaction, boolean ignoreInvalidTransactions) throws MaileonException {
        return createTransactions(Arrays.asList(transaction), ignoreInvalidTransactions, false).iterator().next();
    }

    /**
     * Creates several transactions in the account.
     *
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/create-transactions/?lang=en">Documentation website</a></p>
     *
     * @param transactions the list of {@link Transaction} to create
     * @param ignoreInvalidTransactions if set to <code>false</code>, exceptions like invalid contacts will cause the service to return <code>400 Bad request</code>
     * @param generateTransactionId
     * @return a list of reports of type {@link TransactionProcessingReport} that indicates if the queuing was successful for each transaction
     * @throws MaileonException
     */
    public List<TransactionProcessingReport> createTransactions(List<Transaction> transactions, boolean ignoreInvalidTransactions, boolean generateTransactionId)
            throws MaileonException {
        QueryParameters params = new QueryParameters("ignore_invalid_transactions", ignoreInvalidTransactions);
        params.add("generate_transaction_id", generateTransactionId);
        ResponseWrapper response = post("transactions", params, MediaType.APPLICATION_JSON_TYPE, TransactionsJsonSerializer.serialize(transactions).toJSONString());
        if (response.hasEntity()) {
            return TransactionProcessingReportsJsonSerializer.deserialize(response.getEntityAsString());
        }
        throw new MaileonException(response);
    }

    /**
     * Deletes all transactions of a given type before a given date in the account.
     *
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/transactions/delete-transactions-before-a-given-date/?lang=en">Documentation website</a></p>
     *
     * @param typeId the id of a transaction type
     * @param beforeTimestamp the unix timestamp
     * @throws MaileonException
     */
    public void deleteTransactions(long typeId, long beforeTimestamp) throws MaileonException {
        QueryParameters params = new QueryParameters("type_id", typeId);
        params.add("before_timestamp", beforeTimestamp * 1000L);
        delete("transactions", params);
    }

}
