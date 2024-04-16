package ru.dvfu.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dvfu.demo.property.CtfProperties;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final CtfProperties ctfProperties;

    public String getFlag() {
        return ctfProperties.getFlag();
    }
}
