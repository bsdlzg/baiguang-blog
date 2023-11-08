package com.bsdlzg.blog.im;



import com.alibaba.fastjson.JSONObject;
import com.bsdlzg.blog.vo.WsMessageVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WebSocketInfoService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketInfoService.class);

    /**
     * 清除会话信息
     * @param channel
     */
    public void clearSession(Channel channel) {
        String userId = NettyAttrUtil.getUserId(channel);
        // 清除会话信息
        SessionHolder.channelGroup.remove(channel);
        SessionHolder.channelMap.remove(userId);
    }

    /**
     * 广播 ping 信息
     */
    public void sendPing() {
        WsMessageVO webSocketMessage = new WsMessageVO();
        webSocketMessage.setCode(MessageCodeConstant.PING_MESSAGE_CODE);
        String message = JSONObject.toJSONString(webSocketMessage);
        TextWebSocketFrame tws = new TextWebSocketFrame(message);
        SessionHolder.channelGroup.writeAndFlush(tws);
    }

    /**
     * 从缓存中移除Channel，并且关闭Channel
     */
    public void scanNotActiveChannel() {
        Map<String, Channel> channelMap = SessionHolder.channelMap;
        if (channelMap.size() == 0) {
            return;
        }
        for (Channel channel : channelMap.values()) {
            long lastHeartBeatTime = NettyAttrUtil.getLastHeartBeatTime(channel);
            long intervalMillis = (System.currentTimeMillis() - lastHeartBeatTime);
            if (!channel.isOpen()
                    || !channel.isActive()
                    || intervalMillis > 90000L) {
                channelMap.remove(channel);
                SessionHolder.channelGroup.remove(channel);
                if (channel.isOpen() || channel.isActive()) {
                    channel.close();
                }
            }
        }
    }

}

