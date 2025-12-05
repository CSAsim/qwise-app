package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.Rating;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.RatingRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.request.RatingRequestDto;
import az.company.qwisedemoapp.model.dto.response.LeaderBoardResponseDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.response.RatingResponseDto;
import az.company.qwisedemoapp.model.enums.RatingStatus;
import az.company.qwisedemoapp.model.enums.RatingType;
import az.company.qwisedemoapp.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;

    public Page<RatingResponseDto> findAllUserRatings(RatingRequestDto request, Pageable pageable) {
        Page<Rating> pages = ratingRepository.findAllUserRatingsByType(request.getPeriodStart(), request.getType(), pageable);
        return pages.map(rp -> {
            RatingResponseDto responseDto = new RatingResponseDto();
            responseDto.setId(rp.getId());
            responseDto.setFullName(rp.getUser().getFullName());
            responseDto.setProfilePictureUrl(rp.getUser().getProfilePictureUrl());
            responseDto.setScore(rp.getScore());
            return responseDto;
        });
    }

    public LeaderBoardResponseDto getLeaderBoard(RatingRequestDto request, Pageable pageable) {
        Page<RatingResponseDto> ratingPage =
                findAllUserRatings(request, pageable);

        Long userId = AuthService.getCurrentUserId();

        Integer myRank = ratingRepository.findUserRank(
                userId,
                request.getType(),
                request.getPeriodStart()
        );

        if (myRank == null) {
            myRank = 0;
        }

        PageableResponseDto<RatingResponseDto> pageableResponseDto = PageableResponseDto.of(
                ratingPage.getContent(),
                ratingPage.getPageable().getPageNumber(),
                ratingPage.getSize(),
                ratingPage.getTotalElements(),
                ratingPage.getTotalPages()
        );

        Rating rating = ratingRepository.findByUserIdAndType(userId, request.getType())
                .orElseThrow(() -> new NotFoundException("Rating not found for user"));

        RatingResponseDto ratingResponseDto = RatingResponseDto.builder()
                .id(rating.getId())
                .score(rating.getScore())
                .fullName(rating.getUser().getFullName())
                .profilePictureUrl(rating.getUser().getProfilePictureUrl())
                .build();

        return LeaderBoardResponseDto.builder()
                .ratings(pageableResponseDto)
                .myRank(myRank)
                .myRating(ratingResponseDto)
                .build();
    }

    @Transactional
    public void createInitialRatingsForUser(User user) {

        LocalDateTime now = LocalDateTime.now();

        Rating daily = build(user, RatingType.DAILY, now);
        Rating weekly = build(user, RatingType.WEEKLY, now);
        Rating monthly = build(user, RatingType.MONTHLY, now);

        ratingRepository.saveAll(
                List.of(daily, weekly, monthly)
        );
    }

    @Transactional
    public void rolloverExpiredRatings() {
        LocalDateTime now = LocalDateTime.now();

        List<Rating> expiredRatings = ratingRepository.findExpiredActiveRatings(now);
        if (!expiredRatings.isEmpty()) {
            log.info("Rolling over {} expired ratings", expiredRatings.size());
            return;
        }
        ratingRepository.deactivateExpiredRatings(now);

        List<Rating> newRatings = new ArrayList<>();

        for (Rating old : expiredRatings) {
            RatingType type = old.getType();

            LocalDateTime newStart = calculateNewPeriodStart(type, now);
            LocalDateTime newEnd = calculateNewPeriodEnd(type, newStart);

            Rating fresh = new Rating();
            fresh.setUser(old.getUser());
            fresh.setType(type);
            fresh.setScore(0);
            fresh.setStatus(RatingStatus.ACTIVE);
            fresh.setPeriodStart(newStart);
            fresh.setPeriodEnd(newEnd);

            newRatings.add(fresh);
        }
        ratingRepository.saveAll(newRatings);
    }

    @Transactional
    public void updateRating(Long userId, Integer score) {
        List<Rating> ratings = ratingRepository.findByUserId(userId);
        ratings.forEach(rating -> rating.setScore(rating.getScore() + score));
        ratingRepository.saveAll(ratings);
    }

    private Rating build(User user, RatingType type, LocalDateTime now) {

        LocalDateTime start = calculateNewPeriodStart(type, now);
        LocalDateTime end = calculateNewPeriodEnd(type, start);

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setType(type);
        rating.setScore(0);
        rating.setStatus(RatingStatus.ACTIVE);
        rating.setPeriodStart(start);
        rating.setPeriodEnd(end);

        return rating;
    }

    private LocalDateTime calculateNewPeriodStart(RatingType type, LocalDateTime now) {
        return switch (type) {
            case DAILY -> now.toLocalDate().atStartOfDay();
            case WEEKLY -> now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
            case MONTHLY -> now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        };
    }

    private LocalDateTime calculateNewPeriodEnd(RatingType type, LocalDateTime newStart) {
        return switch (type) {
            case DAILY -> newStart.plusDays(1);
            case WEEKLY -> newStart.plusWeeks(1);
            case MONTHLY -> newStart.plusMonths(1);
        };
    }
}
