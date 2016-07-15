package com.company.data.dao.stub.proto;

import com.company.data.model.proto.DataItem;
import com.company.util.DataIntegrityException;
import com.company.util.GenericHolder;

import java.util.*;

/**
 * Created by Yevhen on 15.07.2016.
 */
public class StubDao<T extends DataItem> extends GenericHolder<T> {
    private static final String CANNOT_FIND_DATA_ITEM_PATTERN = "<%s>: Cannot find item with id <%d>!";

    private SortedSet<T> dataItemSet = new TreeSet<>();

    public SortedSet<T> getDataItemSet() {
        return dataItemSet;
    }

    public void setDataItemSet(SortedSet<T> dataItemSet) {
        this.dataItemSet = dataItemSet;
    }

    protected SortedSet<T> readDataItemSet() {
        return getDataItemSet();
    }

    protected void saveDataItemSet(SortedSet<T> dataItemSet) {
        setDataItemSet(dataItemSet);
    }

    private int generateId() {
        SortedSet<T> dataItemSet = readDataItemSet();

        return (dataItemSet != null && !dataItemSet.isEmpty()) ? dataItemSet.last().getId() + 1 : 1;
    }

    @Override
    protected T newObject() {
        T result = super.newObject();
        result.setId(generateId());

        return result;
    }

    private T saveOrUpdate(T dataItem) {
        if (dataItem != null) {
            int dataItemId = dataItem.getId();
            if (dataItemId <= 0) {
                dataItem.setId(generateId());
            }
            SortedSet<T> dataItemSet = readDataItemSet();

            if (!dataItemSet.add(dataItem)) {
                T oldDataItem = findDataItem(dataItemId);
                if (oldDataItem == null) {
                    throw new DataIntegrityException(String.format(CANNOT_FIND_DATA_ITEM_PATTERN, getEntityName(),
                            dataItemId));
                } else {
                    dataItemSet.remove(oldDataItem);
                    dataItemSet.add(dataItem);
                }
            }

            saveDataItemSet(dataItemSet);
        }

        return dataItem;
    }

    protected T saveOrUpdate(String name) {
        T dataItem = newObject();
        dataItem.setName(name);

        return saveOrUpdate(dataItem);
    }

    protected List<T> findAllDataItems() {
        return new ArrayList<>(readDataItemSet());
    }

    protected T addDataItem(T dataItem) {
        dataItem.setId(generateId());

        return saveOrUpdate(dataItem);
    }

    protected T findDataItem(int dataItemId) {
        T result = null;

        SortedSet<T> dataItemSet = readDataItemSet();
        if (dataItemSet != null) {
            Optional<T> tOptional = dataItemSet.stream().filter(t -> (t.getId() == dataItemId)).findFirst();
            if (tOptional.isPresent()) {
                result = tOptional.get();
            }
        }

        return result;
    }

    protected T updateDataItem(T dataItem) {
        return saveOrUpdate(dataItem);
    }

    protected boolean deleteDataItem(int dataItemId) {
        boolean result = false;

        SortedSet<T> dataItemSet = readDataItemSet();
        if (dataItemSet != null) {
            T dataItem = findDataItem(dataItemId);
            if (dataItem != null) {
                result = dataItemSet.remove(dataItem);
                saveDataItemSet(dataItemSet);
            }
        }

        return result;
    }

    protected boolean deleteDataItem(T dataItem) {
        return (dataItem != null) && deleteDataItem(dataItem.getId());
    }
}
