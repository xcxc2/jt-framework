package com.dudu.huoyun.wsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实时定位消息推送后台
 * 访问地址：ws://192.168.10.40:8808/location/12345678
 * 前后端交互的类实现消息的接收推送(自己发送给自己)
 *
 * @ServerEndpoint(value = "/location/{terminalId}") 前端通过此URI和后端交互，建立连接
 */
@Slf4j
@ServerEndpoint(value = "/location/{terminalId}")
@Component
public class LocationSocket {

    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * 存放所有在线的客户端
     */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private static Map<String, List<Session>> terminalSessions = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("terminalId") String terminalId, Session session) {
        onlineCount.incrementAndGet(); // 在线数加1
        clients.put(session.getId(), session);
        addSession(terminalId, session);
        log.info("[sid={},terminalId={}]有新连接加入：当前推送设备会话为：{}", session.getId(), terminalId, onlineCount.get());
    }

    private void addSession(String terminalId, Session session) {
        List<Session> list = terminalSessions.get(terminalId);
        if (list == null) {
            list = new ArrayList<>();
            terminalSessions.put(terminalId, list);
        }
        list.add(session);
    }

    private void removeSession(String terminalId, Session session) {
        List<Session> list = terminalSessions.get(terminalId);
        if (list != null) {
            list.remove(session);
        }
    }

    private List<Session> getSession(String terminalId) {
        return terminalSessions.get(terminalId);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("terminalId") String terminalId, Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        clients.remove(session.getId());
        removeSession(terminalId, session);
        log.info("[sid={},terminalId={}]有一连接关闭，当前推送设备会话为：{}", session.getId(), terminalId, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(@PathParam("terminalId") String terminalId,String message, Session session) {
        log.info("[sid={},terminalId={}]服务端收到客户端的消息:{}", session.getId(),terminalId, message);
        sendMessage(terminalId,message);
    }

    @OnError
    public void onError(@PathParam("terminalId") String terminalId,Session session, Throwable error) {
        log.info("[sid={},terminalId={}]发生错误", session.getId(),terminalId);
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String terminalId, String message) {
        List<Session> list = getSession(terminalId);
        if (list == null || list.size() == 0){
            log.info("[terminalId={}]无客户端连接，不发送消息：{}", terminalId, message);
        }
        for (Session session : list) {
            try {
                log.info("[sid={},terminalId={}]发送消息：{}", session.getId(),terminalId, message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送消息失败：{}", e);
            }
        }

    }
}
