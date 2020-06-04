package site.imcu.tape.uitls;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author: MengHe
 * @date: 2020/3/28 20:22
 */
@Slf4j
@Component
public class PushUtil implements InitializingBean {
    @Value("${jPush.appKey}")
    private String appKey;
    @Value("${jPush.masterSecret}")
    private String masterSecret;


    private PushClient pushClient;

    public void push(String message,String alias){
        PushPayload pushPayload = pushAllNotify(message,alias);
        try {
            PushResult pushResult = pushClient.sendPush(pushPayload);
            log.info(JSONObject.toJSONString(pushResult));
        } catch (APIConnectionException | APIRequestException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 全平台指定用户推送
     *
     * @param message 消息
     * @param alias 对象
     * @return 消息
     */
    public PushPayload pushAllNotify(String message, String... alias) {
        PushPayload.Builder payload = PushPayload.newBuilder()
                .setPlatform(Platform.all());
        payload.setAudience(StringUtils.isEmpty(alias) ? Audience.all() : Audience.alias(alias));
        payload.setNotification(Notification.newBuilder().setAlert(message).build()
        );
        payload.setOptions(Options.newBuilder().setApnsProduction(true).build());
        return payload.build();
    }

    @Override
    public void afterPropertiesSet() {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey);
        pushClient = jPushClient.getPushClient();
    }
}
