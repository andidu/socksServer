package com.andidu.socks_server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final SocksDao socksDao;

    public Controller(SocksDao socksDao) {
        this.socksDao = socksDao;
    }

    @GetMapping("/api/socks")
    ResponseEntity<?> get(@RequestParam String color, @RequestParam String operation, @RequestParam Integer cottonPart) {
        //TODO: change response code
        int sum = 0;
        for (SocksDB s: socksDao.getSocks(color, operation, cottonPart)) {
            sum += s.getQuantity();
        }
        return new ResponseEntity<>(sum, HttpStatus.valueOf(200));
    }

    @PostMapping(value = "/api/socks/income")
    void income(@RequestBody Socks socks) {
        //TODO: change response code
        socksDao.addSocks(socks);
    }

    @PostMapping("/api/socks/outcome")
    void outcome(@RequestBody Socks socks) {
        //TODO: change response code
        socksDao.subtractSocks(socks);
    }
}
