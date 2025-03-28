package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
public class NetworkInterfaceStatusNotifier {
    private final String tag;
    private final NetworkInterfaceManager networkInterfaceManager;
    private final TextChannel serverStatusChannel;
    @Nullable private Message niStatusMessage;

    public NetworkInterfaceStatusNotifier(
            @Value("${room-name}") String roomName,
            NetworkInterfaceManager networkInterfaceManager,
            TextChannel serverStatusChannel) {
        this.tag = "["+roomName+"]"+" network interfaces status";
        this.networkInterfaceManager = networkInterfaceManager;
        this.serverStatusChannel = serverStatusChannel;
        this.niStatusMessage = retrieveTagMessage();
    }

    @EventListener(NetworkInterfaceStatusEvent.class)
    public void handle(){
        String content =
                tag +
                "\n\n" +
                networkInterfaceManager.toString() +
                "\n\n마지막 상태 변경 시각: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        if (niStatusMessage == null) {
            // 메시지가 없으면 새 메시지 전송
            serverStatusChannel.sendMessage(content).queue(message -> niStatusMessage = message);
        } else {
            // 이미 메시지가 있으면 편집
            niStatusMessage.editMessage(content).queue();
        }
    }

    private Message retrieveTagMessage() {
        List<Message> messages = serverStatusChannel.getHistory().retrievePast(10).complete();
        Optional<Message> opt = messages.stream()
                .filter(msg -> msg.getContentRaw().startsWith(tag))
                .findFirst();
        return opt.orElse(null);
    }

}
