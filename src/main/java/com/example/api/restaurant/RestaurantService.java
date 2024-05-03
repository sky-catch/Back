package com.example.api.restaurant;

import static com.example.api.restaurant.exception.RestaurantExceptionType.CAN_CREATE_ONLY_ONE;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_FOUND;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_UNIQUE_NAME;

import com.example.api.facility.StoreFacilityMapper;
import com.example.api.holiday.HolidayDTO;
import com.example.api.holiday.HolidayMapper;
import com.example.api.holiday.HolidayService;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservationavailabledate.ReservationAvailableDateDTO;
import com.example.api.reservationavailabledate.ReservationAvailableDateMapper;
import com.example.api.reservationavailabledate.ReservationAvailableDateService;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.GetRestaurantInfo;
import com.example.api.restaurant.dto.GetRestaurantInfoRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.UpdateRestaurantReq;
import com.example.api.restaurant.dto.enums.Category;
import com.example.api.restaurant.dto.enums.HotPlace;
import com.example.api.restaurant.dto.enums.KoreanCity;
import com.example.api.restaurant.dto.search.GetRestaurantSearchListRes;
import com.example.api.restaurant.dto.search.GetRestaurantSearchRes;
import com.example.api.restaurant.dto.search.GetRestaurantSearchSummaryRes;
import com.example.api.restaurant.dto.search.RestaurantSummaryDTO;
import com.example.api.restaurant.dto.search.SearchFilter;
import com.example.api.review.ReviewMapper;
import com.example.api.review.dto.GetReviewCommentRes;
import com.example.core.exception.SystemException;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ReservationAvailableDateMapper reservationAvailableDateMapper;
    private final HolidayMapper holidayMapper;

    @Transactional
    public long createRestaurant(CreateRestaurantReq req) {

        RestaurantDTO dto = new RestaurantDTO(req);
        if (restaurantMapper.isAlreadyCreated(dto.getOwnerId())) {
            throw new SystemException(CAN_CREATE_ONLY_ONE.getMessage());
        }

        if (restaurantMapper.isAlreadyExistsName(dto.getName())) {
            throw new SystemException(NOT_UNIQUE_NAME.getMessage());
        }

        dto.setHotPlace(HotPlace.getHotPlaceValue(dto.getDetailAddress()));
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

        if (restaurantMapper.isAlreadyExistsNameExcludeSelf(dto.getName(), req.getOwnerId())) {
            throw new SystemException(NOT_UNIQUE_NAME.getMessage());
        }

        dto.setHotPlace(HotPlace.getHotPlaceValue(dto.getDetailAddress()));
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
        ReservationAvailableDateDTO reservationAvailableDateDTO = reservationAvailableDateMapper.findByRestaurantId(
                getRestaurantInfoRes.getRestaurantId());
        List<HolidayDTO> holidayDTOS = holidayMapper.findByRestaurantId(getRestaurantInfoRes.getRestaurantId());
        return new GetRestaurantInfo(getRestaurantInfoRes, reviewComments, reservationAvailableDateDTO, holidayDTOS);
    }

    @Transactional(readOnly = true)
    public GetRestaurantSearchSummaryRes getSearchSummaryList(String keyword) {
        KoreanCity koreanCity = KoreanCity.searchKoreanCity(keyword);
        int koreanCityCount = 0;
        if (koreanCity != null) {
            koreanCityCount = restaurantMapper.getCountByAddress(koreanCity.getKoreanName());
        }
        HotPlace hotPlace = HotPlace.searchHotPlace(keyword);
        int hotPlaceCount = 0;
        if (hotPlace != null) {
            hotPlaceCount = restaurantMapper.getCountByHotPlace(hotPlace.getKoreanName());
        }

        Category category = Category.searchCategory(keyword);
        int categoryCount = 0;
        if (category != null) {
            categoryCount = restaurantMapper.getCountByCategory(category.getKoreanName());
        }

        List<RestaurantSummaryDTO> restaurantSummaryDTOs = restaurantMapper.searchNameByKeyword(keyword);

        String koreanCityName = (koreanCity == null) ? null : koreanCity.getKoreanName();
        String hotPlaceName = (hotPlace == null) ? null : hotPlace.getKoreanName();
        String categoryName = (category == null) ? null : category.getKoreanName();

        return new GetRestaurantSearchSummaryRes(koreanCityName, koreanCityCount,
                hotPlaceName, hotPlaceCount, categoryName, categoryCount, restaurantSummaryDTOs);
    }

    @Transactional(readOnly = true)
    public GetRestaurantSearchRes searchByFilter(SearchFilter filter, Long memberId) {
        long memberPk = (memberId == null) ? 0 : memberId;

        filter.setMemberId(memberPk);
        List<GetRestaurantSearchListRes> getRestaurantSearchListRes = restaurantMapper.searchByFilter(filter);
        getRestaurantSearchListRes.forEach(i ->
                i.setPossibleReservationTime(
                        calculatePossibleReservationTimes(filter.getTime(), i.getAlreadyReservationTime(),
                                i.getLastOrderTime())));

        return new GetRestaurantSearchRes(filter, getRestaurantSearchListRes.size(), getRestaurantSearchListRes);
    }

    private List<String> calculatePossibleReservationTimes(String startTime, List<String> dbTime,
                                                           LocalTime lastOrderTime) {
        TimeSlot reservationTime = new TimeSlot(LocalTime.parse(startTime));

        TimeSlots canReservation = TimeSlots.canReservation(reservationTime, lastOrderTime);
        TimeSlots alreadyReservation = TimeSlots.of(
                dbTime.stream().map(LocalTime::parse).map(TimeSlot::new).collect(Collectors.toList()));
        return canReservation.subtract(alreadyReservation).limit3().toTimeString();
    }

}