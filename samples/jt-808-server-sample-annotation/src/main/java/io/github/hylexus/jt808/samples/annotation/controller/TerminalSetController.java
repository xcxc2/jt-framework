package io.github.hylexus.jt808.samples.annotation.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.github.hylexus.jt.command.CommandWaitingPool;
import io.github.hylexus.jt.command.Jt808CommandKey;
import io.github.hylexus.jt.data.resp.DwordBytesValueWrapper;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt808.dispatcher.CommandSender;
import io.github.hylexus.jt808.msg.resp.CommandMsg;
import io.github.hylexus.jt808.samples.annotation.entity.resp.RespTerminalSettings;
import io.github.hylexus.jt808.samples.annotation.util.MockDbUtil;
import io.github.hylexus.jt808.samples.annotation.util.Result;
import io.github.hylexus.jt808.samples.annotation.util.TscUtil;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.session.Jt808SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt808.samples.annotation.config.Jt808MsgType.*;


/**
 * @author hylexus
 * Created At 2019-10-06 9:12 下午
 */
@Slf4j
@RestController
@RequestMapping("/tsc")
public class TerminalSetController {

    @Autowired
    private Jt808SessionManager jt808SessionManager;
    @Autowired
    private CommandSender commandSender;

    /**
     * 设置终端参数 0x8103
     * jsonParams {
     * "terminalId":"",
     * "timeout":"",
     * "data":[
     * {"key":"0x0013",value:"193.34.343.34"},
     * {"key":"0x0017",value:"193.34.343.34"},
     * {"key":"0x0018",value:"6808"}
     * ]
     * }
     * <p>
     * 0x0001 DWORD 终端心跳发送间隔，单位为（s）
     * 0x0010 STRING 主服务器 APN，无线通信拨号访问点
     * 0x0013 STRING 主服务器地址,IP或域名
     * 0x0017 STRING 备份服务器地址,IP或域名
     * 0x0018 DWORD 服务器TCP端口
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/set8103")
    public Object set8103(@RequestBody JSONObject params) throws Exception {
        log.info("set8103: {}", params);
        String terminalId = params.getString("terminalId");
        Long timeout = params.getLong("timeout");
        timeout = timeout == null ? 5L : timeout;
        JSONArray dataArr = params.getJSONArray("data");
        if (dataArr.isEmpty()) {
            return Result.failMessage("指令数据为空");
        }
        final Object resp;
        try {
            RespTerminalSettings param = TscUtil.getParam8103(dataArr);
            // simulatePutResultByAnotherThread(commandKey);

            // 【下发消息】的消息类型为: RESP_TERMINAL_PARAM_SETTINGS (0x8103)  --> RespTerminalSettings的类注解上指定了下发类型
            // 客户端对该【下发消息】的回复消息类型为: CLIENT_COMMON_REPLY (0x0001)
            CommandMsg commandMsg = CommandMsg.of(terminalId, CLIENT_COMMON_REPLY, param);
            resp = commandSender.sendCommandAndWaitingForReply(commandMsg, timeout, TimeUnit.SECONDS);
            log.info("set8103 resp: {}", resp);
            MockDbUtil.save("set8103",terminalId, dataArr.toJSONString());
            return Result.doneData(resp);
        } catch (JtSessionNotFoundException e) {
            log.info("set8103 err: {}", e.getMessage(), e);
            return Result.failMessage("失败:设备未连接！");
        }catch (Exception e) {

            log.info("set8103 err: {}", e.getMessage(), e);
            return Result.failMessage("失败:"+e.getClass().getSimpleName()+"-" + e.getMessage());
        }

    }

    @GetMapping("/test")
    public Object test(
            @RequestParam(required = false, name = "terminalId", defaultValue = "13717861955") String terminalId,
            @RequestParam(required = false, name = "timeout", defaultValue = "5") Long timeout) throws Exception {

        RespTerminalSettings param = new RespTerminalSettings();
        List<RespTerminalSettings.ParamItem> paramList = Lists.newArrayList(
                new RespTerminalSettings.ParamItem(0x0029, DwordBytesValueWrapper.of(100))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        // simulatePutResultByAnotherThread(commandKey);

        // 【下发消息】的消息类型为: RESP_TERMINAL_PARAM_SETTINGS (0x8103)  --> RespTerminalSettings的类注解上指定了下发类型
        // 客户端对该【下发消息】的回复消息类型为: CLIENT_COMMON_REPLY (0x0001)
        CommandMsg commandMsg = CommandMsg.of(terminalId, CLIENT_COMMON_REPLY, param);
        final Object resp = commandSender.sendCommandAndWaitingForReply(commandMsg, timeout, TimeUnit.SECONDS);
        log.info("resp: {}", resp);
        return resp;
    }

    @GetMapping("/query-terminal-properties")
    public Object queryTerminalProperties(
            @RequestParam(required = false, name = "terminalId", defaultValue = "13717861955") String terminalId,
            @RequestParam(required = false, name = "timeout", defaultValue = "5") Long timeout) throws Exception {

        // 【下发消息】的消息体为空
        // 【下发消息】消息的类型为: RESP_QUERY_TERMINAL_PROPERTIES (0x8107)
        // 客户端对该【下发消息】的回复消息类型为: CLIENT_QUERY_TERMINAL_PROPERTIES_REPLY (0x0107)
        CommandMsg commandMsg = CommandMsg.emptyRespMsgBody(terminalId, CLIENT_QUERY_TERMINAL_PROPERTIES_REPLY, RESP_QUERY_TERMINAL_PROPERTIES);
        return commandSender.sendCommandAndWaitingForReply(commandMsg, timeout, TimeUnit.SECONDS);
    }

    private Jt808Session getSession(String terminalId) {
        return jt808SessionManager.findByTerminalId(terminalId)
                .orElseThrow(() -> new JtSessionNotFoundException(terminalId));
    }

    private void simulatePutResultByAnotherThread(Jt808CommandKey commandKey) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                CommandWaitingPool.getInstance().putIfNecessary(commandKey, "result for " + commandKey.getKeyAsString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
