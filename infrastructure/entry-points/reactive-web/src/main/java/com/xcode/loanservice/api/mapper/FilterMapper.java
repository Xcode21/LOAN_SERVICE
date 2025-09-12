package com.xcode.loanservice.api.mapper;

import com.xcode.loanservice.api.dto.ApplicationFilterRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FilterMapper {
    
    public Map<String, Object> toFilterMap(ApplicationFilterRequest filterRequest) {
        Map<String, Object> filters = new HashMap<>();
        
        if (filterRequest.getDocument() != null && !filterRequest.getDocument().trim().isEmpty()) {
            filters.put("document", filterRequest.getDocument().trim());
        }
        
        if (filterRequest.getEmail() != null && !filterRequest.getEmail().trim().isEmpty()) {
            filters.put("email", filterRequest.getEmail().trim());
        }
        
        if (filterRequest.getLoanType() != null) {
            filters.put("loanType", filterRequest.getLoanType());
        }
        
        if (filterRequest.getMinAmount() != null) {
            filters.put("minAmount", filterRequest.getMinAmount());
        }
        
        if (filterRequest.getMaxAmount() != null) {
            filters.put("maxAmount", filterRequest.getMaxAmount());
        }
        
        if (filterRequest.getMinTerm() != null) {
            filters.put("minTerm", filterRequest.getMinTerm());
        }
        
        if (filterRequest.getMaxTerm() != null) {
            filters.put("maxTerm", filterRequest.getMaxTerm());
        }
        
        if (filterRequest.getDateFrom() != null) {
            filters.put("dateFrom", filterRequest.getDateFrom());
        }
        
        if (filterRequest.getDateTo() != null) {
            filters.put("dateTo", filterRequest.getDateTo());
        }
        
        return filters;
    }
}