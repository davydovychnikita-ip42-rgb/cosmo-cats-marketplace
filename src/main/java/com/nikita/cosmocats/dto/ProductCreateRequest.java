package com.nikita.cosmocats.dto;

import com.nikita.cosmocats.validation.CosmicWordCheck;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
    @NotNull @Size(min = 3, max = 50)
    @CosmicWordCheck 
    String name,
    
    @NotNull @Positive
    Double price 
) {}