package se.lexicon.account.api.client;

import com.so4it.api.Account;
import com.so4it.api.ApiClientProvider;

@ApiClientProvider(
        value = Account.NAME,
        specification = Account.PATH,
        version = Account.VERSION,
        beanPublisher = AccountApiProviderBeanPublisher.class)
public class AccountApiProvider {
}
