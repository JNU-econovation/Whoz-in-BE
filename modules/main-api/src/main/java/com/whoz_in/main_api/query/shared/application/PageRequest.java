package com.whoz_in.main_api.query.shared.application;

public interface PageRequest {

    int page();
    int size();
    String sortType();

}
