package com.whoz_in.network_api.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class JdaConfig {

    @Bean
    public JDA jda(@Value("${discord.bot-token}") String discordBotToken) throws InterruptedException {
        return JDABuilder.createDefault(discordBotToken).build().awaitReady();
    }

    @Bean
    public TextChannel serverStatusChannel(JDA bot, @Value("${discord.server-status-channel-id}") String channelId) {
        return bot.getTextChannelById(channelId);
    }
}
