package se.lexicon.account.api.client;


import se.lexicon.account.component.domain.AccountBalance;
import se.lexicon.account.component.domain.AccountTransaction;
import se.lexicon.account.component.domain.CreateAccountBalanceRequest;
import se.lexicon.account.component.domain.CreateAccountTransactionRequest;

public interface AccountApiClient {
    String DEFAULT_API_BEAN_NAME = "accountApiClient";

    AccountBalance createAccountBalance(CreateAccountBalanceRequest request);
    AccountTransaction createAccountTransaction(CreateAccountTransactionRequest request);

}
