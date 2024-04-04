package co.istad.mbanking.features.media;

import co.istad.mbanking.features.media.dto.MediaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    MediaResponse uploadSingle(MultipartFile file,  String folderName);

    List<MediaResponse> uploadMultiple(List<MultipartFile> files, String folderName);

    MediaResponse loadMediaByName(String name, String folderName);

    MediaResponse deleteByName(String name, String folderName);

    List<MediaResponse> loadMedias(String folderName);

    ResponseEntity downloadMediaByName(String mediaName, String folderName);

}
