package com.master.univt.entities;


/**
 * Request request code status.
 * 
 * @author Gabriela Zaharieva
 */
public enum ResponseStatusCode
{
  /** Error code to indicate that excel file is invalid. */
  SUCCESFULL_AUTHENTICATION(1, 1),
  /** */
  ALREADY_LOGGEDIN_USER(2,  1),
  /** */
  LOGIN_FAILED(-1, 1),
  /** */
  AUTHENTICATION_FAILED(-1,  1),
  /** */
  AUTHENTICATION_FAILED_EMAIL_IN_USE(1,  1),
  /** */
  NO_CONTENT_ACCESS(403,  1),
  /** */
  COURSE_CONTENT_ACCESS(200,  1),
  /** */
  NO_SEARCH_RESULT_CONTENT(403,  1),
  /** */
  SEARCH_RESULT_COURSES_CONTENT_ACCESS(200, 1),
  /** */
  // TODO Add valid message
  PAYMENT_FAILED(-1,  1),
  /** */
  // TODO Add valid message
  PAYMENT_SUCCESSFULL(-1,  1);

  /** The code of the error. */
  private final int iErrorCode;
  /** The default error message. */
  private final int iDefaultErrorMessage;

  /**
   * Create a new instance of ErrorCode.
   * 
   * @param aDefaultErrorMessage the default error message
   * @param aErrorCode the error code for the message
   */
  ResponseStatusCode(final int aErrorCode, final int aDefaultErrorMessage)
  {
    iErrorCode = aErrorCode;
    iDefaultErrorMessage = aDefaultErrorMessage;
  }

  /**
   * Gets the error code.
   * 
   * @return the error code
   */
  public int getValue()
  {
    return iErrorCode;
  }

  /**
   * Gets the default error message.
   * 
   * @return the default error message
   */
  public int getDefaultMessage()
  {
    return iDefaultErrorMessage;
  }

  /**
   * Gets the message for current locale. If the message is not found then return the default one.
   * 
   * @return the message based on current locale
   */
  public int getMessage()
  {
    return getDefaultMessage();
  }

}
