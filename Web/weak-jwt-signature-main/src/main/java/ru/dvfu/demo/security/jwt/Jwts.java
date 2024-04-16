package ru.dvfu.demo.security.jwt;


public class Jwts {

    public static JwtXorBuilder builder() {
        return new JwtXorBuilder();
    }

    public static JwtXorParserBuilder parserBuilder() {
        return new JwtXorParserBuilder();
    }
}
