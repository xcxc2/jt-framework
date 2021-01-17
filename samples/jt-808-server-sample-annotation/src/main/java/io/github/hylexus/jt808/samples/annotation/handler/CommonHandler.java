package io.github.hylexus.jt808.samples.annotation.handler;

import com.dudu.huoyun.service.DuduService;
import io.github.hylexus.jt.annotation.msg.handler.Jt808ExceptionHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandlerMapping;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.BuiltinEmptyRequestMsgBody;
import io.github.hylexus.jt808.msg.req.BuiltinTerminalCommonReplyMsgBody;
import io.github.hylexus.jt808.msg.resp.VoidRespMsgBody;
import io.github.hylexus.jt808.samples.annotation.entity.req.*;
import io.github.hylexus.jt808.samples.annotation.entity.req.demo01.LocationUploadRequestMsgBodyDemo01;
import io.github.hylexus.jt808.samples.annotation.entity.resp.RegisterReplyMsgBody;
import io.github.hylexus.jt808.samples.annotation.entity.resp.ServerCommonReplyMsgBody;
import io.github.hylexus.jt808.samples.annotation.serivce.TerminalService;
import io.github.hylexus.jt808.samples.annotation.util.MockDbUtil;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.github.hylexus.jt.data.msg.BuiltinJt808MsgType.CLIENT_HEART_BEAT;
import static io.github.hylexus.jt808.samples.annotation.config.Jt808MsgType.*;

/**
 * @author hylexus
 * Created At 2020-02-01 2:54 下午
 */
@Slf4j
@Jt808RequestMsgHandler
@Component
public class CommonHandler {

    @Autowired
    private TerminalService terminalService;//= SpringUtils.getBean(TerminalService.class);

    @Autowired
    private Jt808SessionManager jt808SessionManager;
    @Autowired
    private DuduService duduService;

    @Jt808RequestMsgHandlerMapping(msgType = 0x0100, desc = "终端注册")
    public RegisterReplyMsgBody processRegisterMsg(RegisterMsg msg, RequestMsgHeader header) {
        log.info("终端注册 terminalId = {}, carIdentifier = {}", header.getTerminalId(), msg.getCarIdentifier());
        log.info("{}", msg.toString());
        //065044634533
        //todo 记录注册信息 RegisterMsg，
        // 同时生成设备授权码 authCode, 保存到库中或redis中
        String terminalId = header.getTerminalId();
        String authCode = header.getTerminalId();
        MockDbUtil.save("registerMsg", terminalId, authCode);
        byte result = 0;
        return new RegisterReplyMsgBody(header.getFlowId(), result, authCode);
    }

    // 此处会覆盖内置的鉴权消息处理器(如果启用了的话)
    @Jt808RequestMsgHandlerMapping(msgType = 0x0102, desc = "终端鉴权")
    public ServerCommonReplyMsgBody processAuthMsg(AuthRequestMsgBody msgBody, RequestMsgHeader header, Jt808Session abstractSession, Session session) {
        log.info("处理鉴权消息 terminalId = {}, authCode = {}", header.getTerminalId(), msgBody.getAuthCode());
        if (header.getTerminalId().equals(System.getProperty("debug-terminal-id"))) {
            throw new UnsupportedOperationException("terminal [" + header.getTerminalId() + "] was locked.");
        }
        log.info("{}", terminalService);
        Optional<Jt808Session> sessionInfo = jt808SessionManager.findByTerminalId(header.getTerminalId());
        assert sessionInfo.isPresent();
        assert sessionInfo.get() == abstractSession;
        // 不建议直接使用Session，建议使用Jt808Session
        assert sessionInfo.get() == session;

        //todo 校验 设备标识和授权码是否存在，根据情况返回是否鉴权成功
        String terminalId = header.getTerminalId();
        String authCode = msgBody.getAuthCode();
        MockDbUtil.save("authMsg", terminalId, authCode);

        // return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_AUTH);
        byte result = 0;
        return new ServerCommonReplyMsgBody(header.getFlowId(), CLIENT_AUTH.getMsgId(), result);
    }

    // 处理MsgId为0x0200的消息
    @Jt808RequestMsgHandlerMapping(msgType = 0x0200)
    public ServerCommonReplyMsgBody processLocationMsg(
            Jt808Session session, RequestMsgMetadata metadata,
            RequestMsgHeader header,
            // LocationUploadRequestMsgBody msgBody,
            //LocationUploadRequestMsgBodyDemo02 msgBody,
            LocationUploadRequestMsgBodyDemo01 msgBody) {

        assert header.getMsgId() == BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId();
        assert session.getTerminalId().equals(header.getTerminalId());
        assert session.getTerminalId().equals(metadata.getHeader().getTerminalId());
        assert metadata.getHeader() == header;

        log.info("处理位置上报消息 terminalId = {}, msgBody = {}", header.getTerminalId(), msgBody);

        //todo 位置信息入库记录： 设备码ID，经纬度 位置信息
        String terminalId = header.getTerminalId();
        Double lng = msgBody.getLng();//经度
        Double lat = msgBody.getLat();//纬度
        String time = msgBody.getTime();
        MockDbUtil.save("locationMsg", terminalId, time, lng, lat);
        duduService.saveGPS(terminalId,lng,lat,time);

        // return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
        byte result = 0;
        return new ServerCommonReplyMsgBody(header.getFlowId(), CLIENT_LOCATION_INFO_UPLOAD.getMsgId(), result);
    }

    @Jt808RequestMsgHandlerMapping(msgType = 0x0900)
    public RespMsgBody process0x0900(PassthroughPack body) {
        log.info("bug-fix --> 0x0900: {}", body);
        return VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT;
    }

    // 此处会覆盖内置的终端通用应答消息处理器(如果启用了的话)
    @Jt808RequestMsgHandlerMapping(msgType = 0x0001)
    public void processTerminalCommonReplyMsg(Jt808Session session, BuiltinTerminalCommonReplyMsgBody msgBody) {
        log.info("处理终端通用应答消息 terminalId = {}, msgBody = {}", session.getTerminalId(), msgBody);
    }

    @Jt808RequestMsgHandlerMapping(msgType = 0x0002)
    public ServerCommonReplyMsgBody processHeatBeatMsg(BuiltinEmptyRequestMsgBody heartBeatMsgBody, RequestMsgHeader header) {
        byte result = 0;
        return new ServerCommonReplyMsgBody(header.getFlowId(), CLIENT_HEART_BEAT.getMsgId(), result);
    }

    //@Jt808RequestMsgHandlerMapping(msgType = 0x0002)
    //public void processHeatBeatMsg(BuiltinEmptyRequestMsgBody heartBeatMsgBody, RequestMsgHeader header) {
    //    log.info("心跳消息，不回复试试？");
    //}

    @Jt808RequestMsgHandlerMapping(msgType = 0x0704)
    public ServerCommonReplyMsgBody processMsg0704(RequestMsgHeader header, Msg0704 msg) {

        return new ServerCommonReplyMsgBody(header.getFlowId(), CLIENT_LOCATION_INFO_BATCH_UPLOAD.getMsgId(), (byte) 0);
    }

    @Jt808RequestMsgHandlerMapping(msgType = 0x0104)
    public ServerCommonReplyMsgBody processMsg0104(Msg0104 msg, RequestMsgHeader header) {
        log.info("processMsg0104: {}", msg);
        byte result = 0;
        return new ServerCommonReplyMsgBody(header.getFlowId(), REQ_QUERY_LOCK_PARAM_REPLY.getMsgId(), result);
    }

    @Jt808ExceptionHandler
    public RespMsgBody processUnsupportedOperationException(RequestMsgMetadata metadata, Jt808Session session, UnsupportedOperationException exception) {
        assert metadata.getHeader().getTerminalId().equals(session.getTerminalId());
        log.error("出异常了:{}", exception.getMessage());
        return VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT;
    }

    @Jt808ExceptionHandler
    public RespMsgBody processException(RequestMsgMetadata metadata, Jt808Session session, Exception exception) {
        assert metadata.getHeader().getTerminalId().equals(session.getTerminalId());
        log.info("exception", exception);
        return VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT;
    }
}
