package com.cmms.app.dto.base.request;

import lombok.*;

@Getter
@Setter
public abstract class BaseGetAllRequest {
    protected Integer skipCount = 0;
    protected Integer maxResultCount = 10;
    protected String sortBy;
    protected String sortDirection = "asc";
}