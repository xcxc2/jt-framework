package io.github.hylexus.jt808.samples.annotation.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.hylexus.jt.data.resp.DwordBytesValueWrapper;
import io.github.hylexus.jt.data.resp.StringBytesValueWrapper;
import io.github.hylexus.jt808.samples.annotation.entity.resp.RespTerminalSettings;

import java.util.ArrayList;
import java.util.List;

public class TscUtil {


    /**
     * * 0x0001 DWORD �ն��������ͼ������λΪ��s��
     * * 0x0010 STRING �������� APN ������ͨ�Ų��ŷ��ʵ㡣
     * * 0x0013 STRING ����������ַ,IP������
     * * 0x0017 STRING ���ݷ�������ַ ,IP ,IP������
     * * 0x0018 DWORD ������TCPTCP
     * <p>
     * dataArr[ key: value:]
     */
    public static RespTerminalSettings getParam8103(JSONArray dataArr) {
        RespTerminalSettings param = new RespTerminalSettings();
        List<RespTerminalSettings.ParamItem> paramList = new ArrayList<>();
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject obj = (JSONObject) dataArr.getJSONObject(i);
            String msgId = obj.getString("key");
            RespTerminalSettings.ItemType type = RespTerminalSettings.ItemType.parseFromString(msgId).get();
            if (type == null) {
                throw new UnsupportedOperationException("this msgId[" + msgId + "] should be write in RespTerminalSettings.ItemType");
            }
            RespTerminalSettings.ParamItem item = null;
            switch (type.getDataType()) {
                case "DWORD":
                    item = new RespTerminalSettings.ParamItem(type.getMsgId(), DwordBytesValueWrapper.of(obj.getInteger("value")));
                case "STRING":
                    item = new RespTerminalSettings.ParamItem(type.getMsgId(), StringBytesValueWrapper.of(obj.getString("value")));
                    //todo others
            }
            if (item == null) {
                throw new UnsupportedOperationException("this msgId[" + msgId + "] should be write in TscUtil");
            }
            paramList.add(item);
        }
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        return param;

    }
}
