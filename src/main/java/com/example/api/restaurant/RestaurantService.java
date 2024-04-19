package com.example.api.restaurant;

import static com.example.api.restaurant.exception.RestaurantExceptionType.CAN_CREATE_ONLY_ONE;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_FOUND;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_UNIQUE_NAME;

import com.example.api.facility.StoreFacilityMapper;
import com.example.api.holiday.HolidayDTO;
import com.example.api.holiday.HolidayService;
import com.example.api.member.MemberDTO;
import com.example.api.reservationavailabledate.ReservationAvailableDateDTO;
import com.example.api.reservationavailabledate.ReservationAvailableDateService;
import com.example.api.restaurant.dto.*;
import com.example.api.restaurant.dto.enums.Category;
import com.example.api.restaurant.dto.enums.KoreanCity;
import com.example.api.restaurant.dto.search.*;
import com.example.api.review.ReviewMapper;
import com.example.api.review.dto.GetReviewCommentRes;
import com.example.core.exception.SystemException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.api.restaurant.exception.RestaurantExceptionType.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    public static final int MAX_SHOW_RESERVATION = 3;
    public static final int RESERVATION_TIME_INTERVAL = 30;
    private final RestaurantMapper restaurantMapper;
    private final StoreFacilityMapper storeFacilityMapper;
    private final HolidayService holidayService;
    private final ReservationAvailableDateService reservationAvailableDateService;
    private final ReviewMapper reviewMapper;

    @Transactional
    public long createRestaurant(CreateRestaurantReq req) {

        RestaurantDTO dto = new RestaurantDTO(req);
        if (restaurantMapper.isAlreadyCreated(dto.getOwnerId())) {
            throw new SystemException(CAN_CREATE_ONLY_ONE.getMessage());
        }

        if (restaurantMapper.isAlreadyExistsName(dto.getName())) {
            throw new SystemException(NOT_UNIQUE_NAME.getMessage());
        }

        restaurantMapper.save(dto);

        holidayService.createHolidays(dto.getRestaurantId(), req.getDays());

        reservationAvailableDateService.create(dto.getRestaurantId(),
                req.getReservationBeginDate(), req.getReservationEndDate());

        if (req.getFacilities() != null && !req.getFacilities().isEmpty()) {
            storeFacilityMapper.createFacility(dto.getRestaurantId(), req.getFacilities());
        }

        return dto.getRestaurantId();
    }

    @Transactional
    public void updateRestaurant(UpdateRestaurantReq req) {
        RestaurantDTO dto = new RestaurantDTO(req);

        if (restaurantMapper.isAlreadyExistsNameExcludeSelf(dto.getName(), dto.getRestaurantId())) {
            throw new SystemException(NOT_UNIQUE_NAME.getMessage());
        }

        restaurantMapper.updateRestaurant(dto);

        List<HolidayDTO> holidayDTOs = req.getDays().getDays().stream()
                .map(day -> new HolidayDTO(dto.getRestaurantId(), day))
                .collect(Collectors.toList());

        holidayService.update(dto.getRestaurantId(), holidayDTOs);
        reservationAvailableDateService.update(new ReservationAvailableDateDTO(req));

    }

    @Transactional(readOnly = true)
    public RestaurantDTO getRestaurantById(long restaurantId) {
        return restaurantMapper.findById(restaurantId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public GetRestaurantInfoRes getRestaurantInfoById(long restaurantId) {
        GetRestaurantInfoRes getRestaurantInfoRes = restaurantMapper.findRestaurantInfoById(restaurantId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
//        getRestaurantRes.sortImages();

        return getRestaurantInfoRes;
    }

    @Transactional(readOnly = true)
    public GetRestaurantInfo getRestaurantInfoByName(String name, Long memberId) {
        GetRestaurantInfoRes getRestaurantInfoRes = restaurantMapper.findRestaurantInfoByName(name, memberId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
        List<GetReviewCommentRes> reviewComments = reviewMapper.getReviewComments(
                getRestaurantInfoRes.getRestaurantId());
        return new GetRestaurantInfo(getRestaurantInfoRes, reviewComments);
    }

    public GetRestaurantSearchSummaryRes getSearchSummaryList(String keyword) {
        KoreanCity koreanCity = KoreanCity.searchKoreanCity(keyword);
        int koreanCityCount = 0;
        if (koreanCity != null) {
            koreanCityCount = restaurantMapper.getCountByAddress(koreanCity.getKoreanName());
        }

        Category category = Category.searchCategory(keyword);
        int categoryCount = 0;
        if (category != null) {
            categoryCount = restaurantMapper.getCountByCategory(category.getKoreanName());
        }

        List<RestaurantSummaryDTO> restaurantSummaryDTOs = restaurantMapper.searchNameByKeyword(keyword);
        return new GetRestaurantSearchSummaryRes(koreanCity, koreanCityCount, category, categoryCount, restaurantSummaryDTOs);
    }

    public GetRestaurantSearchRes getSearchList(SearchFilter filter, long memberId) {
        //order by, 저장했는지
        List<GetRestaurantSearchListRes> getRestaurantSearchListRes = restaurantMapper.searchByFilter(filter, memberId);
        getRestaurantSearchListRes.forEach(i -> {
            List<String> possibleReservationTimes =
                    calculatePossibleReservationTimes(filter.getDate(), filter.getTime(), i.getPossibleReservationTime(), i.getLastOrderTime());
            i.setPossibleReservationTime(possibleReservationTimes);
        });

        return new GetRestaurantSearchRes(filter, getRestaurantSearchListRes.size(), getRestaurantSearchListRes);
    }

    private List<String> calculatePossibleReservationTimes(String date, String time, List<String> dbTime, LocalTime lastOrderTime){
        LocalDateTime dateTime = parseToLDT(date + " " + time);
        List<LocalDateTime> result = new ArrayList<>();
        List<LocalDateTime> collect = dbTime.stream().map(this::parseToLDT).collect(Collectors.toList());
        int point = 0;
        while(result.size() < MAX_SHOW_RESERVATION
                && (dateTime.toLocalTime().isBefore(lastOrderTime) || dateTime.toLocalTime().equals(lastOrderTime))){
            if(collect.size() > point && collect.get(point).isEqual(dateTime)){
                dateTime = dateTime.plusMinutes(RESERVATION_TIME_INTERVAL);
                point++;
            }
            result.add(dateTime);
            dateTime = dateTime.plusMinutes(RESERVATION_TIME_INTERVAL);
        }
        return result.stream().map(this::parseToTime).collect(Collectors.toList());
    }

    private LocalDateTime parseToLDT(String stringTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(stringTime, formatter);
    }

    private String parseToTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }
}