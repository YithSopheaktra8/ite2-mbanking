package co.istad.mbanking.features.media.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record MediaResponse(
        String name,
        String contentType,// PNG, JPG, ...
        String uri,
        @JsonInclude(JsonInclude.Include.NON_NULL) // if size is null not response to user
        Long size, // link access to the image
        String extension
) {
}
