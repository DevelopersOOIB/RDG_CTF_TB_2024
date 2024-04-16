package org.example.hardauthorizationv2.service;

import org.example.hardauthorizationv2.domain.model.Role;
import org.example.hardauthorizationv2.domain.model.User;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class MainService {
    public String check(String input, User user) {
        input = formatInput(input);
        if ("flag".equals(input) && !Objects.equals(Role.ROLE_ADMIN, user.getRole())) {
            return "Не-a меня не проведешь, ты не Admin";
        } else if ("flag".equals(input) && Objects.equals(Role.ROLE_ADMIN, user.getRole())) {
            return "NB2HI4DTHIXS653XO4XHS33VOR2WEZJOMNXW2L3XMF2GG2C4H53FYPLEKF3TI5ZZK5TVQY2R";
        }
        return input + " нет у меня такого";
    }

    private static String formatInput(String input) {
        return input.replaceAll("^\"|\"$", "");
    }
}
