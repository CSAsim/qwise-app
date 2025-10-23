package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findAllByUser_Id(Long userId, Pageable pageable);
}
