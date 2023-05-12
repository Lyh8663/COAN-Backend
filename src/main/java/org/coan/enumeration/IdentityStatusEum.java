package org.coan.enumeration;

import lombok.Getter;

@Getter
public enum IdentityStatusEum {
    NORMAL(1),
    BANNED(2),
    LOGOUT(3);
    private final int type;

    IdentityStatusEum(int type) {
        this.type = type;
    }
}
