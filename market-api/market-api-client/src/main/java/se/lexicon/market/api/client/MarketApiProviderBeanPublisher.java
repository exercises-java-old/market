package se.lexicon.market.api.client;

import com.so4it.api.AbstractApiClientProviderBeanPublisher;
import com.so4it.api.ApiFrameworkConfiguration;
import com.so4it.api.importer.ApiClientProviderDefinition;
import com.so4it.api.util.StatusRuntimeExceptionBeanProxy;
import com.so4it.common.bean.BeanContext;
import com.so4it.common.bean.BeanProxy;
import com.so4it.common.bean.BeanProxyInvocationHandler;
import com.so4it.ft.core.FaultTolerantBeanProxy;
import com.so4it.metric.springframework.MetricsTimerBeanProxy;
import com.so4it.request.core.RequestContextBeanProxy;
import io.grpc.ManagedChannel;
import se.lexicon.market.MarketApiServiceGrpc;

/**
 * @author Stephan Köhler {@literal <mailto:stephan.kohler@so4it.com/>}
 */

public class MarketApiProviderBeanPublisher extends AbstractApiClientProviderBeanPublisher {

    @Override
    public void publish(ApiClientProviderDefinition apiClientProviderDefinition, BeanContext beanContext, ApiFrameworkConfiguration apiFrameworkConfiguration, ManagedChannel managedChannel) {
        MarketApiServiceGrpc.MarketApiServiceFutureStub marketService = MarketApiServiceGrpc.newFutureStub(managedChannel);
        MarketApiClient marketApiClient = new MarketApiClientImpl(marketService);
        MarketApiClient marketApiClientProxy = BeanProxyInvocationHandler.create(
                MarketApiClient.class,
                marketApiClient,
                createClientInterceptors(beanContext, marketApiClient));
        beanContext.register(MarketApiClient.DEFAULT_API_BEAN_NAME, marketApiClientProxy);
    }

    private static BeanProxy[] createClientInterceptors(BeanContext beanContext, Object target) {
        return new BeanProxy[]{
                StatusRuntimeExceptionBeanProxy.create(),
                MetricsTimerBeanProxy.create(target),
                FaultTolerantBeanProxy.create(target, beanContext),
                RequestContextBeanProxy.create()};
    }

}
