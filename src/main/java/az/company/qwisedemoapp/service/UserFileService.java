package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.file.File;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.UserFile;
import az.company.qwisedemoapp.domain.repository.file.FileRepository;
import az.company.qwisedemoapp.domain.repository.UserFileRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.AlreadyExistsException;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.filter.AdminResourceSpecificationFilter;
import az.company.qwisedemoapp.filter.PurchasedResourceSpecificationFilter;
import az.company.qwisedemoapp.mapper.UserFileMapper;
import az.company.qwisedemoapp.model.constants.ExceptionMessages;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.model.dto.response.UserFileResponseDto;
import az.company.qwisedemoapp.service.auth.AuthService;
import az.company.qwisedemoapp.util.SortUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFileService {

    private final UserFileRepository userFileRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final UserFileMapper userFileMapper;

    public Page<UserFileResponseDto> findAllUserFiles(FilteredRequestDto request, Pageable pageable) {
        Long studentId = AuthService.getCurrentUserId();
        request.setUserId(studentId);
        Specification<UserFile> specification = new PurchasedResourceSpecificationFilter<UserFile>().byFilters(request);
        Sort sorting = SortUtil.resolveSort(request.getSort());
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
        Page<UserFile> pages = userFileRepository.findAll(specification, sortedPageable);
        return userFileMapper.toDtoPage(pages);
    }

    @Transactional
    public UserFileResponseDto assignFileToStudent(Long fileId) {
        Long currentUserId = AuthService.getCurrentUserId();
        User student = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Student" + ExceptionMessages.NOT_FOUND));
        File file = fileRepository.findPublishedPacketsById(fileId)
                .orElseThrow(() -> new NotFoundException("File" + ExceptionMessages.NOT_FOUND));
        file.setSoldCount(file.getSoldCount() + 1);
        fileRepository.save(file);
        boolean alreadyExists = userFileRepository.existsByUserIdAndResourceId(student.getId(), file.getId());
        if (alreadyExists) {
            throw new AlreadyExistsException("File already assigned to student");
        }
        UserFile userFile = UserFile.builder()
                .user(student)
                .resource(file)
                .build();
        student.addEnrolledFile(userFile);
        UserFile savedUserFile = userFileRepository.save(userFile);
        return userFileMapper.toDto(savedUserFile);
    }

    @Transactional
    public void removeFileFromStudent(Long fileId) {
        log.info("Removing file from student {}", fileId);
        Long authorId = AuthService.getCurrentUserId();
        User user =  userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User" + ExceptionMessages.NOT_FOUND));
        UserFile userFile = user.getUserEnrolledFiles()
                .stream()
                .filter(up -> up.getResource().getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User file" + ExceptionMessages.NOT_FOUND));
        user.removeEnrolledFile(userFile);
        userRepository.save(user);
        log.info("File removed from student {}", fileId);
    }
}
