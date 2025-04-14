package com.umi.tradestar.fix;

import com.umi.tradestar.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.NewOrderSingle;

/**
 * QuickFIX/J application implementation for handling FIX messages
 * 
 * @author VrushankPatel
 */
@Slf4j
@Component
public class FixApplication implements Application {
    private final OrderService orderService;
    private final FixMessageHandler fixMessageHandler;

    public FixApplication(OrderService orderService, FixMessageHandler fixMessageHandler) {
        this.orderService = orderService;
        this.fixMessageHandler = fixMessageHandler;
    }

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("FIX session created: {}", sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        log.info("FIX session logged on: {}", sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        log.info("FIX session logged out: {}", sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        log.info("FIX admin message: {}", message);
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) {
        log.info("FIX admin message received: {}", message);
    }

    @Override
    public void toApp(Message message, SessionID sessionID) {
        log.info("FIX app message: {}", message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) {
        log.info("FIX app message received: {}", message);
        try {
            String msgType = message.getHeader().getString(MsgType.FIELD);
            if (MsgType.ORDER_SINGLE.equals(msgType)) {
                fixMessageHandler.handleNewOrderSingle((NewOrderSingle) message, sessionID);
            }
        } catch (Exception e) {
            log.error("Error processing FIX message: {}", e.getMessage());
        }
    }

    public void sendMessage(Message message, SessionID sessionID) throws SessionNotFound {
        Session.sendToTarget(message, sessionID);
    }

    private void sendReject(Message message, SessionID sessionID, String reason) throws FieldNotFound {
        Message reject = createReject(message, reason);
        try {
            sendMessage(reject, sessionID);
        } catch (SessionNotFound e) {
            log.error("Failed to send reject message: {}", e.getMessage(), e);
        }
    }

    private Message createReject(Message message, String reason) throws FieldNotFound {
        ExecutionReport reject = new ExecutionReport(
            new OrderID(message.getString(ClOrdID.FIELD)),
            new ExecID(generateExecId()),
            new ExecTransType(ExecTransType.NEW),
            new ExecType(ExecType.REJECTED),
            new OrdStatus(OrdStatus.REJECTED),
            new Symbol(message.getString(Symbol.FIELD)),
            new Side(message.getChar(Side.FIELD)),
            new LeavesQty(0),
            new CumQty(0),
            new AvgPx(0)
        );
        reject.set(new Text(reason));
        return reject;
    }

    private String generateExecId() {
        return "EXE" + System.currentTimeMillis();
    }
} 