package com.whoz_in.main_api.command.member.presentation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.whoz_in.domain.member.model.Position;
import java.io.IOException;

public class PositionDeserializer extends JsonDeserializer<Position> {
    @Override
    public Position deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String value = p.getText();
        return Position.findByName(value); // Position 로직 재활용
    }
}