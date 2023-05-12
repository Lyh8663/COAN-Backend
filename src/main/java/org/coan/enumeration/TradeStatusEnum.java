package org.coan.enumeration;

import lombok.Getter;

@Getter
public enum TradeStatusEnum {
    ONGOING(1),
    FINISHED(2),
    ERROR(3);

    private final int type;

    TradeStatusEnum(int type) {
        this.type = type;
    }
}
