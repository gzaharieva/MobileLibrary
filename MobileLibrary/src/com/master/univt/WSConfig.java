package com.master.univt;
/**
 * Web server configurations, URLs.
 *
 * @author Gabriela Zaharieva
 */
public final class WSConfig
{

    private WSConfig()
    {
    }

    public static final String ROOT_API = "https://www.googleapis.com/books/v1/";

    /** The user login / logout services. */
    public static final String WS_BOOKSHELFS = "bookshelves";
    public static final String WS_VOLUMES = "volumes";

    public static final String WS_USER_AUTHENTICATION_FACEBOOK = "fb-connect.json";
    public static final String WS_USER_AUTHENTICATION_XING = "xing-connect.json";

    /** The user content. */
    public static final String WS_USER_ACTIVE_CONTENT = "overview/accessible.json?user_learn_progress=1";

    public static final String WS_COURSE_FORMAT = "android/course/content/%s.json?user_learn_progress=1";
    public static final String WS_LECTURE_FORMAT = "android/lecture/%s.json";

    public static final String WS_SUBPORTALS = "subportals.json";
    public static final String WS_SUBPORTAL_COURSES = "android/subportal/%s.json";
    public static final String WS_SEARCH_COURSES = "android/search.json";

    public static final String WS_LECTURE_PROGRESS_FORMAT = "player/progress";
    public static final String WS_XING_PROTECTED_RESOURCE_URL = "https://api.xing.com/v1/users/me";
    public static final String WS_PAYMENT_VERIFICATION_URL = "android/purchase/verify.json";
    public static final String WS_LECTURE_WEB_SITE_URL_ENDING = "vortrag";
    public static final String WS_COURSE_WEB_SITE_URL_ENDING = "kurs";
    public static final String WS_E_LEARNINGE_URL = "e-learning/";

    public static final String WS_COURSE_DETAILS = "android/course/information/%s.html";
    public static final String WS_COURSE_REVIEWS = "android/course/reviews/%s.html";
    public static final String WS_LECTURE_QUESTIONS = "android/questions/%s.json";
    public static final String WS_LECTURE_SAVE_ANSWER = "android/questions/save-answers.json";
    public static final String WS_COURSES_FILE_SIZES = "lectures/filesizes.json";
    public static final String WS_LECTURES_VERIFY = "lectures/verify.json";

    public static String buildServiceURL(final String serviceURL, final String normalizedTitle)
    {
        return String.format(serviceURL, normalizedTitle);
    }
}
