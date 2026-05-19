package com.vtlamdev.sourcebase.dao.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryRewriter;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SoftDeleteQueryRewriter implements QueryRewriter {

    private static final Pattern FROM_ALIAS_PATTERN = Pattern.compile("\\bfrom\\s+\\S+\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
    private static final String DELETED_CLAUSE_TEMPLATE = "%s.deleted = false";

    @Override
    public String rewrite(String query, Sort sort) {
        String lowerCaseQuery = query.toLowerCase(Locale.ROOT);
        if (lowerCaseQuery.contains(".deleted")) {
            return query;
        }

        String alias = resolveAlias(query);
        if (alias == null) {
            return query;
        }

        String deletedClause = DELETED_CLAUSE_TEMPLATE.formatted(alias);
        int orderByIndex = lowerCaseQuery.lastIndexOf(" order by ");
        String queryWithoutOrderBy = orderByIndex >= 0 ? query.substring(0, orderByIndex) : query;
        String orderByClause = orderByIndex >= 0 ? query.substring(orderByIndex) : "";

        String lowerCaseQueryWithoutOrderBy = queryWithoutOrderBy.toLowerCase(Locale.ROOT);
        if (lowerCaseQueryWithoutOrderBy.contains(" where ")) {
            return queryWithoutOrderBy + " and " + deletedClause + orderByClause;
        }

        return queryWithoutOrderBy + " where " + deletedClause + orderByClause;
    }

    private String resolveAlias(String query) {
        Matcher matcher = FROM_ALIAS_PATTERN.matcher(query);
        return matcher.find() ? matcher.group(1) : null;
    }

}
