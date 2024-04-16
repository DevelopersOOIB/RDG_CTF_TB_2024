package ru.dvfu.demo.security.jwt;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtXorParserBuilder {

    private String key;

    public JwtXorParserBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public JwtParser build() {
        return new JwtParser(this.key);
    }
}
