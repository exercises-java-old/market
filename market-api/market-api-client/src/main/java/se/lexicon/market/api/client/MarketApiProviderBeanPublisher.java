package se.lexicon.account.api.client;

import se.lexicon.account.api.AccountApiServiceGrpc;
import com.so4it.api.AbstractApiClientProviderBeanPublisher;
import com.so4it.api.ApiFrameworkConfiguration;
import com.so4it.api.importer.ApiClientProviderDefinition;
import com.so4it.api.util.StatusRuntimeExceptionBeanProxy;
import com.so4it.common.bean.BeanContext;
import com.so4it.common.bean.BeanProxyInvocationHandler;
import com.so4it.ft.core.FaultTolerantBeanProxy;
import com.so4it.metric.springframework.MetricsTimerBeanProxy;
import com.so4it.request.core.RequestContextBeanProxy;
import io.grpc.ManagedChannel;

public class AccountApiProviderBeanPublisher extends AbstractApiClientProviderBeanPublisher {

    @Override
    public void publish(ApiClientProviderDefinition apiProviderDefinition,
                        BeanContext beanContext,
                        ApiFrameworkConfiguration configuration,
                        ManagedChannel managedChannel) {
        AccountApiServiceGrpc.AccountApiServiceBlockingStub agreementApiServiceBlockingStub = AccountApiServiceGrpc.newBlockingStub(managedChannel);
        AccountApiClient accountApiClientImpl = new AccountApiClientImpl(agreementApiServiceBlockingStub);
        AccountApiClient accountApiClient = BeanProxyInvocationHandler.create(
                AccountApiClient.class,
                accountApiClientImpl,
                StatusRuntimeExceptionBeanProxy.create(),
                MetricsTimerBeanProxy.create(accountApiClientImpl),
                //FaultTolerantBeanProxy.create(accountApiClientImpl, beanContext),
                RequestContextBeanProxy.create());
        beanContext.register(AccountApiClient.DEFAULT_API_BEAN_NAME, accountApiClient);
    }

}
