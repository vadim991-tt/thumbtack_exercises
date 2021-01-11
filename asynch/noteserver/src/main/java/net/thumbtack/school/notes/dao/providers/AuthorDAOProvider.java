package net.thumbtack.school.notes.dao.providers;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class AuthorDAOProvider {

    public String selectAuthorLike(Map<String, String> map) {
        String unsortedSql = createUnsortedSql(map);
        String orderCondition = map.get("sortByRating");
        if (orderCondition != null) {
            return new SQL() {
                {
                    String preparedSql = "(" + unsortedSql + ") AS T";
                    SELECT("*").FROM(preparedSql);
                    if (orderCondition.equalsIgnoreCase("asc")) {
                        ORDER_BY("rating ASC");
                    } else if (orderCondition.equalsIgnoreCase("desc")) {
                        ORDER_BY("rating DESC");
                    }
                }
            }.toString();
        }
        return unsortedSql;
    }

    public String createUnsortedSql(Map<String, String> map) {
        return new SQL() {
            {
                String id = map.get("id");
                String superUserRequestCondition = map.get("superUserRequest");
                if (superUserRequestCondition != null) {
                    SELECT("a.id, firstname, lastname, patronymic, login, timeRegistered, uuid, isDeleted, role, rating");
                } else {
                    SELECT("a.id, firstname, lastname, patronymic, login, timeRegistered, uuid, isDeleted, rating");
                }
                FROM("author a");
                LEFT_OUTER_JOIN("`session` ON a.id = authorId");

                String typeCondition = map.get("type");
                if (typeCondition != null) {
                    switch (typeCondition) {
                        case ("highRating"):
                            ORDER_BY("rating ASC");
                            break;
                        case ("lowRating"):
                            ORDER_BY("rating DESC");
                            break;
                        case ("following"):
                            String following = String.format("a.id IN (SELECT followedId FROM followed WHERE authorId = %s)", id);
                            WHERE(following);
                            break;
                        case ("followers"):
                            String followers = String.format("a.id IN (SELECT authorId FROM followed WHERE followedId = %s)", id);
                            WHERE(followers);
                            break;
                        case ("ignore"):
                            String ignore = String.format("a.id IN (SELECT ignoredId FROM ignored WHERE authorId = %s)", id);
                            WHERE(ignore);
                            break;
                        case ("ignoredBy"):
                            String ignoredBy = String.format("a.id IN (SELECT authorId FROM ignored WHERE ignoredId = %s)", id);
                            WHERE(ignoredBy);
                            break;
                        case ("deleted"):
                            WHERE("isDeleted = true");
                            break;
                        case ("superUsers"):
                            WHERE("role = 'SUPER'");
                    }
                }

                String countCondition = map.get("count");
                if (countCondition != null) {
                    LIMIT(Integer.parseInt(countCondition));
                } else {
                    LIMIT(Integer.MAX_VALUE);
                }

                String fromCondition = map.get("from");
                if (fromCondition != null) {
                    OFFSET(Integer.parseInt(fromCondition));
                }
            }
        }.toString();
    }
}
