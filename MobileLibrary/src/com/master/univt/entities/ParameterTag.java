package com.master.univt.entities;

/**
 * Parameter tags as constants.
 * 
 * @author Gabriela Zaharieva
 */
public final class ParameterTag
{

  /**
   * The class is final and hold only constants for the application. It can not be initialized.
   */
  private ParameterTag()
  {
  }

  public static final String PARAM_EMAIL = "email";
  public static final String PARAM_PASSWORD = "password";
  public static final String PARAM_SOURCE_OS = "source_os";
  public static final String PARAM_SOURCE_ANDROID = "android";

  public static final String ERROR_TAG = "error";

  public static final String COURSES_TAG = "courses";

  /** The unique UID of the user. */
  public static final String USER_UID_PREFERENCE = "user_uid";

  public static final String TITLE_ARG = "title";
  public static final String TITLE_NORMALIZED_ARG = "normalized_title";
  public static final String TITLE_SUBPORTAL_ARG = "subportal_title";
  public static final String LECTURE_ID_ARG = "lecture_id";
  public static final String SHOULD_CALL_API = "should_call_api";
  public static final String FRAGMENT_COURSE = "fragment_course";
  public static final String COURSE_VIEW = "course_view";
  public static final String IS_USER_DISCOVER_COURSES = "is_discover";
  public static final String PARAM_USER_BIRTHDAY = "user_birthday";

  public static final String PARAM_XING_USERS_JSON_TAG = "users";
  public static final String PARAM_SUBPORTALS_JSON_TAG = "subportals";
  public static final int RC_REQUEST_CODE_PAYMENT = 10001;

  /** Payment constans. */
  public static final String PARAM_PAYMENT_USERID = "userId";
  public static final String PARAM_PAYMENT_ORDER_ID = "orderId";
  public static final String PARAM_PAYMENT_PRODUCT_ID = "productId";
  public static final String PARAM_PAYMENT_TOKEN = "token";
  public static final String PARAM_PAYMENT_PRICE_AMOUNT_MICROS = "price_amount_micros";
  public static final String PARAM_PAYMENT_PRICE_CURRENCY_CODE = "price_currency_code";
  public static final String PARAM_TRANSACTION_ID = "transactionId";
  public static final String PARAM_SECURITY_TOKEN = "securityToken";
  public static final String PARAM_COURSE_INFORMATION_API_CALL = "course_information_api_call";
  public static final String PARAM_COURSE_SERIAZABLE = "course_seriazable";
  // Search parameters

}
