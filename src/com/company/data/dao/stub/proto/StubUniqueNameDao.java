package com.company.data.dao.stub.proto;

import com.company.data.model.proto.DataItem;

import java.util.Optional;
import java.util.SortedSet;

/**
 * Created by Yevhen on 13.07.2016.
 */

public class StubUniqueNameDao<T extends DataItem> extends StubDao<T> {
    protected T saveOrUpdate(String name) {
        T result = null;

        SortedSet<T> dataItemSet = readDataItemSet();
        if (dataItemSet != null) {
            Optional<T> tOptional = dataItemSet.stream().filter(t -> (t.getName().equals(name))).findFirst();
            if (tOptional.isPresent()) {
                result = tOptional.get();
            }
        }
        if (result == null) {
            result = super.saveOrUpdate(name);
        }

        return result;
    }
}
