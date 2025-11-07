package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.enums.FileCategory;
import az.company.qwisedemoapp.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/minio")
public class MinioController {

    private final MinioService minioService;

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam FileCategory fileCategory) {
        return ResponseEntity.ok().body(minioService.uploadFile(file, fileCategory));
    }

    @PostMapping(value = "/update-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateFile(
            @RequestPart("file") MultipartFile newFile,
            @RequestParam String oldFileUrl,
            @RequestParam FileCategory fileCategory) {
        return ResponseEntity.ok()
                .body(minioService.updateFile(newFile, oldFileUrl, fileCategory));
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<Void> deleteFile(@RequestPart String url) {
        minioService.deleteFile(url);
        return ResponseEntity.noContent().build();
    }
}
