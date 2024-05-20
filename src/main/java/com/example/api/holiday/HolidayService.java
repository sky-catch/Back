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
            log.info("휴일이 없음");
            return;
        }

        if (holidayMapper.isAlreadyExistsDays(restaurantId, holidays)) {
            throw new SystemException(HolidayExceptionType.ALREADY_EXISTS.getMessage());
        }

        holidayMapper.saveAll(restaurantId, holidays);
    }

    public void update(long restaurantId, Holidays holidays) {
        holidayMapper.delete(restaurantId);
        holidayMapper.saveAll(restaurantId, holidays);
    }
}
