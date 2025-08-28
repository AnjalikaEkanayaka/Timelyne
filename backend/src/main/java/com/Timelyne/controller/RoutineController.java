package com.Timelyne.controller;

import com.Timelyne.dto.RoutineRequest;
import com.Timelyne.dto.RoutineResponse;
import com.Timelyne.service.RoutineService;
import com.Timelyne.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;
    private final JwtUtil jwtUtil;

    public RoutineController(RoutineService routineService, JwtUtil jwtUtil) {
        this.routineService = routineService;
        this.jwtUtil = jwtUtil;
    }

    // Helper to extract the email from "Authorization: Bearer <token>"
    private String emailFromAuth(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractEmail(token);
    }

    // ------ Create ------
    @PostMapping
    public ResponseEntity<RoutineResponse> create(
            @RequestHeader("Authorization") String auth,
            @RequestBody RoutineRequest req
    ) {
        String email = emailFromAuth(auth);
        RoutineResponse created = routineService.create(email, req);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ------ Get My Routines ------
    @GetMapping("/me")
    public ResponseEntity<List<RoutineResponse>> listMine(
            @RequestHeader("Authorization") String auth
    ) {
        String email = emailFromAuth(auth);
        return ResponseEntity.ok(routineService.listMine(email));
    }

    // ------ Update ------
    @PutMapping("/{id}")
    public ResponseEntity<RoutineResponse> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id,
            @RequestBody RoutineRequest req
    ) {
        String email = emailFromAuth(auth);
        return ResponseEntity.ok(routineService.update(email, id, req));
    }

    // ------ Delete ------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id
    ) {
        String email = emailFromAuth(auth);
        routineService.delete(email, id);
        return ResponseEntity.noContent().build();
    }
}
