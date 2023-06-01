package org.coan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.pojo.ExchangeRequest;
import org.coan.service.ExchangeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/6/1 20:38
 */
@RestController
@RequestMapping("/exchangeRequest")
public class ExchangeRequestController {
    @Autowired
    ExchangeRequestService exchangeRequestService;

    @GetMapping("/exchangeRequests/{senderId}")
    public Page<ExchangeRequest> selectExchangeRequestBySenderId(@PathVariable("senderId") String senderId,
                                                                 @RequestParam(defaultValue = "0") Integer pageNum,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        return exchangeRequestService.selectExchangeRequestBySenderId(Long.valueOf(senderId), pageNum, pageSize);
    }
}
