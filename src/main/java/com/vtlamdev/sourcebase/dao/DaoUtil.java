package com.vtlamdev.sourcebase.dao;

import com.vtlamdev.sourcebase.common.data.id.UUIDBased;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.dao.model.ToData;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public final class DaoUtil {

    private DaoUtil() {
    }

    public static <T> PageData<T> toPageData(Page<? extends ToData<T>> page) {
        return new PageData<>(convertDataList(page.getContent()), page.getTotalPages(), page.getTotalElements(), page.hasNext());
    }

    public static <T> T getData(ToData<T> data) {
        return data != null ? data.toData() : null;
    }

    public static <T> T getData(Optional<? extends ToData<T>> data) {
        return data.map(ToData::toData).orElse(null);
    }

    public static <T> Optional<T> getOptionalData(Optional<? extends ToData<T>> data) {
        return data.map(ToData::toData);
    }

    public static <T> List<T> convertDataList(Collection<? extends ToData<T>> toConvert) {
        if (toConvert == null || toConvert.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> converted = new ArrayList<>(toConvert.size());
        for (ToData<T> entry : toConvert) {
            if (entry != null) {
                converted.add(entry.toData());
            }
        }
        return converted;
    }

    public static UUID getId(UUIDBased idBased) {
        return idBased != null ? idBased.getId() : null;
    }

    public static <I> I toEntityId(UUID uuid, Function<UUID, I> creator) {
        return uuid != null ? creator.apply(uuid) : null;
    }

}
