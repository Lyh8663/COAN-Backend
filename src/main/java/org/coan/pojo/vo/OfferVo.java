package org.coan.pojo.vo;

import lombok.Data;
import org.coan.pojo.Offer;
import org.coan.pojo.Payment;

import java.util.List;

@Data
public class OfferVo extends Offer {
    List<Payment> payments;
}
