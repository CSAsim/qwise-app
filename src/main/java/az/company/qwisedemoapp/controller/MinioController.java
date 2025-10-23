package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.enums.FileCategory;
import az.company.qwisedemoapp.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/minio")
public class MinioController {

    private final MinioService minioService;

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            FileCategory fileCategory) {
        return ResponseEntity.ok().body(minioService.uploadFile(file, fileCategory));
    }

    @PutMapping("/update-file")
    public ResponseEntity<String> updateFile(
            @RequestParam("file") MultipartFile newFile,
            @RequestParam String oldFileUrl,
            FileCategory fileCategory) {
        return ResponseEntity.ok()
                .body(minioService.updateFile(newFile, oldFileUrl, fileCategory));
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<Void> deleteFile(@RequestParam String url) {
        minioService.deleteFile(url);
        return ResponseEntity.noContent().build();
    }
}
