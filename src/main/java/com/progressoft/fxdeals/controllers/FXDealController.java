package com.progressoft.fxdeals.controllers;


import com.progressoft.fxdeals.model.dtos.FXDealDto;
import com.progressoft.fxdeals.services.FXDealService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor

@RestController
@RequestMapping(path = "/api/FXdeals")
public class FXDealController {

    private final FXDealService fxDealService;

    @PostMapping
    public ResponseEntity<FXDealDto> create(@Valid @RequestBody FXDealDto newFXDealDto) {
        return new ResponseEntity<>(
                fxDealService.create(newFXDealDto),
                HttpStatus.CREATED
        );
    }
}