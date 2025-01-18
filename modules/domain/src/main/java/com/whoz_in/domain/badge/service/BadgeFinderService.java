package com.whoz_in.domain.badge.service;

import com.whoz_in.domain.badge.BadgeRepository;
import com.whoz_in.domain.badge.exception.SameBadgeExistException;
import com.whoz_in.domain.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class BadgeFinderService {
    private final BadgeRepository badgeRepository;

    public void mustNotExist(String name) {
        if (badgeRepository.findByName(name).isPresent()) {
            throw SameBadgeExistException.EXCEPTION;
        }
    }
}
