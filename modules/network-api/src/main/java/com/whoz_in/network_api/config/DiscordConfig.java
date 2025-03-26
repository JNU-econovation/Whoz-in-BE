package com.whoz_in.network_api.config;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("prod")
public class DiscordConfig {

    @Bean
    public JDA jda(@Value("${discord.bot-token}") String discordBotToken) throws InterruptedException {
        return JDABuilder.createDefault(discordBotToken).build().awaitReady();
    }

    @Bean
    public TextChannel serverStatusChannel(JDA bot, @Value("${discord.server-status-channel-id}") String channelId) {
        return bot.getTextChannelById(channelId);
    }
}
