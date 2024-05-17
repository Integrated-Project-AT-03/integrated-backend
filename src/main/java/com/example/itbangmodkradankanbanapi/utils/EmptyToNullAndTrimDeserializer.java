package com.example.itbangmodkradankanbanapi.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import java.io.IOException;

public class EmptyToNullAndTrimDeserializer extends StringDeserializer {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String result = super.deserialize(parser, ctx).trim();
        if ( result.isEmpty()) {
            return null;
        }
        return result.trim();
    }
}