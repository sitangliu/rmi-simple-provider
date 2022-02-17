package com.bail.rpc.config.spring.registry;

import com.bail.rpc.config.spring.cluster.AbstractDirectory;
import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.StringUtils;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.protocol.Protocol;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;

import java.util.List;
import java.util.Map;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/29 15:19
 */
public class RegistryDirectory<T> extends AbstractDirectory<T> {

    private final String serviceKey;
    private final Class<T> serviceType;
    private final Map<String, String> queryMap;
    private volatile URL overrideDirectoryUrl;
    private final URL directoryUrl;
    private final boolean multiGroup;
    private final String[] serviceMethods;
    private Registry registry;
    private Protocol protocol;

    public RegistryDirectory(Class<T> serviceType, URL url) {
        super(url);
        if (serviceType == null)
            throw new IllegalArgumentException("service type is null.");
        if (url.getServiceKey() == null || url.getServiceKey().length() == 0)
            throw new IllegalArgumentException("registry serviceKey is null.");
        this.serviceType = serviceType;
        this.serviceKey = url.getServiceKey();
        this.queryMap = StringUtils.parseQueryString(url.getParameterAndDecoded(Constants.REFER_KEY));
        this.overrideDirectoryUrl = this.directoryUrl = url.setPath(url.getServiceInterface()).clearParameters().addParameters(queryMap).removeParameter(Constants.MONITOR_KEY);
        String group = directoryUrl.getParameter(Constants.GROUP_KEY, "");
        this.multiGroup = group != null && ("*".equals(group) || group.contains(","));
        String methods = queryMap.get(Constants.METHODS_KEY);
        this.serviceMethods = methods == null ? null : Constants.COMMA_SPLIT_PATTERN.split(methods);
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public Class<T> getServiceType() {
        return serviceType;
    }

    public Map<String, String> getQueryMap() {
        return queryMap;
    }

    public URL getOverrideDirectoryUrl() {
        return overrideDirectoryUrl;
    }

    public void setOverrideDirectoryUrl(URL overrideDirectoryUrl) {
        this.overrideDirectoryUrl = overrideDirectoryUrl;
    }

    public URL getDirectoryUrl() {
        return directoryUrl;
    }

    public boolean isMultiGroup() {
        return multiGroup;
    }

    public String[] getServiceMethods() {
        return serviceMethods;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public Class<T> getInterface() {
        return serviceType;
    }

    @Override
    public List<Invoker<T>> list(Invocation invocation) throws RpcException {
        return null;
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

    @Override
    protected List<Invoker<T>> doList(Invocation invocation) throws RpcException {
        return null;
    }
}
