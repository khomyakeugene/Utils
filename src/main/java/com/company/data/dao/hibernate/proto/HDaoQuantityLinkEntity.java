package com.company.data.dao.hibernate.proto;

import com.company.data.model.proto.LinkObject;

/**
 * Created by Yevhen on 11.06.2016.
 */
public abstract class HDaoQuantityLinkEntity <T extends LinkObject> extends HDaoAmountLinkEntity<T>  {
    private Integer selectCurrentQuantity(int firstId, int secondId) {
        return selectCurrentAmount(firstId, secondId).intValue();
    }

    protected String amountToString(float amount) {
        return String.valueOf(Math.round(amount));
    }

    protected void increaseQuantity(int firstId, int secondId, int increasePortion) {
        increaseAmount(firstId, secondId, increasePortion);
    }

    protected void decreaseQuantity(int firstId, int secondId, int decreasePortion) {
        decreaseAmount(firstId, secondId, decreasePortion);
    }
}
