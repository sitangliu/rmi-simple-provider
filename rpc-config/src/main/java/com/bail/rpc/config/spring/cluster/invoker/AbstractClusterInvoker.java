package com.bail.rpc.config.spring.cluster.invoker;

import com.bail.rpc.config.spring.cluster.Directory;
import com.bail.rpc.config.spring.cluster.balance.LoadBalance;
import com.bail.rpc.config.spring.cluster.balance.RandomLoadBalance;
import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 19:36
 */
public abstract class AbstractClusterInvoker<T> implements Invoker<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClusterInvoker.class);

    protected final Directory<T> directory;

    protected final boolean availablecheck;

    private AtomicBoolean destroyed = new AtomicBoolean(false);

    private volatile Invoker<T> stickyInvoker = null;

    public AbstractClusterInvoker(Directory<T> directory) {
        this(directory, directory.getUrl());
    }

    public AbstractClusterInvoker(Directory<T> directory, URL url) {
        if (directory == null)
            throw new IllegalArgumentException("service directory == null");

        this.directory = directory;
        //sticky: invoker.isAvailable() should always be checked before using when availablecheck is true.
        this.availablecheck = url.getParameter(Constants.CLUSTER_AVAILABLE_CHECK_KEY, Constants.DEFAULT_CLUSTER_AVAILABLE_CHECK);
    }

    protected Invoker<T> select(LoadBalance loadbalance, Invocation invocation, List<Invoker<T>> invokers, List<Invoker<T>> selected) throws RpcException {
        if (invokers == null || invokers.isEmpty())
            return null;
        String methodName = invocation == null ? "" : invocation.getMethodName();

        boolean sticky = invokers.get(0).getUrl().getMethodParameter(methodName, Constants.CLUSTER_STICKY_KEY, Constants.DEFAULT_CLUSTER_STICKY);
        {
            //ignore overloaded method
            if (stickyInvoker != null && !invokers.contains(stickyInvoker)) {
                stickyInvoker = null;
            }
            //ignore concurrency problem
            if (sticky && stickyInvoker != null && (selected == null || !selected.contains(stickyInvoker))) {
                if (availablecheck && stickyInvoker.isAvailable()) {
                    return stickyInvoker;
                }
            }
        }
        Invoker<T> invoker = doSelect(loadbalance, invocation, invokers, selected);

        if (sticky) {
            stickyInvoker = invoker;
        }
        return invoker;
    }

    protected Invoker<T> doSelect(LoadBalance loadbalance, Invocation invocation, List<Invoker<T>> invokers, List<Invoker<T>> selected){
        if (invokers == null || invokers.isEmpty())
            return null;
        if (invokers.size() == 1)
            return invokers.get(0);
        // If we only have two invokers, use round-robin instead.
        if (invokers.size() == 2 && selected != null && !selected.isEmpty()) {
            return selected.get(0) == invokers.get(0) ? invokers.get(1) : invokers.get(0);
        }
        loadbalance = new RandomLoadBalance();
        Invoker<T> invoker = loadbalance.select(invokers, getUrl(), invocation);
//        if ((selected != null && selected.contains(invoker))
//                || (!invoker.isAvailable() && getUrl() != null && availablecheck)) {
//            try {
//                Invoker<T> rinvoker = reselect(loadbalance, invocation, invokers, selected, availablecheck);
//                if (rinvoker != null) {
//                    invoker = rinvoker;
//                } else {
//                    //Check the index of current selected invoker, if it's not the last one, choose the one at index+1.
//                    int index = invokers.indexOf(invoker);
//                    try {
//                        //Avoid collision
//                        invoker = index < invokers.size() - 1 ? invokers.get(index + 1) : invoker;
//                    } catch (Exception e) {
//                        logger.warn(e.getMessage() + " may because invokers list dynamic change, ignore.", e);
//                    }
//                }
//            } catch (Throwable t) {
//                logger.error("cluster reselect fail reason is :" + t.getMessage() + " if can not solve, you can set cluster.availablecheck=false in url", t);
//            }
//        }
        return invoker;
    }


    @Override
    public Class<T> getInterface() {
        return directory.getInterface();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        LoadBalance loadBalance = new RandomLoadBalance();
        List<Invoker<T>> invokers = list(invocation);

        return doInvoke(invocation,invokers,loadBalance);
    }

    protected abstract Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadBalance);

    protected List<Invoker<T>> list(Invocation invocation) throws RpcException {
        List<Invoker<T>> invokers = directory.list(invocation);
        return invokers;
    }
}
