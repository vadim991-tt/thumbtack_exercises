package net.thumbtack.school.notes.endpoint;


import net.thumbtack.school.notes.dto.response.server.ServerSettingsDtoResponse;
import net.thumbtack.school.notes.service.DebugService;
import net.thumbtack.school.notes.utils.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugEndpoint {

    private DebugService debugService;

    @Autowired
    public DebugEndpoint(DebugService debugService) {
        this.debugService = debugService;
    }

    @GetMapping("/settings")
    public ServerSettingsDtoResponse getServerSettings() {
        return debugService.getServerSettings();
    }

    @PostMapping("/clear")
    public void clearDataBase(){
        debugService.clear();
    }

}
