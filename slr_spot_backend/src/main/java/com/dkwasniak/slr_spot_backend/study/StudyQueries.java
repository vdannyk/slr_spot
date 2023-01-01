package com.dkwasniak.slr_spot_backend.study;

public class StudyQueries {

    public static final String STUDIES_BY_REVIEW_ID_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId ";

    // TO BE REVIEWED
    public static final String TO_BE_REVIEWED_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.stage = :stage AND sd.user.id = :userId) = 0 ";
    public static final String TO_BE_REVIEWED_TAGS_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "LEFT OUTER JOIN s.tags st " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.user.id = :userId) = 0 ";
    public static final String TO_BE_REVIEWED_FOLDERS_QUERY = TO_BE_REVIEWED_QUERY +
            "AND s.folder.id = :folderId ";


    // AWAITING
    public static final String AWAITING_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.stage = :stage AND sd.user.id = :userId) > 0 ";
    public static final String AWAITING_TAGS_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "LEFT OUTER JOIN s.tags st " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.stage = :stage AND sd.user.id = :userId) > 0 ";
    public static final String AWAITING_FOLDERS_QUERY = AWAITING_QUERY +
            "AND s.folder.id = :folderId ";

    // CONFLICTED
    public static final String CONFLICTED_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state ";
    public static final String CONFLICTED_TAGS_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "LEFT OUTER JOIN s.tags st " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state ";
    public static final String CONFLICTED_FOLDERS_QUERY = CONFLICTED_QUERY +
            "AND s.folder.id = :folderId ";

    // EXCLUDED
    public static final String EXCLUDED_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state ";
    public static final String EXCLUDED_TAGS_QUERY = "SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "LEFT OUTER JOIN s.tags st " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state ";
    public static final String EXCLUDED_FOLDERS_QUERY = EXCLUDED_QUERY +
            "AND s.folder.id = :folderId ";

    // SEARCHING CONDITIONS
    public static final String BY_TITLE_CONDITION =
            "AND s.title LIKE %:searchValue% ";
    public static final String BY_AUTHORS_CONDITION =
            "AND s.authors LIKE %:searchValue% ";
    public static final String BY_PUBLICATION_YEAR_CONDITION =
            "AND CAST(s.publicationYear as string) LIKE %:searchValue% ";
    public static final String BY_TITLE_AND_AUTHORS_CONDITION =
            "AND (s.title LIKE %:searchValue% " + "OR s.authors LIKE %:searchValue%) ";
    public static final String BY_TITLE_AND_PUBLICATION_YEAR_CONDITION = "AND (s.title LIKE %:searchValue% " +
            "OR CAST(s.publicationYear as string) LIKE %:searchValue%) ";
    public static final String BY_AUTHORS_AND_PUBLICATION_YEAR_CONDITION = "AND (s.authors LIKE %:searchValue% " +
            "OR CAST(s.publicationYear as string) LIKE %:searchValue%) ";
    public static final String BY_TITLE_AUTHORS_PUBLICATION_YEAR_CONDITION = "AND (s.title LIKE %:searchValue% " +
            "OR s.authors LIKE %:searchValue% " +
            "OR CAST(s.publicationYear as string) LIKE %:searchValue%) ";
    public static final String BY_EVERYTHING_CONDITION = "AND (s.title LIKE %:searchValue% " +
            "OR s.authors LIKE %:searchValue% " +
            "OR s.journalTitle LIKE %:searchValue% " +
            "OR CAST(s.publicationYear as string) LIKE %:searchValue% " +
            "OR s.volume LIKE %:searchValue% " +
            "OR s.doi LIKE %:searchValue% " +
            "OR s.url LIKE %:searchValue% " +
            "OR s.documentAbstract LIKE %:searchValue% " +
            "OR s.issn LIKE %:searchValue% " +
            "OR s.language LIKE %:searchValue% " +
            "OR st.name IS NOT NULL AND st.name LIKE %:searchValue%) " +
            "GROUP BY s";

}
