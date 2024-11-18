package com.projectforge.template.infrastructure.api.exceptionhandler;

import java.util.List;
import lombok.Builder;

@Builder
public record ErrorResponseDTO(int status, String message, List<ErrorDTO> errors) {
    public record ErrorDTO(String message) {
    }
}

