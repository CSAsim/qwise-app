package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.RatingRequestDto;
import az.company.qwisedemoapp.model.dto.response.LeaderBoardResponseDto;
import az.company.qwisedemoapp.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/ratings")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/leaderboard")
    public ResponseEntity<LeaderBoardResponseDto> getLeaderBoard(
            @RequestBody RatingRequestDto request,
            @PageableDefault Pageable pageable) {
        LeaderBoardResponseDto responseDto = ratingService.getLeaderBoard(request, pageable);
        return ResponseEntity.ok(responseDto);
    }
}
