package co.istad.mbanking.features.media;


import co.istad.mbanking.features.media.dto.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medias")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload-single")
    @ResponseStatus(HttpStatus.CREATED)
    MediaResponse uploadSingle(@RequestPart MultipartFile file){
        return mediaService.uploadSingle(file, "IMAGE");
    }

    @PostMapping("/upload-multiple")
    @ResponseStatus(HttpStatus.CREATED)
    List<MediaResponse> uploadMultiple(@RequestPart List<MultipartFile> files){
        return mediaService.uploadMultiple(files, "IMAGE");
    }

    @GetMapping("/{mediaName}")
    MediaResponse loadMediaByName(@PathVariable String mediaName){
        return mediaService.loadMediaByName(mediaName,"IMAGE");
    }

    @DeleteMapping("/{mediaName}")
    MediaResponse deleteMediaByName(@PathVariable String mediaName){
        return mediaService.deleteByName(mediaName,"IMAGE");
    }

    @GetMapping()
    List<MediaResponse> loadMedia(){
        return mediaService.loadMedias("IMAGE");
    }

    @GetMapping("/download/{name}")
    ResponseEntity downloadByName (@PathVariable String name) {
        return mediaService.downloadMediaByName(name , "IMAGE");
    }

}
