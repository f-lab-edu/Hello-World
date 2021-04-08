package me.soo.helloworld.integration;

import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;
import me.soo.helloworld.model.condition.SearchConditionsRequest;
import me.soo.helloworld.model.language.LanguageData;
import me.soo.helloworld.model.user.UserProfiles;
import me.soo.helloworld.service.BlockUserService;
import me.soo.helloworld.service.LanguageService;
import me.soo.helloworld.service.ProfileService;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.util.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static me.soo.helloworld.TestCountries.SOUTH_KOREA;
import static me.soo.helloworld.TestCountries.UNITED_KINGDOM;
import static me.soo.helloworld.TestLanguages.ENGLISH;
import static me.soo.helloworld.TestLanguages.KOREAN;
import static me.soo.helloworld.TestTowns.ABERDEEN;
import static me.soo.helloworld.TestTowns.LIVERPOOL;
import static me.soo.helloworld.TestUsersFixture.*;
import static me.soo.helloworld.util.validator.AgeRangeValidator.MIN_AGE_BOUND;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ITSearchProfilesTest {

    @Autowired
    UserService userService;

    @Autowired
    LanguageService languageService;

    @Autowired
    BlockUserService blockUserService;

    @Autowired
    ProfileService profileService;

    Pagination pagination;

    List<LanguageData> nativeEnglish;

    List<LanguageData> nativeKorean;

    List<LanguageData> beginnerKorean;

    List<LanguageData> beginnerEnglish;

    List<LanguageData> intermediateKorean;

    Set<LanguageLevel> allLevelsIncludingNativeLevel;

    Set<LanguageLevel> allLevelsWithoutNativeLevel;

    List<String> englishSpeakers;

    List<String> koreanBeginners;

    List<String> unitedKingdomOrigins;

    List<String> unitedKingdomResidents;

    List<String> koreaResidents;

    @BeforeEach
    public void initPagination() {
        pagination = Pagination.create(null, 5);
    }

    @BeforeEach
    public void initLanguages() {
        nativeEnglish = new ArrayList<>();
        nativeEnglish.add(new LanguageData(ENGLISH, LanguageLevel.NATIVE));

        nativeKorean = new ArrayList<>();
        nativeKorean.add(new LanguageData(KOREAN, LanguageLevel.NATIVE));

        beginnerKorean = new ArrayList<>();
        beginnerKorean.add(new LanguageData(KOREAN, LanguageLevel.BEGINNER));

        beginnerEnglish = new ArrayList<>();
        beginnerEnglish.add(new LanguageData(ENGLISH, LanguageLevel.BEGINNER));

        intermediateKorean = new ArrayList<>();
        intermediateKorean.add(new LanguageData(KOREAN, LanguageLevel.INTERMEDIATE));
    }

    @BeforeEach
    public void initLanguageLevelConditions() {
        allLevelsIncludingNativeLevel = Arrays.stream(LanguageLevel.values()).collect(Collectors.toUnmodifiableSet());
        allLevelsWithoutNativeLevel = Arrays.stream(LanguageLevel.values()).filter(level -> !LanguageLevel.NATIVE.equals(level))
                .collect(Collectors.toUnmodifiableSet());
    }

    @BeforeEach
    public void initEnglishSpeakers() {
        englishSpeakers = new ArrayList<>();
        englishSpeakers.add(TENS_MALE_FROM_UK_LIVING_LONDON.getUserId());
        englishSpeakers.add(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId());
        englishSpeakers.add(SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId());
    }

    @BeforeEach
    public void initKoreanBeginners() {
        koreanBeginners = new ArrayList<>();
        koreanBeginners.add(TENS_MALE_FROM_UK_LIVING_LONDON.getUserId());
        koreanBeginners.add(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId());
    }

    @BeforeEach
    public void initCountryResidents() {
        unitedKingdomOrigins = new ArrayList<>();
        unitedKingdomOrigins.add(TENS_MALE_FROM_UK_LIVING_LONDON.getUserId());
        unitedKingdomOrigins.add(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId());
        unitedKingdomOrigins.add(SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId());

        unitedKingdomResidents = new ArrayList<>();
        unitedKingdomResidents.add(TENS_MALE_FROM_UK_LIVING_LONDON.getUserId());
        unitedKingdomResidents.add(SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId());

        koreaResidents = new ArrayList<>();
        koreaResidents.add(TENS_FEMALE_FROM_KOREA.getUserId());
        koreaResidents.add(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId());
    }

    private void insertUsers() {
        userService.userSignUp(TENS_MALE_FROM_UK_LIVING_LONDON);
        userService.userSignUp(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL);
        userService.userSignUp(SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL);

        userService.userSignUp(TENS_FEMALE_FROM_KOREA);

        userService.userSignUp(CURRENT_USER);
    }

    private void insertLanguages() {
        languageService.addLanguages(TENS_MALE_FROM_UK_LIVING_LONDON.getUserId(), nativeEnglish, LanguageStatus.NATIVE);
        languageService.addLanguages(TENS_MALE_FROM_UK_LIVING_LONDON.getUserId(), beginnerKorean, LanguageStatus.LEARNING);

        languageService.addLanguages(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId(), nativeEnglish, LanguageStatus.NATIVE);
        languageService.addLanguages(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId(), beginnerKorean, LanguageStatus.LEARNING);

        languageService.addLanguages(SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId(), nativeEnglish, LanguageStatus.NATIVE);
        languageService.addLanguages(SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId(), intermediateKorean, LanguageStatus.LEARNING);

        languageService.addLanguages(TENS_FEMALE_FROM_KOREA.getUserId(), nativeKorean, LanguageStatus.NATIVE);
        languageService.addLanguages(TENS_FEMALE_FROM_KOREA.getUserId(), beginnerEnglish, LanguageStatus.LEARNING);
    }

    @Test
    @DisplayName("조건을 설정해서 사용자를 검색할 때 학습중인 언어 레벨에 NATIVE 가 포함될 시 검색에 실패하며 InvalidLanguageLevelException 이 발생합니다.")
    public void searchUserProfilesByOnlyLanguagesFailWhenNativeIncludedInLearningLevel() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsIncludingNativeLevel)
                .build();

        assertThrows(InvalidLanguageLevelException.class, () -> {
            profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);
        });
    }

    @Test
    @DisplayName("구사 언어와 학습 언어정보를 설정해서 사용자를 검색하면 해당 언어정보와 매칭하는 사용자만 반환합니다.")
    public void searchingUserProfilesByCertainLanguageNameShowsOnlyMatchingUsers() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 3);
        assertTrue(matchingProfiles.stream().noneMatch(user -> TENS_FEMALE_FROM_KOREA.getUserId().equals(user.getUserId())));
        assertTrue(matchingProfiles.stream().allMatch(englishSpeaker -> englishSpeakers.contains(englishSpeaker.getUserId())));
    }


    @Test
    @DisplayName("구체적인 언어레벨 또한 설정해서 사용자를 검색하면 학습 언어의 레벨이 매칭하는 사용자의 프로필만 반환합니다. - 레벨: BEGINNER")
    public void searchingUserProfilesByCertainLanguageNameAndBeginnerLevelShowsOnlyMatchingUsers() {
        insertUsers();
        insertLanguages();
        Set<LanguageLevel> beginnerLevel = new HashSet<>();
        beginnerLevel.add(LanguageLevel.BEGINNER);

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(beginnerLevel)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 2);
        assertTrue(matchingProfiles.stream().noneMatch(user -> SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId().equals(user.getUserId())));
        assertTrue(matchingProfiles.stream().allMatch(user -> koreanBeginners.contains(user.getUserId())));
    }

    @Test
    @DisplayName("구체적인 언어레벨 또한 설정해서 사용자를 검색하면 학습 언어의 레벨이 매칭하는 사용자의 프로필만 반환합니다. - 레벨: Intermediate")
    public void searchingUserProfilesByCertainLanguageNameAndIntermediateLevelShowsOnlyMatchingUsers() {
        insertUsers();
        insertLanguages();
        Set<LanguageLevel> intermediateLevel = new HashSet<>();
        intermediateLevel.add(LanguageLevel.INTERMEDIATE);

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(intermediateLevel)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 1);
        assertTrue(matchingProfiles.stream().noneMatch(user -> koreanBeginners.contains(user.getUserId())));
        assertTrue(matchingProfiles.stream().allMatch(user -> SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId().equals(user.getUserId())));
    }

    @Test
    @DisplayName("필수 조건외에 선택조건인 출신 국가가 조건으로 설정될 경우 해당 조건에 해당하는 사용자의 프로필만 반환합니다. - 출신: UK")
    public void searchingUserProfilesWithOriginCountryAsConditionShowsOnlyMatchingUsers() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .originCountry(UNITED_KINGDOM)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 3);
        assertTrue(matchingProfiles.stream().allMatch(user -> unitedKingdomOrigins.contains(user.getUserId())));
        assertTrue(matchingProfiles.stream().noneMatch(user -> TENS_FEMALE_FROM_KOREA.getUserId().equals(user.getUserId())));
    }

    @Test
    @DisplayName("필수 조건외에 선택조건으로 출신 국가와 거주 국가가 모두 설정될 경우 해당 조건에 해당하는 사용자의 프로필만 반환합니다. - 출신: UK, 거주: Korea")
    public void searchingUserProfilesWithOriginAndLivingCountryAsConditionShowsOnlyMatchingUsers() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .originCountry(UNITED_KINGDOM)
                .livingCountry(SOUTH_KOREA)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 1);
        assertTrue(matchingProfiles.stream().allMatch(user -> FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId().equals(user.getUserId())));
    }

    @Test
    @DisplayName("필수 조건외에 선택조건으로 출신 국가와 거주 국가 그리고 거주 도시가 모두 설정될 경우 해당 조건에 해당하는 사용자의 프로필만 반환합니다. - 출신: UK, 거주: UK, 거주도시: LIVERPOOL")
    public void searchingUserProfilesWithOriginAndLivingCountryAndLivingTownAsConditionShowsOnlyMatchingUsers() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .originCountry(UNITED_KINGDOM)
                .livingCountry(UNITED_KINGDOM)
                .livingTown(LIVERPOOL)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 1);
        assertTrue(matchingProfiles.stream().allMatch(user -> SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL.getUserId().equals(user.getUserId())));
    }

    @Test
    @DisplayName("조건이 잘 설정되었더라도 경우 해당 조건에 맞는 사용자가 없으면 빈 리스트만 반환합니다.")
    public void searchingUserProfilesNoMatchedUsersReturnsOnlyEmptyList() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .originCountry(UNITED_KINGDOM)
                .livingCountry(UNITED_KINGDOM)
                .livingTown(ABERDEEN)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertTrue(matchingProfiles.isEmpty());
    }

    @Test
    @DisplayName("필수 조건외에 선택조건으로 최소 나이를 설정한 경우 설정한 최소나이로부터 나이의 최대 범위인 99살까지의 대상만 리턴합니다. - 최소나이 10살")
    public void searchingUserProfilesByMinAgeReturnsUsersFromTheMinToDefaultMaxAge() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .minAge(MIN_AGE_BOUND)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 3);
        assertTrue(matchingProfiles.stream().allMatch(user -> englishSpeakers.contains(user.getUserId())));
    }

    @Test
    @DisplayName("필수 조건외에 선택조건으로 최대 나이를 설정한 경우 설정한 최소 나이인 10살까지의 설정한 최대 나이까지의 대상만 리턴합니다. - 설정: 최대나이 30살")
    public void searchingUserProfilesByMaxAgeReturnsUsersFromDefaultMinToSetMaxAge() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .maxAge(30)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 1);
        assertTrue(matchingProfiles.stream().allMatch(user -> TENS_MALE_FROM_UK_LIVING_LONDON.getUserId().equals(user.getUserId())));
    }

    @Test
    @DisplayName("필수 조건외에 선택조건으로 최소 및 최대 나이를 설정한 경우 설정한 최소 나이 부터 설정한 최대 나이까지의 대상만을 리턴합니다. - 설정: 최소나이 20살, 최대나이 50살")
    public void searchingUserProfilesByMinAndMaxAgeReturnsUsersFromSetMinToSetMaxAge() {
        insertUsers();
        insertLanguages();

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .minAge(20)
                .maxAge(50)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 1);
        assertTrue(matchingProfiles.stream().allMatch(user -> FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId().equals(user.getUserId())));
    }

    @Test
    @DisplayName("설정한 필수, 선택 조건에 맞는 대상이 서비스 내에는 존재할지라도, 상대방이 현재 검색 중인 사용자를 차단했을 경우에는 대상의 프로필을 반환하지 않습니다.")
    public void searchingUserProfilesReturnsEmptyResultsAboutAnyUsersBlockingCurrentUser() {
        insertUsers();
        insertLanguages();
        blockUserService.blockUser(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId(), CURRENT_USER.getUserId());

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .minAge(20)
                .maxAge(50)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 0);
    }

    @Test
    @DisplayName("설정한 필수, 선택 조건에 맞는 대상이었으나 회원탈퇴로 비활성화 되어있는 사용자의 경우에는 해당 사용자의 프로필을 반환하지 않습니다.")
    public void searchingUserProfilesReturnsEmptyResultsAboutAnyDeactivatedUsers() {
        insertUsers();
        insertLanguages();
        assertTrue(userService.isUserActivated(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId()));
        userService.userDeleteAccount(FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getUserId(), FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL.getPassword());

        SearchConditionsRequest request = SearchConditionsRequest.builder()
                .speakLanguage(ENGLISH)
                .learningLanguage(KOREAN)
                .learningLanguageLevel(allLevelsWithoutNativeLevel)
                .minAge(20)
                .maxAge(50)
                .build();

        List<UserProfiles> matchingProfiles = profileService.searchUserProfiles(request, CURRENT_USER.getUserId(), pagination);

        assertEquals(matchingProfiles.size(), 0);
    }
}
