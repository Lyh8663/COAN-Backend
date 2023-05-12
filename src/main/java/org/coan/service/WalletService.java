package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.enumeration.ItemTypeEnum;
import org.coan.mapper.*;
import org.coan.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.coan.enumeration.ItemTypeEnum.CRYPTO;
import static org.coan.enumeration.ItemTypeEnum.GAME;

@Service
public class WalletService {

    @Autowired
    private CryptoCurrencyMapper currencyMapper;

    @Autowired
    private NFTMapper nftMapper;

    @Autowired
    private GamePropertyMapper gamePropertyMapper;

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private IdentityMapper identityMapper;

    public double getWalletValueById(long id) {
        LambdaQueryWrapper<CryptoCurrency> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CryptoCurrency::getUserId, id);
        List<CryptoCurrency> list = currencyMapper.selectList(lqw);
        double val = 0.0;
        for (CryptoCurrency currency : list) {
            double curVal = coinMapper.findProjectValue(currency.getCurrency());
            val += curVal * currency.getAmount();
        }
        return val;
    }

    public Page<CryptoCurrency> getCryptoPage(long id, int page, int size) {
        LambdaQueryWrapper<CryptoCurrency> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CryptoCurrency::getUserId, id);
        return currencyMapper.selectPage(new Page<>(page, size), lqw);
    }

    public Page<NFT> getNFTPage(long id, int page, int size) {
        LambdaQueryWrapper<NFT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NFT::getOwner, id);
        return nftMapper.selectPage(new Page<>(page, size), lqw);
    }

    public Page<GameProperty> getGamePropertyPage(long id, int page, int size) {
        LambdaQueryWrapper<GameProperty> lqw = new LambdaQueryWrapper<>();
        lqw.eq(GameProperty::getOwner, id);
        return gamePropertyMapper.selectPage(new Page<>(page, size), lqw);
    }

    public boolean completeTrade(CoanTrade trade) {
        boolean flag = true;
        if (trade.getTgtType() == CRYPTO.getType()) {
            consumCrypto(trade.getBuyer(), trade.getTarget(), trade.getTgtAmount());
            earnCrypto(trade.getSeller(), trade.getTarget(), trade.getTgtAmount());
        } else if (trade.getTgtType() == GAME.getType()) {
            consumGame(trade.getBuyer(), trade.getTarget(), trade.getTgtAmount());
            earnGame(trade.getSeller(), trade.getTarget(), trade.getTgtAmount());
        } else if (trade.getTgtType() == ItemTypeEnum.NFT.getType()){
            consumNFT(trade.getBuyer(), trade.getTarget(), trade.getTgtAmount());
            earnNFT(trade.getSeller(), trade.getTarget(), trade.getSrcAmount());
        }
        else {
            consumCash(trade.getBuyer(), trade.getTgtAmount());
            earnCash(trade.getSeller(), trade.getTgtAmount());
        }
        if (trade.getSrcType() == GAME.getType()) {
            consumGame(trade.getSeller(), trade.getSource(), trade.getSrcAmount());
            earnGame(trade.getBuyer(), trade.getSource(), trade.getSrcAmount());
        } else if (trade.getSrcType() == CRYPTO.getType()) {
            consumCrypto(trade.getSeller(), trade.getSource(), trade.getSrcAmount());
            earnCrypto(trade.getBuyer(), trade.getSource(), trade.getSrcAmount());
        } else if(trade.getSrcType() == ItemTypeEnum.NFT.getType()){
            consumNFT(trade.getSeller(), trade.getSource(), trade.getSrcAmount());
            earnNFT(trade.getBuyer(), trade.getSource(), trade.getSrcAmount());
        }
        else {
            consumCash(trade.getSeller(), trade.getTgtAmount());
            consumCash(trade.getBuyer(), trade.getTgtAmount());
        }
        return flag;
    }

    private boolean earnCash(Long seller, Double tgtAmount) {
        Identity identity = identityMapper.selectById(seller);
        if(identity == null) {
            return false;
        }
        identity.setCash(identity.getCash() + tgtAmount);
        return identityMapper.updateById(identity) > 0;
    }

    private boolean consumCash(Long buyer, Double tgtAmount) {
        Identity identity = identityMapper.selectById(buyer);
        if(identity == null) {
            return false;
        }
        identity.setCash(identity.getCash() - tgtAmount);
        return identityMapper.updateById(identity) > 0;
    }

    private boolean consumGame(long user, String item, double amount) {
        boolean flag;
        GameProperty gameProperty = gamePropertyMapper.findGamePropertyByKey(user, item);
        if(Objects.isNull(gameProperty)) {
            return false;
        }
        if (gameProperty.getAmount() == amount) {
            flag = gamePropertyMapper.deleteGameProperty(item, user) > 0;
        } else {
            gameProperty.setAmount(gameProperty.getAmount() - amount);
            flag = gamePropertyMapper.updateGameProperty(item, user, gameProperty.getAmount() - amount) > 0;
        }
        return flag;
    }

    private boolean consumCrypto(long user, String item, double amount) {
        boolean flag;
        CryptoCurrency currency = currencyMapper.findCryptoByKey(user, item);
        if(currency == null) {
            return false;
        }
        if (currency.getAmount() == amount) {
            flag = currencyMapper.deleteCryptoCurrency(item, user) > 0;
        } else {
            flag = currencyMapper.updateCryptoCurrency(item, user, currency.getAmount() - amount) > 0;
        }
        return flag;
    }

    private boolean consumNFT(long user, String item, double amount) {

        boolean flag;
        NFT nft = nftMapper.findNFTByKey(user, item);
        if(nft == null) {
            return false;
        }
        if (nft.getAmount() == amount) {
            flag = nftMapper.deleteNFT(item, user) > 0;
        } else {
            nft.setAmount(nft.getAmount() - amount);
            flag = nftMapper.updateNFT(item, user,nft.getAmount() - amount) > 0;
        }
        return flag;
    }

    private boolean earnGame(long user, String item, double amount) {
        boolean flag = true;
        GameProperty gameProperty = gamePropertyMapper.findGamePropertyByKey(user, item);
        if (Objects.isNull(gameProperty)) {
            gameProperty = new GameProperty();
            gameProperty.setName(item);
            gameProperty.setOwner(user);
            gameProperty.setAmount(amount);
            flag = gamePropertyMapper.insert(gameProperty) > 0;
        } else {
            flag = gamePropertyMapper.updateGameProperty(item, user,
                    gameProperty.getAmount() + amount) > 0;
        }
        return flag;

    }

    private boolean earnNFT(long user, String item, double amount) {
        boolean flag = true;
        NFT nft = nftMapper.findNFTByKey(user, item);
        if (Objects.isNull(nft)) {
            nft = new NFT();
            nft.setNft(item);
            nft.setOwner(user);
            nft.setAmount(amount);
            flag = nftMapper.insert(nft) > 0;
        } else {
            nft.setAmount(nft.getAmount() + amount);
            flag = nftMapper.updateNFT(item, user, nft.getAmount() + amount) > 0;
        }
        return flag;
    }

    private boolean earnCrypto(long user, String item, double amount) {
        boolean flag = true;
        CryptoCurrency currency = currencyMapper.findCryptoByKey(user, item);
        if (Objects.isNull(currency)) {
            currency = new CryptoCurrency();
            currency.setAmount(amount);
            currency.setUserId(user);
            currency.setCurrency(item);
            flag = currencyMapper.insert(currency) > 0;
        } else {
            flag = currencyMapper.updateCryptoCurrency(item, user, currency.getAmount() + amount) > 0;
        }
        return flag;
    }



}
