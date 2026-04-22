package com.trecapps.gateway;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Pause/")
public class PauseController {

    @GetMapping
    String index(final Model model){
        return "pause";
    }
}
