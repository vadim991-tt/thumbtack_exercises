package net.thumbtack.school.notes.dao.providers;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class NoteDAOProvider {

    public String selectNoteLike(Map<String, String> map) {

        return new SQL() {
            {
                String id = map.get("id");
                String commentVersion = map.get("commentVersion");
                String obtainRevisionId = "";
                if (commentVersion != null && commentVersion.equals("true")) {
                    obtainRevisionId = "    comment.revisionId AS commentRevisionId,\n";
                }

                SELECT("n.id AS noteId,\n" +
                        "    n.subject,\n" +
                        "    n.body,\n" +
                        "    sectionId,\n" +
                        "    n.authorId,\n" +
                        "    n.created,\n" +
                        "    n.rating,\n" +
                        "    \n" +
                        "    noteAuthor.firstName,\n" +
                        "    noteAuthor.lastName,\n" +
                        "    noteAuthor.patronymic,\n" +
                        "    noteAuthor.login,\n" +
                        "    noteAuthor.`password`,\n" +
                        "    noteAuthor.rating AS noteAuthorRating,\n" +
                        "    noteAuthor.createdNotes,\n" +
                        "    noteAuthor.timeRegistered,\n" +
                        "    noteAuthor.`role`,\n" +
                        "    noteAuthor.isDeleted,\n" +
                        "    \n" +
                        "    revision.id AS revisionId,\n" +
                        "    revision.body AS revisionBody,\n" +
                        "    revision.created AS revisionCreated,\n" +
                        "    \n" +
                        "    section.name AS sectionName,\n" +
                        "    \n" +
                        "    comment.id AS commentId,\n" +
                        "    comment.body AS commentBody,\n" +
                        "    comment.authorId AS commentAuthorId,\n" +
                             obtainRevisionId +
                        "    comment.created AS commentCreated,\n" +
                        "    \n" +
                        "    commentAuthor.firstName AS commentAuthorFirstName,\n" +
                        "    commentAuthor.lastName AS commentAuthorLastName,\n" +
                        "    commentAuthor.patronymic AS commentAuthorPatronymic,\n" +
                        "    commentAuthor.login AS commentAuthorLogin,\n" +
                        "    commentAuthor.`password` AS commentAuthorPassword,\n" +
                        "    commentAuthor.rating AS commentAuthorRating,\n" +
                        "    commentAuthor.createdNotes AS commentAuthorCreatedNotes,\n" +
                        "    commentAuthor.timeRegistered commentAuthorRegistered,\n" +
                        "    commentAuthor.`role` commentAuthorRole,\n" +
                        "    commentAuthor.isDeleted commentAuthorIsDeleted");
                FROM("note n");
                LEFT_OUTER_JOIN("author AS noteAuthor ON n.authorId = noteAuthor.id");
                LEFT_OUTER_JOIN("section ON sectionId = section.id");
                LEFT_OUTER_JOIN("revision ON n.id = noteId");
                LEFT_OUTER_JOIN("`comment` ON revisionId = revision.id");
                LEFT_OUTER_JOIN("author AS commentAuthor ON comment.authorId = commentAuthor.id");

                String sectionCondition = map.get("sectionId");
                if (sectionCondition != null) {
                    AND().WHERE("sectionId = " + sectionCondition);
                }

                String tagString = map.get("tags");
                String allTagsCondition = map.get("allTags");
                if (tagString != null) {
                    String[] tags = tagString.split(",");
                    String regexString;
                    String preparedString;
                    for (int i = 0; i < tags.length; i++) {
                        tags[i] = "(?=.*" + tags[i].trim() + ")";
                    }

                    if (allTagsCondition != null && allTagsCondition.equals("true")) {
                        preparedString = String.join("|", tags);
                    } else {
                        preparedString = String.join("", tags);
                    }

                    regexString = "^" + preparedString + ".*$";
                    AND().WHERE(String.format("n.body REGEXP '%s'", regexString));
                }

                String timeFromCondition = map.get("timeFrom");
                if (timeFromCondition != null) {
                    AND().WHERE("n.created > " + timeFromCondition);
                }

                String timeToCondition = map.get("timeTo");
                if (timeFromCondition != null) {
                    AND().WHERE("n.created < " + timeToCondition);
                }

                String userCondition = map.get("user");
                if (userCondition != null) {
                    AND().WHERE("n.authorId = " + userCondition);
                }

                String includeCondition = map.get("include");
                if (includeCondition != null) {
                    switch (includeCondition) {
                        case "onlyFollowings":
                            String following = String.format("n.authorId IN (SELECT followedId FROM followed WHERE authorId = %s)", id);
                            AND().WHERE(following);
                        case "onlyIgnore":
                            String ignore = String.format("n.authorId IN (SELECT ignoredId FROM ignored WHERE authorId = %s)", id);
                            AND().WHERE(ignore);
                        case "notIgnore":
                            String notIgnore = String.format("n.authorId NOT IN (SELECT ignoredId FROM ignored WHERE authorId = %s)", id);
                            AND().WHERE(notIgnore);
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

                String orderCondition = map.get("sortByRating");
                if (orderCondition != null) {
                    if (orderCondition.equalsIgnoreCase("asc")) {
                        ORDER_BY("rating ASC");
                    } else if (orderCondition.equalsIgnoreCase("desc")) {
                        ORDER_BY("rating DESC");
                    }
                }


            }
        }.toString();
    }
}
