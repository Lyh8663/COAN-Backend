package org.coan.enumeration;

import lombok.Getter;

@Getter
public enum OfferTypeEnum {
    SWAP_IN(1),
    SWAP_OUT(2),
    BUY_IN(3),
    BUY_OUT(4);
    private final int type;

    OfferTypeEnum(int type) {
        this.type = type;
    }

}
