package com.example.api.holiday;

import com.example.api.holiday.exception.HolidayExceptionType;
import com.example.core.exception.SystemException;
import java.util.List;
import java.util.stream.Collectors;
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
    public void createHolidays(long restaurantId, Days days) {
        if (days == null || days.getDays().isEmpty()) {
            log.info("휴일이 없음");
            return;
        }

        List<Day> dayList = days.getDays();
        if (holidayMapper.isAlreadyExistsDays(restaurantId, dayList)) {
            throw new SystemException(HolidayExceptionType.ALREADY_EXISTS.getMessage());
        }

        List<HolidayDTO> holidayDTOs = dayList.stream()
                .map(day -> HolidayDTO.builder()
                        .restaurantId(restaurantId)
                        .day(day)
                        .build())
                .collect(Collectors.toList());

        holidayMapper.saveAll(holidayDTOs);
    }

    public void updateHolidays(long restaurantId, List<HolidayDTO> holidayDTOs) {
        holidayMapper.delete(restaurantId);
        holidayMapper.saveAll(holidayDTOs);
    }
}
