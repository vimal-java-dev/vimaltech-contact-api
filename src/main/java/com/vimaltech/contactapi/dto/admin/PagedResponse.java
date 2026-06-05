package com.vimaltech.contactapi.dto.admin;

import java.util.List;

public record PagedResponse<T>(

        List<T> content,

        int page,

        int size,

        long totalElements,

        int totalPages,

        boolean first,

        boolean last
) {
}