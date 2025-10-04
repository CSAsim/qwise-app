package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.File;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.FileRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.exception.TokenExpiredException;
import az.company.qwisedemoapp.filter.FileSpecificationFilter;
import az.company.qwisedemoapp.mapper.FileMapper;
import az.company.qwisedemoapp.model.dto.response.FileResponseDto;
import az.company.qwisedemoapp.model.enums.FileStatus;
import az.company.qwisedemoapp.model.dto.request.CreateFileRequestDto;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.model.dto.request.UpdateFileRequestDto;
import az.company.qwisedemoapp.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileMapper fileMapper;

    public Page<FileResponseDto> findAllFiles(FilteredRequestDto request, Pageable pageable) {
        Specification<File> specification = FileSpecificationFilter.byFilters(request);
        Page<File> pages = fileRepository.findAll(specification, pageable);
        return fileMapper.toDtoPage(pages);
    }

    public FileResponseDto findById(Long id) {
        File file = fileRepository.findByIdWithStatus(id)
                .orElseThrow(() -> new NotFoundException(String.format("File with id %s not found", id)));
        return fileMapper.toDto(file);
    }

    @Transactional
    public FileResponseDto createFile(CreateFileRequestDto request) {
        Long authorId = AuthService.getCurrentUserId();
        if(authorId == null) {
            throw new TokenExpiredException("Token expired!");
        }
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        log.info("Creating file: {}", request);
        File file = fileMapper.toEntity(request);
        file.setStatus(FileStatus.ACTIVE);
        author.addFile(file);
        File savedFile = fileRepository.save(file);
        log.info("File created: {}", savedFile);
        return fileMapper.toDto(savedFile);
    }

    @Transactional
    public FileResponseDto publishFile(Long fileId) {
        log.info("Publishing file with id {}", fileId);
        File file = fileRepository.findByIdWithStatus(fileId)
                .orElseThrow(() -> new NotFoundException(String.format("File with id %s not found or deleted", fileId)));
        file.setStatus(FileStatus.PUBLISHED);
        File savedFile = fileRepository.save(file);
        log.info("File published: {}", savedFile);
        return fileMapper.toDto(savedFile);
    }

    @Transactional
    public FileResponseDto updateFile(Long id, UpdateFileRequestDto request) {
        log.info("Updating file with id {}", id);
        File oldFile = fileRepository.findByIdWithStatus(id)
                .orElseThrow(()
                        -> new NotFoundException(String.format("File with id %s not found or deleted", id)));
        File file = fileMapper.toEntity(oldFile, request);
        File savedFile = fileRepository.save(file);
        log.info("File updated: {}", savedFile);
        return fileMapper.toDto(savedFile);
    }

    @Transactional
    public void deleteFile(Long id) {
        log.info("Deleting file with id {}", id);
        Long authorId = AuthService.getCurrentUserId();
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("File with id %s not found", id)));
        author.removeFile(file);
        fileRepository.delete(file);
    }
}
