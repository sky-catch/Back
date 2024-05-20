package com.example.api.holiday;

import com.example.api.holiday.exception.HolidayExceptionType;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayMapper holidayMapper;

    @Transactional
    public void createHolidays(long restaurantId, Holidays holidays) {
        if (holidays == null || holidays.isEmpty()) {
            log.info("빈 휴일 매개 변수값 전달됨");
            return;
        }

        if (holidayMapper.isAlreadyExistsDays(restaurantId, holidays)) {
            log.error("ID 값 {} 식당은 {} 이 중 휴일이 등록되어 있습니다.", restaurantId, holidays);
            throw new SystemException(HolidayExceptionType.ALREADY_EXISTS.getMessage());
        }

        holidayMapper.saveAll(restaurantId, holidays);
    }

    public void update(long restaurantId, Holidays holidays) {
        holidayMapper.delete(restaurantId);
        holidayMapper.saveAll(restaurantId, holidays);
    }
}
