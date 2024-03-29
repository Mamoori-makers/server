package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.WillRequest;
import com.mamoori.mamooriback.api.dto.WillResponse;
import com.mamoori.mamooriback.api.service.WillService;
import com.mamoori.mamooriback.auth.service.JwtService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class WillController {

    private final WillService willService;
    private final JwtService jwtService;

    @GetMapping("/will")
    public ResponseEntity<WillResponse> getWill(HttpServletRequest request) {
        log.debug("getWill called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));
        log.debug("getWill -> email : {}", email);
        WillResponse willResponse = willService.getWillByEmail(email);

        return ResponseEntity.ok()
                .body(willResponse);
    }

    @PutMapping("/will")
    public ResponseEntity putWill(@RequestBody WillRequest willRequest,
                                   HttpServletRequest request) {
        log.debug("putWill called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));
        log.debug("putWill -> email : {}", email);
        willService.putWill(email, willRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/will")
    public ResponseEntity deleteWill(HttpServletRequest request) {
        log.debug("deleteWill called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));
        log.debug("deleteWill -> email : {}", email);
        willService.deleteWill(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
