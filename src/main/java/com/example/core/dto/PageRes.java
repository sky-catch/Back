package com.example.core.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class PageRes<T> {

    private boolean hasNext;        //다음 페이지가 있는지
    private int totalPage;          //총 페이지 수
    private long totalElements;     //총 결과 수
    private int pageSize;           //페이지 하나의 크기
    private int currentPageNumber;  //현재 페이지
    private int numberOfElements;   //현재 페이지의 데이터 개수(마지막 페이지일 경우 pageSize 보다 작을 수도 있다)
    private List<T> list;

    public PageRes(Page<T> page) {
        this.hasNext = page.hasNext();
        this.totalPage = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageSize = page.getSize();
        this.currentPageNumber = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.list = page.getContent();
    }
}
