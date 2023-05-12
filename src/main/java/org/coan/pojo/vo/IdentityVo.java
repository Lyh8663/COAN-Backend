package org.coan.pojo.vo;

import lombok.Data;
import org.coan.pojo.CryptoCurrency;
import org.coan.pojo.GameProperty;
import org.coan.pojo.Identity;
import org.coan.pojo.NFT;

import java.util.List;

@Data
public class IdentityVo extends Identity {
    private List<CryptoCurrency> cryptoCurrencies;

    private List<NFT> nftProperties;

    private List<GameProperty> gameProperties;
}
