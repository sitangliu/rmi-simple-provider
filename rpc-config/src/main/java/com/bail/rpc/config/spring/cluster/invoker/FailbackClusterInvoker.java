package com.bail.rpc.config.spring.cluster.invoker;

import com.bail.rpc.config.spring.cluster.Directory;
import com.bail.rpc.config.spring.cluster.balance.LoadBalance;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 19:35
 */
public class FailbackClusterInvoker<T> extends AbstractClusterInvoker<T>{

    private static final Logger logger = LoggerFactory.getLogger(FailbackClusterInvoker.class);

    private static final long RETRY_FAILED_PERIOD = 5 * 1000;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private final ConcurrentMap<Invocation, AbstractClusterInvoker<?>> failed = new ConcurrentHashMap<Invocation, AbstractClusterInvoker<?>>();
    private volatile ScheduledFuture<?> retryFuture;


    public FailbackClusterInvoker(Directory<T> directory) {
        super(directory);
    }

    @Override
    protected Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadBalance) {
        Invoker<T> invoker = select(loadBalance, invocation, invokers, null);
        return invoker.invoke(invocation);
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void destroy() {

    }
}
