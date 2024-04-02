package co.istad.mbanking.features.media.dto;

import lombok.Builder;

@Builder
public record MediaResponse(
        String name,
        String contentType,// PNG, JPG, ...
        String uri, // link access to the image
        Long size,
        String extension
) {
}
