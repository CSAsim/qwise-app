package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.Contact;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.ContactRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.mapper.ContactMapper;
import az.company.qwisedemoapp.model.dto.request.ContactRequestDto;
import az.company.qwisedemoapp.model.dto.response.ContactResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContactService {

    private final UserService userService;
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public Page<ContactResponseDto> findAllByUserId(Long userId, Pageable pageable) {
        Page<Contact> entities = contactRepository.findAllByUser_Id(userId, pageable);
        return contactMapper.toDtoPage(entities);
    }

    @Transactional
    public ContactResponseDto save(ContactRequestDto request) {
        log.info("Saving contact: {}", request);
        User user = userService.getEntity();
        Contact entity = Contact.builder()
                .user(user)
                .text(request.getText())
                .build();
        Contact savedEntity = contactRepository.save(entity);
        log.info("Contact saved: {}", savedEntity.getId());
        return contactMapper.toDto(savedEntity);
    }

    @Transactional
    public ContactResponseDto update(Long id, ContactRequestDto request) {
        log.info("Updating contact with id {}: {}", id, request);
        Contact entity = contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact not found"));
        entity.setText(request.getText());
        Contact savedEntity = contactRepository.save(entity);
        log.info("Contact updated: {}", savedEntity.getUpdatedDate());
        return contactMapper.toDto(savedEntity);
    }

    @Transactional
    public void delete(Long id) {
        contactRepository.deleteById(id);
        log.info("Contact with id {} deleted", id);
    }
}
