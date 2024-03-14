package com.example.api.holiday;

import com.example.api.holiday.exception.HolidayExceptionType;
import com.example.core.exception.SystemException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayMapper holidayMapper;

    @Transactional
    public void createHolidays(long restaurantId, Days days) {
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
}
