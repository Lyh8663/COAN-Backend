package org.coan.enumeration;

import lombok.Getter;

@Getter
public enum ItemTypeEnum {

    NFT(1),
    GAME(2),
    CRYPTO(3),
    CASH(4);

    private final int type;

    ItemTypeEnum(int type) {
        this.type = type;
    }
}
