package io.github.hylexus.jt808.samples.annotation.entity.resp;

import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.data.resp.BytesValueWrapper;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.hylexus.jt.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.data.MsgDataType.DWORD;

/**
 * @author hylexus
 * Created At 2019-10-16 10:43 下午
 */
@Data
@Accessors(chain = true)
@Jt808RespMsgBody(respMsgId = 0x8103, desc = "设置终端参数")
public class RespTerminalSettings {

    @CommandField(order = 2)
    private List<ParamItem> paramList;

    @CommandField(order = 1, targetMsgDataType = BYTE)
    private int totalParamCount;

    @Data
    @Accessors(chain = true)
    @SuppressWarnings("rawtypes")
    public static class ParamItem {
        @CommandField(order = 1, targetMsgDataType = DWORD)
        private int msgId;

        @CommandField(order = 2, targetMsgDataType = BYTE)
        private int bytesCountOfContentLength;

        @CommandField(order = 3)
        private BytesValueWrapper msgContent;

        public ParamItem(int msgId, BytesValueWrapper msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.bytesCountOfContentLength = msgContent.getAsBytes().length;
        }
    }

    @Getter
    public enum ItemType implements MsgType {
        /**
         * 0x0001 DWORD 终端心跳发送间隔，单位为（s）
         * 0x0010 STRING 主服务器 APN，无线通信拨号访问点
         * 0x0013 STRING 主服务器地址,IP或域名
         * 0x0017 STRING 备份服务器地址,IP或域名
         * 0x0018 DWORD 服务器TCP端口
         */
        heart(0x0001, "DWORD", "终端心跳发送间隔，单位为s"),
        apn(0x0010, "STRING", "主服务器APN"),
        ip_main(0x0013, "STRING", "主服务器地址IP或域名"),
        ip_second(0x0017, "STRING", "备份服务器地址IP或域名"),
        ip_port(0x0018, "DWORD", "服务器TCP端口"),

        ;

        private static final Map<Integer, ItemType> mapping = new HashMap<>(values().length);
        private static final Map<String, ItemType> mapping2 = new HashMap<>(values().length);

        static {
            for (ItemType type : values()) {
                mapping.put(type.msgId, type);
                mapping2.put(HexStringUtils.int2HexString(type.msgId, 4), type);
            }
        }

        private final int msgId;
        private final String dataType;
        private final String desc;

        ItemType(int msgId, String dataType, String desc) {
            this.msgId = msgId;
            this.dataType = dataType;
            this.desc = desc;
        }

        @Override
        public Optional<MsgType> parseFromInt(int msgId) {
            return Optional.ofNullable(mapping.get(msgId));
        }

        public static Optional<ItemType> parseFromString(String msgId) {
            return Optional.ofNullable(mapping2.get(msgId));
        }
    }


}
