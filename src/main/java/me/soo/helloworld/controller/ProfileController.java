package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.condition.SearchConditionsRequest;
import me.soo.helloworld.model.user.UserProfile;
import me.soo.helloworld.model.user.UserProfiles;
import me.soo.helloworld.service.ProfileService;
import me.soo.helloworld.util.Pagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Value("${max.page.size:30}")
    private int pageSize;

    @LoginRequired
    @GetMapping("/{targetId}")
    public UserProfile getUserProfile(@PathVariable String targetId,
                                      @CurrentUser String userId) {
        return profileService.getUserProfile(targetId, userId);
    }

    @LoginRequired
    @GetMapping
    public List<UserProfiles> getUserProfiles(@CurrentUser String userId,
                                              @RequestParam(required = false) Integer cursor) {
        return profileService.getUserProfiles(userId, Pagination.create(cursor, pageSize));
    }

    @LoginRequired
    @PostMapping("/on/conditions")
    public List<UserProfiles> searchUserProfiles(@Valid @RequestBody SearchConditionsRequest conditionsRequest,
                                               @CurrentUser String userId,
                                               @RequestParam(required = false) Integer cursor) {
        return profileService.searchUserProfiles(conditionsRequest, userId, Pagination.create(cursor, pageSize));
    }
}
