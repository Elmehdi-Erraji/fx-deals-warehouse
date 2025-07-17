package com.progressoft.fxdeals.services.implementation;

import com.progressoft.fxdeals.exceptions.RequestAlreadyExistException;
import com.progressoft.fxdeals.model.dtos.FXDealDto;
import com.progressoft.fxdeals.model.entities.FXDeal;
import com.progressoft.fxdeals.repositories.FXDealRepository;

import com.progressoft.fxdeals.services.FXDealService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import org.modelmapper.ModelMapper;

@AllArgsConstructor
@Service
public class FXDealServiceImpl implements FXDealService {

    private final FXDealRepository fxDealRepository;
    private final ModelMapper modelMapper;

    @Override
    public FXDealDto create(final FXDealDto newFXDealDto) {
        if(fxDealRepository.existsById(newFXDealDto.getId()))
            throw new RequestAlreadyExistException("Request is already imported.");
        return modelMapper.map(
                fxDealRepository.save(
                        modelMapper.map(newFXDealDto, FXDeal.class)
                ),
                FXDealDto.class
        );
    }

}