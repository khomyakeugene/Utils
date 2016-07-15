package com.company.data.dao.stub.http.proto;

import com.company.data.dao.stub.proto.StubDao;
import com.company.data.model.proto.DataItem;

import javax.servlet.ServletContext;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Yevhen on 13.07.2016.
 */
public class HttpStubDao<T extends DataItem> extends StubDao<T> {
    private static final String SET_VAR_SUFFIX = "Set";

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private Object getAttribute(String attributeName) {
        return (servletContext == null) ? null : servletContext.getAttribute(attributeName);
    }

    private void setAttribute(String attributeName, Object attributeValue) {
        if (servletContext != null) {
            servletContext.setAttribute(attributeName, attributeValue);
        }
    }

    private String getEntitySetVariableName() {
        return getEntityName() + SET_VAR_SUFFIX;
    }

    @Override
    protected void saveDataItemSet(SortedSet<T> dataItemSet) {
        setAttribute(getEntitySetVariableName(), dataItemSet);
    }

    @Override
    protected SortedSet<T> readDataItemSet() {
        SortedSet<T> result = (SortedSet<T>)getAttribute(getEntitySetVariableName());

        return (result == null) ? new TreeSet<>() : result;
    }
}
